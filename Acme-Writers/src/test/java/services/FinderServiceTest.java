
package services;

import java.text.ParseException;
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
import domain.Finder;
import domain.Genre;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class FinderServiceTest extends AbstractTest {

	@Autowired
	private BookService		bookService;

	@Autowired
	private FinderService	finderService;

	@Autowired
	private GenreService	genreService;


	/**
	 * This test reefer to use case 11
	 * here we're going to test the writers's finder
	 * One positive
	 * One negative
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void saveFinderDriver() throws ParseException {

		final Collection<Book> books = new ArrayList<Book>();

		books.add(this.bookService.findOne(this.getEntityId("book0")));

		final Object testingData[][] = {

			//Correct
			{
				/**
				 * a) 11: writers have finders
				 * b) Positive
				 * c) 100%
				 * d) 50%
				 * 
				 */
				"reader0", "", this.finderService.findOne(this.getEntityId("finder0")), "ES", this.genreService.findOne(this.getEntityId("genre1")), 1, 10000000, books, null
			}, {
				/**
				 * a) 11: writers have finders
				 * b) Positive
				 * c) 100%
				 * d) 50%
				 * 
				 */
				"reader1", "", this.finderService.findOne(this.getEntityId("finder0")), "ES", this.genreService.findOne(this.getEntityId("genre1")), 1, 10000000, books, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.saveFinderTemplate((String) testingData[i][0], (String) testingData[i][1], (Finder) testingData[i][2], (String) testingData[i][3], (Genre) testingData[i][4], (Integer) testingData[i][5], (Integer) testingData[i][6],
				(Collection<Book>) testingData[i][7], (Class<?>) testingData[i][8]);
	}
	protected void saveFinderTemplate(final String beanName, final String keyword, Finder finder, final String lang, final Genre genre, final Integer minNumWords, final Integer maxNumWords, final Collection<Book> resultsExpected, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			super.authenticate(beanName);
			finder.setKeyWord(keyword);
			finder.setLang(lang);
			finder.setGenre(genre);
			finder.setMinNumWords(minNumWords);
			finder.setMaxNumWords(maxNumWords);

			finder = this.finderService.save(finder);
			this.finderService.flush();

			Assert.isTrue(resultsExpected.containsAll(finder.getBooks()) && resultsExpected.size() == finder.getBooks().size());
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
	/**
	 * This test reefer to use case 11
	 * Let's test the cleanliness of the finders
	 * One positive
	 * One negative
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void clearFinderDriver() throws ParseException {

		final Collection<Book> books = new ArrayList<Book>();

		books.addAll(this.bookService.getAllVisibleBooks());

		final Object testingData[][] = {

			//Correct
			{
				/**
				 * a) 11: writers have finders
				 * b) Positive
				 * c) 100%
				 * d)
				 * 
				 */
				"reader0", this.finderService.findOne(this.getEntityId("finder0")), books, null
			},

			//Incorrect user
			{
				/**
				 * a) 11: writers have finders
				 * b) Must be a writer(null)
				 * c) 43.47%
				 * d)
				 * 
				 */
				null, this.finderService.findOne(this.getEntityId("finder0")), books, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.clearFinderTemplate((String) testingData[i][0], (Finder) testingData[i][1], (Collection<Book>) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	protected void clearFinderTemplate(final String beanName, Finder finder, final Collection<Book> resultsExpected, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			super.authenticate(beanName);

			finder = this.finderService.clear(finder);
			this.finderService.flush();

			Assert.isTrue(resultsExpected.containsAll(finder.getBooks()) && resultsExpected.size() == finder.getBooks().size());
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
