
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

import repositories.OpinionRepository;
import security.LoginService;
import utiles.AuthorityMethods;
import domain.Book;
import domain.Opinion;
import domain.Reader;

@Service
@Transactional
public class OpinionService {

	@Autowired
	private OpinionRepository		opinionRepository;

	@Autowired
	private ReaderService			readerService;

	@Autowired
	Validator						validator;

	private final SimpleDateFormat	FORMAT	= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


	public Opinion save(final Opinion opinion) {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("READER"));
		Assert.isTrue(opinion.getReader().equals(this.readerService.findByPrincipal(LoginService.getPrincipal())));
		return this.opinionRepository.save(opinion);
	}

	public void delete(final int idOpinion) {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("READER"));
		final Opinion opinion = this.findOne(idOpinion);
		Assert.isTrue(opinion.getReader().equals(this.readerService.findByPrincipal(LoginService.getPrincipal())));
		this.opinionRepository.delete(opinion);
	}

	public Opinion findOne(final int idOpinion) {
		return this.opinionRepository.findOne(idOpinion);
	}

	public Collection<Opinion> findOpinionsByReader(final int idReader) {
		return this.opinionRepository.findOpinionsByReader(idReader);
	}

	public Opinion create(final Book book) {
		final Opinion opinion = new Opinion();
		opinion.setBook(book);
		return opinion;
	}

	public Opinion reconstruct(final Opinion opinion, final BindingResult binding) throws ParseException {
		final Reader reader = this.readerService.findByPrincipal(LoginService.getPrincipal());

		Opinion opinionRec;
		if (opinion.getId() == 0) {
			opinionRec = opinion;
			opinionRec.setReader(reader);
			final LocalDateTime DATETIMENOW = LocalDateTime.now();
			final Date actual = this.FORMAT.parse(DATETIMENOW.getYear() + "/" + DATETIMENOW.getMonthOfYear() + "/" + DATETIMENOW.getDayOfMonth() + " " + DATETIMENOW.getHourOfDay() + ":" + LocalDateTime.now().getMinuteOfHour() + ":"
				+ DATETIMENOW.getSecondOfMinute());
			opinionRec.setMoment(actual);
		} else
			opinionRec = this.opinionRepository.findOne(opinion.getId());

		opinionRec.setReview(opinion.getReview());
		opinionRec.setPositiveOpinion(opinion.getPositiveOpinion());

		this.validator.validate(opinionRec, binding);
		if (binding.hasErrors())
			throw new ValidationException();

		return opinionRec;
	}

}
