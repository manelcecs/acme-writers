
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Genre;
import domain.Writer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class DashboardServiceTest extends AbstractTest {

	@Autowired
	private AdministratorService	administratorService;
	@Autowired
	private WriterService			writerService;
	@Autowired
	private GenreService			genreService;


	@Override
	@Before
	public void setUp() {
		this.unauthenticate();
	}

	/**
	 * This test reefer to use case 25.h
	 * here we're going to test the dashboard metrics related to books for the administrator
	 * One positive
	 * Two negatives
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void dashboardBookDriver() {

		final Collection<Writer> writersWithMoreBooks = new ArrayList<Writer>();
		writersWithMoreBooks.add(this.writerService.findOne(this.getEntityId("writer0")));
		writersWithMoreBooks.add(this.writerService.findOne(this.getEntityId("writer1")));

		final Collection<Writer> writersWithLessBooks = new ArrayList<Writer>();
		writersWithLessBooks.add(this.writerService.findOne(this.getEntityId("writer2")));

		final Collection<Genre> genres = new ArrayList<Genre>();

		genres.add(this.genreService.findOne(this.getEntityId("genre1")));
		genres.add(this.genreService.findOne(this.getEntityId("genre2")));

		final Object testingData[][] = {
			{
				/**
				 * a) 25.h: show metrics as an Administrator
				 * b) Positive
				 * c) 100%
				 * d)
				 * 
				 */
				"admin", 0.6667, 0, 1, 0.4714, 1.0, 2.0, 1, 3, 1.0, genres, writersWithMoreBooks, writersWithLessBooks, null
			}, {
				/**
				 * a) 25.h: show metrics as an Administrator
				 * b) Must be an administrator(writer)
				 * c) 50%
				 * d)
				 * 
				 */
				"writer0", 0.6667, 0, 1, 0.4714, 1.0, 2.0, 1, 3, 1.0, genres, writersWithMoreBooks, writersWithLessBooks, IllegalArgumentException.class
			}, {
				/**
				 * a) 25.h: show metrics as an Administrator
				 * b) Must be an administrator(publisher)
				 * c) 50%
				 * d)
				 * 
				 */
				"publisher0", 0.6667, 0, 1, 0.4714, 1.0, 2.0, 1, 3, 1.0, genres, writersWithMoreBooks, writersWithLessBooks, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.dashboardBookTemplate((String) testingData[i][0], (Double) testingData[i][1], (Integer) testingData[i][2], (Integer) testingData[i][3], (Double) testingData[i][4], (Double) testingData[i][5], (Double) testingData[i][6],
				(Integer) testingData[i][7], (Integer) testingData[i][8], (Double) testingData[i][9], (Collection<Genre>) testingData[i][10], (Collection<Writer>) testingData[i][11], (Collection<Writer>) testingData[i][12], (Class<?>) testingData[i][13]);
	}
	protected void dashboardBookTemplate(final String beanName, final Double avgOfBooksPerWriterExpected, final Integer minOfBooksPerWriterExpected, final Integer maxOfBooksPerWriterExpected, final Double sDOfBooksPerWriterExpected,
		final Double ratioBooksWithPublisherVsWithoutPublisherExpected, final Double avgOfChaptersPerBookExpected, final Integer minOfChaptersPerBookExpected, final Integer maxOfChaptersPerBookExpected, final Double sDOfChaptersPerBookExpected,
		final Collection<Genre> genresExpected, final Collection<Writer> writersWithMoreBooksExpected, final Collection<Writer> writersWithLessBooksExpected, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			super.authenticate(beanName);

			final Double avgOfBooksPerWriter = this.administratorService.getAvgOfBooksPerWriter();
			final Integer maxOfBooksPerWriter = this.administratorService.getMaximumOfBooksPerWriter();
			final Integer minOfBooksPerWriter = this.administratorService.getMinimumOfBooksPerWriter();
			final Double sDOfBooksPerWriter = this.administratorService.getSDOfBooksPerWriter();

			final Double avgOfChaptersPerBook = this.administratorService.getAvgOfChaptersPerBook();
			final Integer maxOfChaptersPerBook = this.administratorService.getMaximumOfChaptersPerBook();
			final Integer minOfChaptersPerBook = this.administratorService.getMinimumOfChaptersPerBook();
			final Double sDOfChaptersPerBook = this.administratorService.getSDOfChaptersPerBook();

			final Double ratioBooksWithPublisherVsWithoutPublisher = this.administratorService.getRatioOfBooksWithPublisherVsBooksIndependients();
			final List<Object[]> histogram = this.administratorService.getHistogramData();
			final Collection<Writer> writersWithMoreBooks = this.writerService.getWritersWithMoreBooks();
			final Collection<Writer> writersWithLessBooks = this.writerService.getWritersWithLessBooks();

			Assert.isTrue(writersWithMoreBooksExpected.containsAll(writersWithMoreBooks) && writersWithMoreBooksExpected.size() == writersWithMoreBooks.size());
			Assert.isTrue(writersWithLessBooksExpected.containsAll(writersWithLessBooks) && writersWithLessBooksExpected.size() == writersWithLessBooks.size());
			Assert.isTrue(genresExpected.contains(histogram.get(0)[1]) && genresExpected.contains(histogram.get(1)[1]) && genresExpected.size() == histogram.size());

			Assert.isTrue(avgOfBooksPerWriter.equals(avgOfBooksPerWriterExpected) && maxOfBooksPerWriter.equals(maxOfBooksPerWriterExpected) && minOfBooksPerWriter.equals(minOfBooksPerWriterExpected)
				&& sDOfBooksPerWriter.equals(sDOfBooksPerWriterExpected) && ratioBooksWithPublisherVsWithoutPublisher.equals(ratioBooksWithPublisherVsWithoutPublisherExpected));

			Assert.isTrue(avgOfChaptersPerBook.equals(avgOfChaptersPerBookExpected) && maxOfChaptersPerBook.equals(maxOfChaptersPerBookExpected) && minOfChaptersPerBook.equals(minOfChaptersPerBookExpected)
				&& sDOfChaptersPerBook.equals(sDOfChaptersPerBookExpected));
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
