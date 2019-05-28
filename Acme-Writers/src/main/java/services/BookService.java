
package services;

import java.text.ParseException;
import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.BookRepository;
import security.LoginService;
import utiles.AuthorityMethods;
import utiles.IntermediaryBetweenTransactions;
import domain.Book;
import domain.Chapter;
import domain.Publisher;
import domain.Reader;
import domain.Ticker;
import domain.Writer;
import forms.BookForm;

@Service
@Transactional
public class BookService {

	@Autowired
	private BookRepository					bookRepository;

	@Autowired
	private MessageService					messageService;

	@Autowired
	private OpinionService					opinionService;

	@Autowired
	private WriterService					writerService;

	@Autowired
	private ChapterService					chapterService;

	@Autowired
	private PublisherService				publisherService;

	@Autowired
	private ReaderService					readerService;

	@Autowired
	private Validator						validator;

	@Autowired
	private IntermediaryBetweenTransactions	intermediaryBetweenTransactions;


	public Book findOne(final int idBook) {
		return this.bookRepository.findOne(idBook);
	}

	public Collection<Book> getFilterBooksByKeyword(final String keyword) {
		return this.bookRepository.getFilterBooksByKeyword(keyword);
	}

	public Collection<Book> getFilterBooksByFinder(final String keyWord, final Integer minNumWords, final Integer maxNumWords, final String language, final int minGenre, final int maxGenre) {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("READER"));
		return this.bookRepository.getFilterBooksByFinder(keyWord, minNumWords, maxNumWords, language, minGenre, maxGenre);
	}

	public Collection<Book> getBooksByFinder(final int idFinder) {
		return this.bookRepository.getBooksByFinder(idFinder);
	}

	public Collection<Book> getBooksByGenre(final int idGenre) {
		return this.bookRepository.getBooksByGenre(idGenre);
	}

	public Book updateGenre(final Book book) {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.bookRepository.save(book);
	}

	public void checkFinalAndNotCancelled(final Book book) {
		Assert.isTrue(!book.getDraft() && !book.getCancelled());
	}

	public Book reconstruct(final BookForm bookForm, final BindingResult bindingResult) {
		Book book = new Book();

		if (bookForm.getId() != 0)
			book = this.findOne(bookForm.getId());
		else {
			book.setCancelled(false);
			book.setDraft(true);
			book.setScore(null);
			book.setTicker(this.intermediaryBetweenTransactions.generateTicker());
			book.setNumWords(0);
			final Writer writerLogged = this.writerService.findByPrincipal(LoginService.getPrincipal().getId());
			book.setWriter(writerLogged);
		}
		book.setTitle(bookForm.getTitle());
		book.setDescription(bookForm.getDescription());
		book.setLanguage(bookForm.getLanguage());
		book.setCover(bookForm.getCover());
		book.setGenre(bookForm.getGenre());
		book.setPublisher(bookForm.getPublisher());
		if (bookForm.getPublisher() == null)
			book.setStatus("INDEPENDENT");
		else
			book.setStatus("PENDING");

		this.validator.validate(book, bindingResult);

		if (bindingResult.hasErrors())
			throw new ValidationException();

		return book;

	}
	//CRUD
	public Book create() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("WRITER"));
		return new Book();
	}

	//==================================================
	//		NUEVO METODO SAVE
	//==================================================

	public Book save(final BookForm bookForm, final BindingResult bindingResult) {
		final Book book = this.reconstruct(bookForm, bindingResult);
		return this.save(book);
	}

	//FIXME: CONTROLAR QUE EL IDIOMA ESTE EN LA LISTA DE IDIOMAS
	public Book save(final Book book) {

		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("WRITER"));

		final Writer writerLogged = this.writerService.findByPrincipal(LoginService.getPrincipal().getId());

		Assert.isTrue(book.getWriter().equals(writerLogged));

		if (book.getId() != 0) {
			final Book bookDB = this.bookRepository.findOne(book.getId());
			Assert.isTrue(bookDB.getDraft());
		}

		return this.bookRepository.save(book);
	}

	public void flush() {
		this.bookRepository.flush();
	}

	//==================================================

	public void delete(final int idBook) {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("WRITER"));

		final Book book = this.findOne(idBook);

		final Writer writerLogged = this.writerService.findByPrincipal(LoginService.getPrincipal().getId());

		Assert.isTrue(book.getWriter().equals(writerLogged));

		Assert.isTrue(book.getDraft());

		final Collection<Chapter> chapters = this.chapterService.getChaptersOfABook(book.getId());

		this.chapterService.deleteChapters(chapters);
		this.bookRepository.delete(book);
	}

	public Collection<Book> getAllBooksOfLoggedWriter() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("WRITER"));

		final Writer writerLogged = this.writerService.findByPrincipal(LoginService.getPrincipal().getId());

		return this.bookRepository.getAllBooksOfAWriter(writerLogged.getId());

	}

	public Collection<Book> getBooksCanChangeDraft() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("WRITER"));

		final Writer writerLogged = this.writerService.findByPrincipal(LoginService.getPrincipal().getId());

		return this.bookRepository.getBooksCanChangeDraft(writerLogged.getId());

	}

	public Collection<Book> getAllVisibleBooks() {
		return this.bookRepository.getAllVisibleBooks();
	}

	public Collection<Book> getAllVisibleBooksOfWriter(final int idWriter) {
		return this.bookRepository.getAllVisibleBooksOfWriter(idWriter);
	}

	public Collection<Book> getAllVisibleBooksOfPublisher(final int idPublisher) {
		return this.bookRepository.getAllVisibleBooksOfPublisher(idPublisher);
	}

	public Collection<Book> getBooksOfLoggedPublisher() {
		final Publisher publisher = this.publisherService.findByPrincipal(LoginService.getPrincipal());
		return this.bookRepository.getBooksOfPublisher(publisher.getId());
	}

	public Book changeStatus(final int idBook, final String status) throws ParseException {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("PUBLISHER"));

		final Book book = this.findOne(idBook);

		final Publisher publisherLogged = this.publisherService.findByPrincipal(LoginService.getPrincipal());

		Assert.isTrue(book.getPublisher().equals(publisherLogged));

		Assert.isTrue(!book.getDraft() && book.getStatus().equals("PENDING"));

		Assert.isTrue(status.equals("ACCEPTED") || status.equals("REJECTED"));

		book.setStatus(status);

		final Book bookSaved = this.bookRepository.save(book);

		this.messageService.notifyStatusBook(bookSaved);

		return bookSaved;
	}

	public Book copyBook(final int idBook) {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("WRITER"));

		final Book book = this.findOne(idBook);

		final Writer writerLogged = this.writerService.findByPrincipal(LoginService.getPrincipal().getId());

		Assert.isTrue(book.getWriter().equals(writerLogged));

		Assert.isTrue(!book.getDraft() && book.getStatus().equals("REJECTED"));

		final Book bookClone = new Book();
		bookClone.setDraft(true);
		bookClone.setPublisher(null);
		bookClone.setStatus("INDEPENDENT");
		final Ticker ticker = this.intermediaryBetweenTransactions.generateTicker();
		bookClone.setTicker(ticker);
		bookClone.setCancelled(book.getCancelled());
		bookClone.setCover(book.getCover());
		bookClone.setDescription(book.getDescription());
		bookClone.setGenre(book.getGenre());
		bookClone.setLanguage(book.getLanguage());
		bookClone.setNumWords(0);
		bookClone.setScore(0.0);
		bookClone.setTitle(book.getTitle());
		bookClone.setWriter(book.getWriter());

		final Book bookSaved = this.bookRepository.save(bookClone);

		final Collection<Chapter> chapters = this.chapterService.getChaptersOfABook(idBook);
		for (final Chapter chapter : chapters) {
			final Chapter chapterClone = new Chapter();
			chapterClone.setBook(bookSaved);
			chapterClone.setNumber(chapter.getNumber());
			chapterClone.setText(chapter.getText());
			chapterClone.setTitle(chapter.getTitle());

			this.chapterService.save(chapterClone);
		}

		return bookSaved;

	}
	public Book changeDraft(final int idBook) {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("WRITER"));

		final Book book = this.findOne(idBook);

		final Writer writerLogged = this.writerService.findByPrincipal(LoginService.getPrincipal().getId());

		Assert.isTrue(book.getWriter().equals(writerLogged));

		Assert.isTrue(book.getDraft());

		final Collection<Chapter> chapters = this.chapterService.getChaptersOfABook(idBook);
		Assert.isTrue(chapters.size() > 0);

		book.setDraft(false);

		return this.bookRepository.save(book);

	}

	public Book cancelBook(final int idBook) {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("WRITER"));

		final Book book = this.findOne(idBook);

		final Writer writerLogged = this.writerService.findByPrincipal(LoginService.getPrincipal().getId());

		Assert.isTrue(book.getWriter().equals(writerLogged));

		Assert.isTrue(!book.getDraft() && (book.getStatus().equals("ACCEPTED") || book.getStatus().equals("INDEPENDENT")));

		book.setCancelled(!book.getCancelled());

		return this.bookRepository.save(book);

	}

	public void addBookToFavouriteList(final int idBook) {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("READER"));

		final Book book = this.findOne(idBook);

		final Reader readerLogged = this.readerService.findByPrincipal(LoginService.getPrincipal());
		final Collection<Book> books = readerLogged.getBooks();

		Assert.isTrue(!books.contains(book));

		Assert.isTrue(!book.getDraft() && !book.getCancelled() && (book.getStatus().equals("INDEPENDENT") || book.getStatus().equals("ACCEPTED")));

		books.add(book);

		readerLogged.setBooks(books);

		this.readerService.save(readerLogged);

	}

	public void deleteBookFromFavouriteList(final int idBook) {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("READER"));

		final Book book = this.findOne(idBook);

		final Reader readerLogged = this.readerService.findByPrincipal(LoginService.getPrincipal());
		final Collection<Book> books = readerLogged.getBooks();

		Assert.isTrue(books.contains(book));

		books.remove(book);

		readerLogged.setBooks(books);

		this.readerService.save(readerLogged);

	}

	public void computeScore() {
		final Collection<Book> books = this.bookRepository.findAll();
		final Double numReaders = 1.0 * this.readerService.findAll().size();
		Double score = null;

		for (final Book book : books) {
			final Double numOpinions = 1.0 * this.opinionService.getNumOpinionsOfBook(book.getId());
			final Double numLike = 1.0 * this.opinionService.getNumLikesOfBook(book.getId());
			final Double numFav = 1.0 * this.bookRepository.getNumFavOfBook(book.getId());

			if (numOpinions != 0) {
				if (numReaders != 0) {
					score = 10.0 * (numLike / numOpinions) + (numFav / numReaders);
					if (score > 10.0)
						score = 10.0;
				} else
					score = 10.0 * (numLike / numOpinions);

			} else
				score = null;

			book.setScore(score);

			this.bookRepository.save(book);
		}

	}

	public Collection<Book> getBooksCanParticipate(final int idContest) {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("WRITER"));
		final Writer writerLogged = this.writerService.findByPrincipal(LoginService.getPrincipal().getId());
		return this.bookRepository.getBooksCanParticipate(writerLogged.getId(), idContest);
	}

	public Collection<Book> getBooksOrderedByScore() {
		return this.bookRepository.getBooksOrderedByScore();
	}

}
