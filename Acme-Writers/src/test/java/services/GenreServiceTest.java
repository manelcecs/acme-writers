
package services;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Genre;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class GenreServiceTest extends AbstractTest {

	@Autowired
	GenreService	genreService;


	/*
	 * This test reefer to requeriment 25b
	 * An admin must be able to:
	 * Manage his or her genres, which includes listing,
	 * CREATING, updating and deleting them.
	 * 1 positive
	 * 2 negative
	 */
	@Test
	public void creatingDriver() {
		final Object testingData[][] = {
			{
				/*
				 * a)Admin create a valid genre
				 * b)Positive
				 * c)Sequence coverage: 100%
				 * d)Data coverage: 100%
				 */
				"admin", "nombreEN", "nombreES", null
			}, {
				/*
				 * a)Admin create a invalid genre (nameEN blank)
				 * b)Negative
				 * c)Sequence coverage: 100%
				 * d)Data coverage: 100%
				 */
				"admin", "", "nombreES", ConstraintViolationException.class
			}, {
				/*
				 * a)Admin create a invalid genre (nameES blank)
				 * b)Negative
				 * c)Sequence coverage: 100%
				 * d)Data coverage: 100%
				 */
				"admin", "nombreEN", "", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.creatingTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	// Ancillary methods ------------------------------------------------------
	protected void creatingTemplate(final String user, final String nameEN, final String nameES, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(user);

			Genre genre = this.genreService.create();

			genre = this.fillData(genre, nameEN, nameES, "genre0");

			this.genreService.save(genre);
			this.genreService.flush();

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			oops.printStackTrace();
		}

		this.checkExceptions(expected, caught);
	}
	/*
	 * This test reefer to requeriment 25b
	 * An admin must be able to:
	 * Manage his or her genres, which includes listing,
	 * creating, UPDATING and deleting them.
	 * 1 positive
	 * 2 negative
	 */
	@Test
	public void updatingDriver() {
		final Object testingData[][] = {
			{
				/*
				 * a)Admin edit a valid genre
				 * b)Positive
				 * c)Sequence coverage: 100%
				 * d)Data coverage: 100%
				 */
				"admin", "nombreEN", "nombreES", null
			}, {
				/*
				 * a)Admin edit a invalid genre (nameEN blank)
				 * b)Negative
				 * c)Sequence coverage: 100%
				 * d)Data coverage: 100%
				 */
				"admin", "", "nombreES", ConstraintViolationException.class
			}, {
				/*
				 * a)Admin edit a invalid genre (nameES blank)
				 * b)Negative
				 * c)Sequence coverage: 100%
				 * d)Data coverage: 100%
				 */
				"admin", "nombreEN", "", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.updatingTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	// Ancillary methods ------------------------------------------------------
	protected void updatingTemplate(final String user, final String nameEN, final String nameES, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(user);

			Genre genre = this.genreService.findOne(this.getEntityId("genre2"));

			genre = this.fillData(genre, nameEN, nameES, "genre0");

			this.genreService.save(genre);
			this.genreService.flush();

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			oops.printStackTrace();
		}

		this.checkExceptions(expected, caught);
	}

	/*
	 * This test reefer to requeriment 25.b
	 * An admin must be able to:
	 * Manage his or her genres, which includes listing,
	 * creating, updating and DELETING them.
	 * 1 positive
	 * 1 negative
	 */
	@Test
	public void deletingDriver() {
		final Object testingData[][] = {
			{
				/*
				 * a)A sponsor delete his or her sponsorship
				 * b)Positive
				 * c)Sequence coverage: 100%
				 * d)Data coverage: -
				 */
				"admin", "genre1", null
			}, {
				/*
				 * a) One sponsor delete the sponsorship of another sponsor
				 * b) Negative
				 * c) Sequence coverage: 17%
				 * d) Data coverage: -
				 */
				"admin", "genre0", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deletingTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	// Ancillary methods ------------------------------------------------------
	protected void deletingTemplate(final String user, final String idSponsorship, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(user);

			final Genre genre = this.genreService.findOne(this.getEntityId(idSponsorship));

			this.genreService.delete(genre);

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			oops.printStackTrace();
		}

		this.checkExceptions(expected, caught);
	}

	private Genre fillData(final Genre genre, final String nameEN, final String nameES, final String parentName) {

		genre.setNameEN(nameEN);
		genre.setNameES(nameES);
		genre.setParent(this.genreService.findOne(this.getEntityId(parentName)));

		return genre;
	}
}
