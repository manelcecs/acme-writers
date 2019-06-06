
package services;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import security.LoginService;
import utilities.AbstractTest;
import domain.Book;
import domain.Opinion;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class OpinionServiceTest extends AbstractTest {

	@Autowired
	private OpinionService	opinionService;

	@Autowired
	private BookService		bookService;

	@Autowired
	private ReaderService	readerService;


	@Override
	@Before
	public void setUp() {
		this.unauthenticate();
	}

	@Test
	public void createAnnouncement() {

		final Object testingData[][] = {
			{/*
			 * Create an announcement, logged as a reader, Legal data
			 * a)
			 * b)Positivo
			 * c)
			 * d)100%
			 */
				"reader1", true, null
			}, {/*
				 * Create an opinion, logged as a writer, legalData
				 * a)
				 * b)Negativo
				 * c)%
				 * d)100%
				 */
				"writer1", true, IllegalArgumentException.class
			}, {/*
				 * Create an announcement, logged as a reader, invalid data
				 * a)
				 * b)Negativo
				 * c)
				 * d)100%
				 */
				"reader0", false, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createOpinionDriver((String) testingData[i][0], (boolean) testingData[i][1], (Class<?>) testingData[i][2]);

	}
	private void createOpinionDriver(final String string, final boolean b, final Class<?> class1) {

		Class<?> expected = null;

		try {
			this.authenticate(string);
			final List<Book> books = (List<Book>) this.bookService.getAllVisibleBooks();
			final SecureRandom randomGenerator = SecureRandom.getInstance("SHA1PRNG");
			final Book book = books.get(randomGenerator.nextInt(books.size()));
			final Opinion o = new Opinion();
			o.setBook(book);
			o.setReader(this.readerService.findByPrincipal(LoginService.getPrincipal()));
			if (b) {
				o.setPositiveOpinion(b);
				o.setReview("This is a valid opinion");
			} else {
				o.setPositiveOpinion(b);
				o.setReview(null);
			}
			o.setMoment(new Date());
			final Opinion result = this.opinionService.save(o);
			this.opinionService.flush();

		} catch (final Throwable oops) {
			expected = oops.getClass();
		}

		this.checkExceptions(expected, class1);

	}
}
