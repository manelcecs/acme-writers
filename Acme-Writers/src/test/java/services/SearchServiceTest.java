
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Book;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SearchServiceTest extends AbstractTest {

	@Autowired
	private BookService	bookService;


	/**
	 * This test reefer to requirement 19.f of Acme-Writers
	 * here we're going to test the search of books by keyword
	 * One positive
	 * Two negatives
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void searchDriver() {

		final Collection<Book> books1 = new ArrayList<Book>();
		final Collection<Book> books2 = new ArrayList<Book>();

		books2.add(this.bookService.findOne(this.getEntityId("book0")));

		final Object testingData[][] = {
			{
				/**
				 * a) 19.f: search books by keyword
				 * b) Positive
				 * c) 100%
				 * d)
				 * 
				 */
				null, "qwertyu", books1, null
			}, {
				/**
				 * a) 19.f: search books by keyword
				 * b) Negative: search results do not match
				 * c) 100%
				 * d)
				 * 
				 */
				null, "sirena", books1, IllegalArgumentException.class
			}, {
				/**
				 * a) 19.f: search books by keyword
				 * b) Negative: cannot search for HTML code
				 * c) 100%
				 * d)
				 * 
				 */
				null, "qwertyu", books2, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.searchTemplate((String) testingData[i][0], (String) testingData[i][1], (Collection<Book>) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	protected void searchTemplate(final String beanName, final String keyword, final Collection<Book> resultsExpected, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			super.authenticate(beanName);
			final Collection<Book> results = this.bookService.getFilterBooksByKeyword(keyword);
			Assert.isTrue(resultsExpected.containsAll(results) && resultsExpected.size() == results.size());
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
