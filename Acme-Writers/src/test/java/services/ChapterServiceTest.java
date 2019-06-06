
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Book;
import domain.Chapter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ChapterServiceTest extends AbstractTest {

	@Autowired
	ChapterService	chapterService;

	@Autowired
	BookService		bookService;


	/**
	 * Test for Case use: Un escritor debe ser capaz de gestionar sus capítulos, es decir, puede ser capaz de crear capítulos a sus libros
	 * 
	 * 
	 */
	@Test
	public void createChapterDriver() {
		final Object testingData[][] = {
			{
				/**
				 * a) #1 | Un escritor puede crear capítulos
				 * b) Positivo
				 * c) Sequence coverage: 73%
				 * d) Data coverage: 100%
				 * 
				 */
				"writer0", "book1", "Titulo de prueba", 4, "Texto de prueba", null
			}, {
				/**
				 * a) #2 | Un escritor puede crear capítulos
				 * b) Negativo (No se pueden añadir capítulos a libros en modo final)
				 * c) Sequence coverage: 73%
				 * d) Data coverage: 100%
				 * 
				 */
				"writer0", "book0", "Titulo de prueba", 4, "Texto de prueba", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createChapterTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	protected void createChapterTemplate(final String user, final String book, final String title, final Integer number, final String text, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			super.authenticate(user);
			final Book bookDB = this.bookService.findOne(this.getEntityId(book));
			final Chapter chapter = this.chapterService.create(bookDB);
			chapter.setText(text);
			chapter.setTitle(title);
			chapter.setNumber(number);

			this.chapterService.save(chapter);
			this.chapterService.flush();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			oops.printStackTrace();
		}

		this.checkExceptions(expected, caught);
	}

	/**
	 * Test for Case use: Un escritor debe ser capaz de gestionar sus libros, es decir, puede ser capaz de editar sus capítulos
	 * 
	 * 
	 */
	@Test
	public void editChapterDriver() {
		final Object testingData[][] = {
			{
				/**
				 * a) #3 | Un escritor puede editar sus capítulos
				 * b) Positivo
				 * c) Sequence coverage: 93%
				 * d) Data coverage: 100%
				 * 
				 */
				"writer0", "chapter3", "Titulo editado", 6, "Texto editado", null
			}, {
				/**
				 * a) #4 | Un escritor puede editar sus capítulos
				 * b) Negativo (Un escritor no puede editar los capítulos de libros que no son suyos)
				 * c) Sequence coverage: 93%
				 * d) Data coverage: 100%
				 * 
				 */
				"writer1", "chapter3", "Titulo editado", 6, "Texto editado", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editChapterTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	protected void editChapterTemplate(final String user, final String chapter, final String title, final Integer number, final String text, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			super.authenticate(user);
			final Chapter chapterDB = this.chapterService.findOne(this.getEntityId(chapter));
			chapterDB.setText(text);
			chapterDB.setTitle(title);
			chapterDB.setNumber(number);

			this.chapterService.save(chapterDB);
			this.chapterService.flush();
			;

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			oops.printStackTrace();
		}

		this.checkExceptions(expected, caught);
	}

	/**
	 * Test for Case use: Un escritor debe ser capaz de gestionar sus libros, es decir, puede ser capaz de borrar sus capítulos
	 * 
	 * 
	 */
	@Test
	public void deleteChapterDriver() {
		final Object testingData[][] = {
			{
				/**
				 * a) #5 | Un escritor puede borrar sus datos
				 * b) Positivo
				 * c) Sequence coverage: 93%
				 * d) Data coverage:
				 * 
				 */
				"writer0", "chapter3", null
			}, {
				/**
				 * a) #6 | Un escritor puede borrar
				 * b) Negativo (No se pueden borrar capítulos de libros que estén en modo final)
				 * c) Sequence coverage: 93%
				 * d) Data coverage:
				 * 
				 */
				"writer0", "chapter1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteChapterTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void deleteChapterTemplate(final String user, final String chapter, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			super.authenticate(user);

			this.chapterService.delete(this.getEntityId(chapter));
			this.chapterService.flush();
			;

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			oops.printStackTrace();
		}

		this.checkExceptions(expected, caught);
	}

}
