
package services;

import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utiles.IntermediaryBetweenTransactions;
import utilities.AbstractTest;
import domain.Book;
import domain.Writer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class BookServiceTest extends AbstractTest {

	@Autowired
	BookService								bookService;

	@Autowired
	GenreService							genreService;

	@Autowired
	PublisherService						publisherService;

	@Autowired
	WriterService							writerService;

	@Autowired
	private IntermediaryBetweenTransactions	intermediaryBetweenTransactions;


	/**
	 * Test for Case use: Un escritor debe ser capaz de gestionar sus libros, es decir, puede ser capaz de listar sus libros
	 * 1 positive
	 * 2 negative
	 */
	@Test
	public void listBooksDriver() {
		final Object testingData[][] = {
			{
				/**
				 * a) #1 | Un escritor puede listar sus libros (el escritor de este test tiene 2 libros)
				 * b) Positivo
				 * c) Sequence coverage: 100%
				 * d) Data coverage: -
				 * 
				 */
				"writer0", 2, null
			}, {
				/**
				 * a) #2 | Un escritor puede listar sus libros (el escritor de este test tiene 1 libros)
				 * b) Negativo (tiene un libro pero en el test se especifica que tiene 1)
				 * c) Sequence coverage: 100%
				 * d) Data coverage: -
				 * 
				 */
				"writer1", 0, IllegalArgumentException.class
			}, {
				/**
				 * a) #3 | Un escritor puede listar sus libros
				 * b) Negativo (un lector no puede listar los libros de un escritor)
				 * c) Sequence coverage: 100%
				 * d) Data coverage: -
				 * 
				 */
				"reader0", 2, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listBooksTemplate((String) testingData[i][0], (Integer) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void listBooksTemplate(final String user, final Integer numOfResultsExpected, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			super.authenticate(user);

			final Collection<Book> books = this.bookService.getAllBooksOfLoggedWriter();
			Assert.isTrue(books.size() == numOfResultsExpected);

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	/**
	 * Test for Case use: Un escritor debe ser capaz de gestionar sus libros, es decir, puede ser capaz de crear libros
	 * 
	 * 
	 */
	@Test
	public void createBooksDriver() {
		final Object testingData[][] = {
			{
				/**
				 * a) #4 | Un escritor puede crear libros
				 * b) Positivo
				 * c) Sequence coverage: 66%
				 * d) Data coverage: 100%
				 * 
				 */
				"writer0", "Titulo válido", "Descripción válida", "ES", "http://www.urlvalida.com", "PENDING", true, false, 0.0, 0, "genre0", "publisher0", null
			}, {
				/**
				 * a) #5 | Un escritor puede crear libros
				 * b) Positivo
				 * c) Sequence coverage: 66%
				 * d) Data coverage: 100%
				 * 
				 */
				"publisher0", "Titulo válido", "Descripción válida", "ES", null, "PENDING", true, false, 0.0, 0, "genre0", "publisher0", IllegalArgumentException.class
			}, {
				/**
				 * a) #6 | Un escritor puede crear libros
				 * b) Negativo (Una editorial no puede crear libros)
				 * c) Sequence coverage: 66%
				 * d) Data coverage: 100%
				 * 
				 */
				"publisher0", "Titulo válido", "Descripción válida", "ES", "http://www.urlvalida.com", "PENDING", true, false, 0.0, 0, "genre0", "publisher0", IllegalArgumentException.class
			}, {
				/**
				 * a) #7 | Un escritor puede crear libros
				 * b) Negativo (Hay datos inválidos)
				 * c) Sequence coverage: 66%
				 * d) Data coverage: 90%
				 * 
				 */
				"writer0", "", "Descripción válida", "ES", "http://www.urlvalida.com", "PENDING", true, false, 0.0, 0, "genre0", "publisher0", ConstraintViolationException.class
			}, {
				/**
				 * a) #8 | Un escritor puede crear libros
				 * b) Negativo (Hay datos inválidos)
				 * c) Sequence coverage: 66%
				 * d) Data coverage: 90%
				 * 
				 */
				"writer0", "Titulo válido", "", "ES", "http://www.urlvalida.com", "PENDING", true, false, 0.0, 0, "genre0", "publisher0", ConstraintViolationException.class
			}, {
				/**
				 * a) #9 | Un escritor puede crear libros
				 * b) Negativo (Hay datos inválidos)
				 * c) Sequence coverage: 66%
				 * d) Data coverage: 95%
				 * 
				 */
				"writer0", "Titulo válido", "Descripción válida", "INVALIDO", "http://www.urlvalida.com", "PENDING", true, false, 0.0, 0, "genre0", "publisher0", ConstraintViolationException.class
			}, {
				/**
				 * a) #10 | Un escritor puede crear libros
				 * b) Negativo (Hay datos inválidos)
				 * c) Sequence coverage: 66%
				 * d) Data coverage: 95%
				 * 
				 */
				"writer0", "Titulo válido", "Descripción válida", "ES", "ESTO NO ES UNA URL", "PENDING", true, false, 0.0, 0, "genre0", "publisher0", ConstraintViolationException.class

			}, {
				/**
				 * a) #11 | Un escritor puede crear libros
				 * b) Negativo (El libro debe tener un género)
				 * c) Sequence coverage: 66%
				 * d) Data coverage: 95%
				 * 
				 */
				"writer0", "Titulo válido", "Descripción válida", "ES", "http://www.urlvalida.com", "PENDING", true, false, 0.0, 0, null, "publisher0", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createBooksTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Boolean) testingData[i][6],
				(Boolean) testingData[i][7], (Double) testingData[i][8], (Integer) testingData[i][9], (String) testingData[i][10], (String) testingData[i][11], (Class<?>) testingData[i][12]);
	}

	protected void createBooksTemplate(final String user, final String title, final String desc, final String idioma, final String url, final String status, final Boolean draft, final Boolean cancelled, final Double score, final Integer numWords,
		final String genre, final String publisher, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			super.authenticate(user);

			final Book book = this.bookService.create();

			final int idWriter = this.getEntityId(user);
			final Writer writer = this.writerService.findOne(idWriter);
			book.setWriter(writer);
			book.setTitle(title);
			book.setDescription(desc);
			book.setLang(idioma);
			book.setCover(url);
			if (genre != null)
				book.setGenre(this.genreService.findOne(this.getEntityId(genre)));
			if (publisher != null)
				book.setPublisher(this.publisherService.findOne(this.getEntityId(publisher)));
			book.setCancelled(cancelled);
			book.setStatus(status);
			book.setDraft(draft);
			book.setScore(score);
			book.setNumWords(numWords);
			book.setTicker(this.intermediaryBetweenTransactions.generateTicker());

			this.bookService.save(book);
			this.bookService.flush();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			oops.printStackTrace();
		}

		this.checkExceptions(expected, caught);
	}

	/**
	 * Test for Case use: Un escritor debe ser capaz de gestionar sus libros, es decir, puede ser capaz de crear libros
	 * 
	 * 
	 */
	@Test
	public void editBooksDriver() {
		final Object testingData[][] = {
			{
				/**
				 * a) #12 | Un escritor puede editar un libro suyo
				 * b) Positivo
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"writer0", "Titulo válido", "Descripción válida", "ES", "http://www.urlvalida.com", "PENDING", 0.0, 0, "book1", null
			}, {
				/**
				 * a) #13 | Un escritor puede editar un libro suyo
				 * b) Negativo (Solo se pueden editar los libros que estén en modo borrador)
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"writer0", "Titulo válido", "Descripción válida", "ES", "http://www.urlvalida.com", "PENDING", 0.0, 0, "book0", IllegalArgumentException.class
			}, {
				/**
				 * a) #14 | Un escritor puede editar un libro suyo
				 * b) Negativo (Hay código HTML)
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 95%
				 * 
				 */
				"writer0", "<script></script>", "Descripción válida", "ES", "http://www.urlvalida.com", "PENDING", 0.0, 0, "book1", ConstraintViolationException.class
			}, {
				/**
				 * a) #15 | Un escritor puede editar un libro suyo
				 * b) Negativo (La puntuación no puede ser negativa)
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 95%
				 * 
				 */
				"writer0", "Titulo válido", "Descripción válida", "ES", "http://www.urlvalida.com", "PENDING", -1.0, 0, "book1", ConstraintViolationException.class
			}, {
				/**
				 * a) #16 | Un escritor puede editar un libro suyo
				 * b) Negativo (La puntuación no puede ser mayor que 10)
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 95%
				 * 
				 */
				"writer0", "Titulo válido", "Descripción válida", "ES", "http://www.urlvalida.com", "PENDING", 20.0, 0, "book1", ConstraintViolationException.class
			}, {
				/**
				 * a) #17 | Un escritor puede editar un libro suyo
				 * b) Negativo (El número de palabras no puede ser negativo)
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 95%
				 * 
				 */
				"writer0", "Titulo válido", "Descripción válida", "ES", "http://www.urlvalida.com", "PENDING", 0.0, -1, "book1", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editBooksTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Double) testingData[i][6],
				(Integer) testingData[i][7], (String) testingData[i][8], (Class<?>) testingData[i][9]);
	}

	protected void editBooksTemplate(final String user, final String title, final String desc, final String idioma, final String url, final String status, final Double score, final Integer numWords, final String book, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			super.authenticate(user);

			final Book bookDB = this.bookService.findOne(this.getEntityId(book));

			bookDB.setTitle(title);
			bookDB.setDescription(desc);
			bookDB.setLang(idioma);
			bookDB.setCover(url);
			bookDB.setScore(score);
			bookDB.setNumWords(numWords);

			this.bookService.save(bookDB);
			this.bookService.flush();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			oops.printStackTrace();
		}

		this.checkExceptions(expected, caught);
	}

	/**
	 * Test for Case use: Un escritor debe ser capaz de gestionar sus libros, es decir, puede ser capaz de borrar libros
	 * 
	 * 
	 */
	@Test
	public void deleteBooksDriver() {
		final Object testingData[][] = {
			{
				/**
				 * a) #18 | Un escitor puede borrar sus libros
				 * b) Positivo
				 * c) Sequence coverage: 100%
				 * d) Data coverage:
				 * 
				 */
				"writer0", "book1", null
			}, {
				/**
				 * a) #19 | Un escitor puede borrar sus libros
				 * b) Negativo (Solo se pueden borrar los libros que estén en modo borrador)
				 * c) Sequence coverage: 100%
				 * d) Data coverage:
				 * 
				 */
				"writer0", "book0", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteBooksTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void deleteBooksTemplate(final String user, final String book, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			super.authenticate(user);

			this.bookService.delete(this.getEntityId(book));
			this.bookService.flush();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			oops.printStackTrace();
		}

		this.checkExceptions(expected, caught);
	}

	/**
	 * Test for Case use: Un escritor debe ser capaz de gestionar sus libros, es decir, puede ser capaz de cancelar sus libros
	 * 
	 * 
	 */
	@Test
	public void cancelBooksDriver() {
		final Object testingData[][] = {
			{
				/**
				 * a) #20 | Un escritor puede cancelar sus libros
				 * b) Positivo
				 * c) Sequence coverage: 100%
				 * d) Data coverage:
				 * 
				 */
				"writer0", "book0", null
			}, {
				/**
				 * a) #21 | Un escritor puede cancelar sus libros
				 * b) Negativo (No se pueden cancelar libros que estén en modo borrador)
				 * c) Sequence coverage: 100%
				 * d) Data coverage:
				 * 
				 */
				"writer0", "book1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.cancelBooksTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void cancelBooksTemplate(final String user, final String book, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			super.authenticate(user);

			this.bookService.cancelBook(this.getEntityId(book));
			this.bookService.flush();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			oops.printStackTrace();
		}

		this.checkExceptions(expected, caught);
	}

	/**
	 * Test for Case use: Un escritor debe ser capaz de gestionar sus libros, es decir, puede ser capaz de poner sus libros en modo final
	 * 
	 * 
	 */
	@Test
	public void changeDraftStatusBooksDriver() {
		final Object testingData[][] = {
			{
				/**
				 * a) #22 | Un escritor puede poner sus libros en modo final
				 * b) Positivo
				 * c) Sequence coverage: 100%
				 * d) Data coverage:
				 * 
				 */
				"writer0", "book1", null
			}, {
				/**
				 * a) #23 | Un escritor puede poner sus libros en modo final
				 * b) Negativo (No se pueden poner en modo final libros de otros escritores)
				 * c) Sequence coverage: 100%
				 * d) Data coverage:
				 * 
				 */
				"writer0", "book2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.changeDraftBooksTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void changeDraftBooksTemplate(final String user, final String book, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			super.authenticate(user);

			this.bookService.changeDraft(this.getEntityId(book));
			this.bookService.flush();

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			oops.printStackTrace();
		}

		this.checkExceptions(expected, caught);
	}

	/**
	 * a) #24 Test for Case use: Un administrador puede lanzar un proceso para calcular las puntuaciones
	 * b)
	 * c) Sequence coverage: 100%
	 * d) Data coverage:
	 */
	@Test
	public void calculateScores() {
		super.authenticate("admin");

		this.bookService.computeScore();
		final Book book = this.bookService.findOne(this.getEntityId("book0"));
		this.bookService.flush();
		Assert.isTrue(book.getScore() != null);

		super.unauthenticate();
	}
}
