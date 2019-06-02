
package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import security.LoginService;
import utilities.AbstractTest;
import domain.Contest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ContestServiceTest extends AbstractTest {

	@Autowired
	private ContestService			contestService;

	@Autowired
	private PublisherService		publisherService;

	private final SimpleDateFormat	FORMAT	= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


	@Override
	@Before
	public void setUp() {
		this.unauthenticate();
	}

	/**
	 * This test reefer to use case 10.1(Acme-Writers)
	 * here we're going to test the create of contests
	 * One positive
	 * One negative
	 * 
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void saveContestDriver() throws ParseException {

		final Collection<String> rules = new ArrayList<String>();
		rules.add("rule1");
		final Date deadline = this.FORMAT.parse("2020/11/11 11:11:11");
		final Object testingData[][] = {

			//Correct create
			{
				/**
				 * a) 10.1(Acme-Writers): Publishers can manage their contests, which includes listing, showing, creating, updating, and deleting them
				 * b) Positive
				 * c) 100%
				 * d)
				 * 
				 */
				"publisher0", new Contest(), "title", "description", "prize", rules, deadline, null
			}, {
				/**
				 * a) 10.1(Acme-Rookie): Publishers can manage his or her catalogue of items, which includes listing, showing, creating, updating, and deleting them
				 * b) Must be a publisher(administrator)
				 * c) 33.33%
				 * d)
				 * 
				 */
				"admin", new Contest(), "title", "description", "prize", rules, deadline, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.saveContestTemplate((String) testingData[i][0], (Contest) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Collection<String>) testingData[i][5], (Date) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}
	protected void saveContestTemplate(final String beanName, final Contest contest, final String title, final String description, final String prize, final Collection<String> rules, final Date deadline, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			super.authenticate(beanName);
			contest.setPublisher(this.publisherService.findByPrincipal(LoginService.getPrincipal()));
			contest.setDeadline(deadline);
			contest.setDescription(description);
			contest.setPrize(prize);
			contest.setTitle(title);
			contest.setRules(rules);
			this.contestService.save(contest);
			this.contestService.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	/**
	 * This test reefer to use case 10.1(Acme-Writers)
	 * here we're going to test the delete of contests
	 * One positive
	 * One negative
	 * 
	 * @throws ParseException
	 */
	@Test
	public void deleteContestDriver() throws ParseException {
		final Object testingData[][] = {

			//Correct delete
			{
				/**
				 * a) 10.1(Acme-Writers): Publishers can manage their contests, which includes listing, showing, creating, updating, and deleting them
				 * b) Positive
				 * c) 100%
				 * d)
				 * 
				 */
				"publisher0", this.contestService.findOne(this.getEntityId("contest0")), null
			}, {
				/**
				 * a) 10.1(Acme-Rookie): Publishers can manage his or her catalogue of items, which includes listing, showing, creating, updating, and deleting them
				 * b) Must be a publisher(administrator)
				 * c) 33.33%
				 * d)
				 * 
				 */
				"publisher1", this.contestService.findOne(this.getEntityId("contest1")), IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteContestTemplate((String) testingData[i][0], (Contest) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	protected void deleteContestTemplate(final String beanName, final Contest contest, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			super.authenticate(beanName);
			this.contestService.delete(contest);
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
