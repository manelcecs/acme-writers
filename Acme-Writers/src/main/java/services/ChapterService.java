
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ChapterRepository;
import security.LoginService;
import utiles.AuthorityMethods;
import domain.Book;
import domain.Chapter;
import domain.Writer;

@Service
@Transactional
public class ChapterService {

	@Autowired
	private ChapterRepository	chapterRepository;

	@Autowired
	private BookService			bookService;

	@Autowired
	private WriterService		writerService;

	//FIXME: Add en las palabras de spam
	public static final String	REGEXP	= "([\\W\\s]+)";


	public Collection<Chapter> getChaptersOfABook(final int idBook) {
		return this.chapterRepository.getChaptersOfABook(idBook);
	}

	public void deleteChapters(final Collection<Chapter> chapters) {
		this.chapterRepository.delete(chapters);
	}

	public Collection<Integer> getNumbersOfChaptersOfABook(final int idBook) {
		return this.chapterRepository.getNumbersOfChaptersOfABook(idBook);
	}

	public Chapter save(final Chapter chapter) {

		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("WRITER"));

		final Book book = chapter.getBook();

		final Writer writerLogged = this.writerService.findByPrincipal(LoginService.getPrincipal().getId());

		Assert.isTrue(book.getWriter().equals(writerLogged));

		Assert.isTrue(book.getDraft());

		final Collection<Integer> numbersOfBook = this.getNumbersOfChaptersOfABook(chapter.getId());

		Assert.isTrue(!numbersOfBook.contains(chapter.getNumber()));

		Integer numPalabras;
		if (chapter.getId() != 0) {
			final Chapter chapterOld = this.findOne(chapter.getId());
			final Integer numPalabrasOld = chapterOld.getText().split(ChapterService.REGEXP).length;

			final Integer numPalabrasNuevo = chapter.getText().split(ChapterService.REGEXP).length;
			numPalabras = numPalabrasNuevo - numPalabrasOld;

		} else
			numPalabras = chapter.getText().split(ChapterService.REGEXP).length;

		book.setNumWords(book.getNumWords() + numPalabras);
		this.bookService.save(book);

		return this.chapterRepository.save(chapter);

	}

	public void flush() {
		this.chapterRepository.flush();
	}

	public void delete(final int idChapter) {

		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("WRITER"));
		final Chapter chapter = this.findOne(idChapter);

		final Book book = chapter.getBook();

		final Writer writerLogged = this.writerService.findByPrincipal(LoginService.getPrincipal().getId());

		Assert.isTrue(book.getWriter().equals(writerLogged));

		Assert.isTrue(book.getDraft());

		final Integer numPalabras = chapter.getText().split(ChapterService.REGEXP).length;

		book.setNumWords(book.getNumWords() - numPalabras);
		this.bookService.save(book);

		this.chapterRepository.delete(chapter);

	}

	public Collection<Chapter> getAllChaptersOfWriter(final int writerId) {
		return this.chapterRepository.getChaptersOfWriter(writerId);
	}

	public Collection<Chapter> getAllChaptersOfPublisher(final Collection<Book> booksPublisher) {
		final Collection<Chapter> res = new ArrayList<>();

		for (final Book b : booksPublisher)
			res.addAll(this.chapterRepository.getChaptersOfABook(b.getId()));

		return res;
	}

	public Chapter findOne(final int idChapter) {
		return this.chapterRepository.findOne(idChapter);
	}

	public Chapter create(final Book book) {
		final Chapter chapter = new Chapter();
		chapter.setBook(book);
		return chapter;
	}

}
