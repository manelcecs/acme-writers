
package services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ParticipationRepository;
import security.LoginService;
import security.UserAccount;
import utiles.AuthorityMethods;
import domain.Contest;
import domain.Participation;
import domain.Publisher;
import domain.Writer;

@Service
@Transactional
public class ParticipationService {

	@Autowired
	private ParticipationRepository	participationRepository;

	@Autowired
	private ContestService			contestService;

	@Autowired
	private WriterService			writerService;

	@Autowired
	private PublisherService		publisherService;

	@Autowired
	private BookService				bookService;

	@Autowired
	private Validator				validator;


	public Participation create(final int idContest) throws ParseException {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("WRITER"));
		final Participation participation = new Participation();
		final Contest contest = this.contestService.findOne(idContest);
		participation.setContest(contest);
		return participation;
	}

	public Participation findOne(final int idParticipation) {
		return this.participationRepository.findOne(idParticipation);
	}

	public Collection<Participation> getParticipationsOfWriter(final int idWriter) {
		return this.participationRepository.getParticipationsOfWriter(idWriter);
	}

	public Collection<Participation> getParticipationsOfPublisher(final int idPublisher) {
		return this.participationRepository.getParticipationsOfPublisher(idPublisher);
	}

	public Participation save(final Participation participation) throws ParseException {
		Participation participationSave;
		if (participation.getId() == 0) {
			Assert.isTrue(AuthorityMethods.chechAuthorityLogged("WRITER"));
			Assert.isTrue(this.contestService.isBeforeDeadline(participation.getContest().getDeadline()));
			final UserAccount principal = LoginService.getPrincipal();
			final Writer writer = this.writerService.findByPrincipal(principal);
			Assert.isTrue(participation.getBook().getWriter().getId() == writer.getId());
			Assert.isTrue(this.bookService.getBooksCanParticipate(participation.getContest().getId()).contains(participation.getBook()));
		} else {
			Assert.isTrue(AuthorityMethods.chechAuthorityLogged("PUBLISHER"));
			final UserAccount principal = LoginService.getPrincipal();
			final Publisher publisher = this.publisherService.findByPrincipal(principal);
			Assert.isTrue(participation.getContest().getPublisher().getId() == publisher.getId());
		}
		participationSave = this.participationRepository.save(participation);
		return participationSave;
	}

	public Participation saveAnonimize(final Participation participation) throws ParseException {
		Participation participationSave;
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("WRITER"));
		participationSave = this.participationRepository.save(participation);
		return participationSave;
	}

	public void delete(final Participation participation) {

		final UserAccount principal = LoginService.getPrincipal();
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("WRITER"));
		final Writer writer = this.writerService.findByPrincipal(principal);
		Assert.isTrue(participation.getBook().getWriter().getId() == writer.getId());
		this.participationRepository.delete(participation);

	}

	public void deleteCollectionOfParticipations(final Collection<Participation> participations) {
		this.participationRepository.delete(participations);
	}

	public void flush() {
		this.participationRepository.flush();
	}

	public Participation reconstruct(final Participation participation, final BindingResult binding) throws ParseException {
		Participation participationRec;
		if (participation.getId() == 0) {
			participationRec = participation;
			participationRec.setStatus("PENDING");
			final Date actual = DateTime.now().toDate();
			participationRec.setMoment(actual);
		} else {
			participationRec = this.participationRepository.findOne(participation.getId());
			if (this.contestService.isBeforeDeadline(participationRec.getContest().getDeadline())) {
				final Publisher publisher = this.publisherService.findByPrincipal(LoginService.getPrincipal());
				Assert.isTrue(participationRec.getContest().getPublisher().equals(publisher));
				Assert.isTrue(participationRec.getStatus().equals("PENDING"));
				Assert.isTrue(participation.getStatus().equals("ACCEPTED") || participation.getStatus().equals("REJECTED"));
				participationRec.setStatus(participation.getStatus());
			} else if (participationRec.getStatus().equals("ACCEPTED")) {
				final List<Integer> positions = this.getAvailablePositionsInAContest(participationRec.getContest().getId());
				if (!(positions.contains(participation.getPosition()) || participation.getPosition() == null))
					binding.rejectValue("position", "participation.edit.position.error");
				participationRec.setPosition(participation.getPosition());
			}
		}

		this.validator.validate(participationRec, binding);
		if (binding.hasErrors()) {
			binding.getAllErrors();
			throw new ValidationException();
		}
		return participationRec;
	}

	public Collection<Participation> getParticipationsOfContest(final int idContest) {
		return this.participationRepository.getParticipationsOfContest(idContest);
	}

	public List<Integer> getAvailablePositionsInAContest(final int idContest) {
		final Integer numOfPositions = this.participationRepository.getNumberOfParticipations(idContest);
		int i = 1;
		final List<Integer> positions = new ArrayList<Integer>();
		while (i <= numOfPositions) {
			positions.add(i);
			i++;
		}
		positions.removeAll(this.getAvailablePositions(idContest));
		return positions;
	}

	public List<Integer> getAvailablePositions(final int idContest) {
		return this.participationRepository.getAvailablePositions(idContest);
	}

}
