
package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ContestRepository;
import security.LoginService;
import security.UserAccount;
import utiles.AuthorityMethods;
import domain.Contest;
import domain.Publisher;

@Service
@Transactional
public class ContestService {

	@Autowired
	private ContestRepository		contestRepository;

	@Autowired
	private PublisherService		publisherService;

	private final SimpleDateFormat	FORMAT	= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


	public Contest findOne(final int idContest) {
		return this.contestRepository.findOne(idContest);
	}

	public Contest save(final Contest contest) throws ParseException {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("PUBLISHER"));
		Assert.isTrue(contest.getId() == 0);
		final UserAccount principal = LoginService.getPrincipal();
		final Publisher publisher = this.publisherService.findByPrincipal(principal);
		Assert.isTrue(this.isBeforeDeadline(contest.getDeadline()));
		contest.setPublisher(publisher);
		return this.contestRepository.save(contest);
	}

	public void delete(final Contest contest) throws ParseException {

		final UserAccount principal = LoginService.getPrincipal();
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("PUBLISHER"));
		final Publisher publisher = this.publisherService.findByPrincipal(principal);
		Assert.isTrue(contest.getPublisher().getId() == publisher.getId());
		Assert.isTrue(this.isBeforeDeadline(contest.getDeadline()));
		this.contestRepository.delete(contest);

	}

	public Collection<Contest> getContestsOfPublisher(final int idPublisher) {
		return this.contestRepository.getContestsOfPublisher(idPublisher);
	}

	public Boolean isBeforeDeadline(final Date deadline) throws ParseException {
		final LocalDateTime DATETIMENOW = LocalDateTime.now();
		final Date actual = this.FORMAT.parse(DATETIMENOW.getYear() + "/" + DATETIMENOW.getMonthOfYear() + "/" + DATETIMENOW.getDayOfMonth() + " " + DATETIMENOW.getHourOfDay() + ":" + LocalDateTime.now().getMinuteOfHour() + ":"
			+ DATETIMENOW.getSecondOfMinute());
		return deadline.after(actual);
	}

	public Collection<Contest> findAll() {
		return this.contestRepository.findAll();
	}

}
