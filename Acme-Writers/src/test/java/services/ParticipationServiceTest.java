
package services;

import java.text.ParseException;
import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Book;
import domain.Participation;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ParticipationServiceTest extends AbstractTest {

	@Autowired
	private ParticipationService	participationService;

	@Autowired
	private BookService				bookService;


	@Override
	@Before
	public void setUp() {
		this.unauthenticate();
	}

	/**
	 * This test reefer to use case 21.b and 22.b(Acme-Writers)
	 * here we're going to test the create/edit of participations
	 * One positive
	 * One negative
	 * 
	 * @throws ParseException
	 */
	@Test
	public void saveParticipationDriver() throws ParseException {
		final Object testingData[][] = {

			//Correct create
			{
				/**
				 * a) 21.b and 22.b(Acme-Writers): writers can participate in a contest
				 * b) Positive
				 * c) 100%
				 * d) 45%
				 * 
				 */
				"writer0", new Participation(), this.getEntityId("contest2"), "comment0", this.bookService.findOne(this.getEntityId("book0")), "comment0", null, null, null
			}, //Incorrect create
			{
				/**
				 * a) 21.b and 22.b(Acme-Writers): writers can participate in a contest
				 * b) a writer cannot participate in a contest several times with the same book
				 * c) 50%
				 * d) 45%
				 * 
				 */
				"writer0", new Participation(), this.getEntityId("contest0"), "comment0", this.bookService.findOne(this.getEntityId("book0")), null, null, IllegalArgumentException.class
			},
			//Correct edit
			{
				/**
				 * a) 21.b and 22.b(Acme-Writers): Publishers can manage their participations
				 * b) Positive
				 * c) 100%
				 * d) 45%
				 * 
				 */
				"publisher0", this.participationService.findOne(this.getEntityId("participation0")), null, null, null, "REJECTED", null, null
			}, //Incorrect edit
			{
				/**
				 * a) 21.b and 22.b(Acme-Writers): Publishers can manage their participations
				 * b) A publisher cannot manage the participations in a contest that is not its own
				 * c) 50%
				 * d) 45%
				 * 
				 */
				"publisher1", this.participationService.findOne(this.getEntityId("participation0")), null, null, null, "REJECTED", null, IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.saveParticipationTemplate((String) testingData[i][0], (Participation) testingData[i][1], (Integer) testingData[i][2], (String) testingData[i][3], (Book) testingData[i][4], (String) testingData[i][5], (Integer) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}
	protected void saveParticipationTemplate(final String beanName, Participation participation, final Integer contest, final String comment, final Book book, final String status, final Integer position, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			super.authenticate(beanName);
			if (participation.getId() == 0) {
				participation = this.participationService.create(contest);
				participation.setStatus("PENDING");
				final Date actual = DateTime.now().toDate();
				participation.setMoment(actual);
				participation.setBook(book);
				participation.setComment(comment);
			} else {
				participation.setStatus(status);
				participation.setPosition(position);
			}
			this.participationService.save(participation);
			this.participationService.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
	/**
	 * This test reefer to use case 21.b(Acme-Writers)
	 * here we're going to test the delete of participations
	 * One positive
	 * One negative
	 * 
	 * @throws ParseException
	 */
	@Test
	public void deleteParticipationDriver() throws ParseException {
		final Object testingData[][] = {

			//Correct delete
			{
				/**
				 * a) 21.b(Acme-Writers): Writers can manage their participations
				 * b) Positive
				 * c) 100%
				 * d)
				 * 
				 */
				"writer0", this.participationService.findOne(this.getEntityId("participation0")), null
			}, {
				/**
				 * a) 21.b(Acme-Writers): Writers can manage their participations
				 * b) A writer cannot eliminate the participation of another.
				 * c) 33.33%
				 * d)
				 * 
				 */
				"writer1", this.participationService.findOne(this.getEntityId("participation0")), IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteParticipationTemplate((String) testingData[i][0], (Participation) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	protected void deleteParticipationTemplate(final String beanName, final Participation participation, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			super.authenticate(beanName);
			this.participationService.delete(participation);
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
