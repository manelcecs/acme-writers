package services;

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
import domain.Writer;
import forms.BookForm;

@Service
@Transactional
public class BookService {

	@Autowired
	BookRepository							bookRepository;

	@Autowired
	WriterService							writerService;

	@Autowired
	ChapterService							chapterService;

	@Autowired
	PublisherService						publisherService;

	@Autowired
	Validator								validator;

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

	public Collection<Book> getBooksOfLoggedPublisher() {
		final Publisher publisher = this.publisherService.findByPrincipal(LoginService.getPrincipal().getId());
		return this.bookRepository.getBooksOfPublisher(publisher.getId());
	}

	public Book changeStatus(final int idBook, final String status) {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("PUBLISHER"));

		final Book book = this.findOne(idBook);

		final Publisher publisherLogged = this.publisherService.findByPrincipal(LoginService.getPrincipal().getId());

		Assert.isTrue(book.getPublisher().equals(publisherLogged));

		Assert.isTrue(!book.getDraft() && book.getStatus().equals("PENDING"));

		Assert.isTrue(status.equals("ACCEPTED") || status.equals("REJECTED"));

		book.setStatus(status);

		return this.bookRepository.save(book);

	}

	public Book copyBook(final int idBook) {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("WRITER"));

		final Book book = this.findOne(idBook);

		final Writer writerLogged = this.writerService.findByPrincipal(LoginService.getPrincipal().getId());

		Assert.isTrue(book.getWriter().equals(writerLogged));

		Assert.isTrue(!book.getDraft() && book.getStatus().equals("REJECTED"));

		book.setId(0);
		book.setDraft(true);
		book.setPublisher(null);
		book.setStatus(null);
		book.setTicker(this.intermediaryBetweenTransactions.generateTicker());

		final Book bookSaved = this.bookRepository.save(book);

		final Collection<Chapter> chapters = this.chapterService.getChaptersOfABook(idBook);
		for (final Chapter chapter : chapters) {
			chapter.setId(0);
			chapter.setBook(bookSaved);
			this.chapterService.save(chapter);
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

		Assert.isTrue(!book.getDraft() && book.getStatus().equals("ACCEPTED"));

		book.setCancelled(!book.getCancelled());

		return this.bookRepository.save(book);

	}

}