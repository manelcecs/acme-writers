
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
	 * This test reefer to use case 10.1(Acme-Writers)
	 * here we're going to test the create/edit of contests
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
				 * a) 10.1(Acme-Writers): Publishers can manage their contests, which includes listing, showing, creating, updating, and deleting them
				 * b) Positive
				 * c) 100%
				 * d)
				 * 
				 */
				"writer0", new Participation(), this.getEntityId("contest2"), "comment0", this.bookService.findOne(this.getEntityId("book0")), "comment0", null, null, null
			}, //Incorrect create
			{
				/**
				 * a) 10.1(Acme-Rookie): Publishers can manage his or her catalogue of items, which includes listing, showing, creating, updating, and deleting them
				 * b) Must be a publisher(administrator)
				 * c) 33.33%
				 * d)
				 * 
				 */
				"writer0", new Participation(), this.getEntityId("contest0"), "comment0", this.bookService.findOne(this.getEntityId("book0")), null, null, IllegalArgumentException.class
			},
			//Correct edit
			{
				/**
				 * a) 10.1(Acme-Writers): Publishers can manage their contests, which includes listing, showing, creating, updating, and deleting them
				 * b) Positive
				 * c) 100%
				 * d)
				 * 
				 */
				"publisher0", this.participationService.findOne(this.getEntityId("participation0")), null, null, null, "REJECTED", null, null
			}, //Incorrect edit
			{
				/**
				 * a) 10.1(Acme-Rookie): Publishers can manage his or her catalogue of items, which includes listing, showing, creating, updating, and deleting them
				 * b) Must be a publisher(administrator)
				 * c) 33.33%
				 * d)
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
	 * This test reefer to use case 10.1(Acme-Writers)
	 * here we're going to test the delete of contests
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
				 * a) 10.1(Acme-Writers): Publishers can manage their contests, which includes listing, showing, creating, updating, and deleting them
				 * b) Positive
				 * c) 100%
				 * d)
				 * 
				 */
				"writer0", this.participationService.findOne(this.getEntityId("participation0")), null
			}, {
				/**
				 * a) 10.1(Acme-Rookie): Publishers can manage his or her catalogue of items, which includes listing, showing, creating, updating, and deleting them
				 * b) Must be a publisher(administrator)
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
