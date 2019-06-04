
package services;

import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Announcement;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AnnouncementServiceTest extends AbstractTest {

	@Autowired
	private AnnouncementService	annService;

	@Autowired
	private WriterService		writerService;

	@Autowired
	private ReaderService		readerService;


	@Override
	@Before
	public void setUp() {
		this.unauthenticate();
	}

	@Test
	public void createAnnouncement() {

		final Object testingData[][] = {
			{/*
			 * Create an announcement, logged as a writer, legalData
			 * a)
			 * b)Positivo
			 * c)100%
			 * d)100%
			 */
				"writer1", true, null
			}, {/*
				 * Create an announcement, logged as a writer, invalid data
				 * a)
				 * b)Negativo
				 * c)10/11
				 * d)100%
				 */
				"writer0", false, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createAnnouncementDriver((String) testingData[i][0], (boolean) testingData[i][1], (Class<?>) testingData[i][2]);

	}
	private void createAnnouncementDriver(final String string, final boolean b, final Class<?> class1) {

		Class<?> expected = null;

		try {
			this.authenticate(string);
			final Announcement a = this.annService.create();
			if (b)
				a.setText("This is a valid Announcement!");
			else
				a.setText(null);

			this.annService.save(a);
			this.annService.flush();

		} catch (final Throwable oops) {
			expected = oops.getClass();
		}

		this.checkExceptions(expected, class1);

	}

	@Test
	public void seeAnnouncement() {

		final Object testingData[][] = {
			{/*
			 * See the list of announcements, logged as a reader
			 * a)
			 * b)Positivo
			 * c)100%
			 * d)100%
			 */
				"reader0", null
			}, {/*
				 * See the list of announcements, not logged
				 * * a)
				 * b)Positivo
				 * c)100%
				 * d)100%
				 */
				null, null
			}, {/*
				 * See the list of announcements, logged as a writer
				 * a)
				 * b)Positivo
				 * c)100%
				 * d)100%
				 */
				"writer1", null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.listOfAnnouncements((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	private void listOfAnnouncements(final String string, final Class<?> class1) {
		Class<?> expected = null;

		try {
			this.authenticate(string);
			final Collection<Announcement> announcements = this.annService.findAll();

		} catch (final Throwable oops) {
			expected = oops.getClass();
		}

		this.checkExceptions(expected, class1);

	}
}
