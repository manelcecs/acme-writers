
package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.joda.time.LocalDateTime;
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
	private Validator				validator;

	private final SimpleDateFormat	FORMAT	= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


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

	public Participation save(final Participation participation) throws ParseException {
		if (participation.getId() == 0) {
			Assert.isTrue(AuthorityMethods.chechAuthorityLogged("WRITER"));
			Assert.isTrue(this.contestService.isBeforeDeadline(participation.getContest().getDeadline()));

			final UserAccount principal = LoginService.getPrincipal();
			final Writer writer = this.writerService.findByPrincipal(principal);
			Assert.isTrue(participation.getBook().getWriter().getId() == writer.getId());
		} else {
			Assert.isTrue(AuthorityMethods.chechAuthorityLogged("PUBLISHER"));
			Assert.isTrue(!this.contestService.isBeforeDeadline(participation.getContest().getDeadline()));
			final UserAccount principal = LoginService.getPrincipal();
			final Publisher publisher = this.publisherService.findByPrincipal(principal);
			Assert.isTrue(participation.getContest().getPublisher().getId() == publisher.getId());
		}
		return this.participationRepository.save(participation);
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

	public Participation reconstruct(final Participation participation, final BindingResult binding) throws ParseException {
		Participation participationRec;
		if (participation.getId() == 0) {
			participationRec = participation;
			participationRec.setStatus("PENDING");
			final LocalDateTime DATETIMENOW = LocalDateTime.now();
			final Date actual = this.FORMAT.parse(DATETIMENOW.getYear() + "/" + DATETIMENOW.getMonthOfYear() + "/" + DATETIMENOW.getDayOfMonth() + " " + DATETIMENOW.getHourOfDay() + ":" + LocalDateTime.now().getMinuteOfHour() + ":"
				+ DATETIMENOW.getSecondOfMinute());
			participationRec.setMoment(actual);
		} else {
			participationRec = this.participationRepository.findOne(participation.getId());
			participationRec.setStatus(participation.getStatus());
			participationRec.setPosition(participation.getPosition());
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
}
