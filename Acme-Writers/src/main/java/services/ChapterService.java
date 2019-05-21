
package services;

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


	public Collection<Chapter> getChaptersOfABook(final int idBook) {
		return this.chapterRepository.getChaptersOfABook(idBook);
	}

	public void deleteChapters(final Collection<Chapter> chapters) {
		this.chapterRepository.delete(chapters);
	}

	public Chapter save(final Chapter chapter) {

		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("WRITER"));

		final Book book = chapter.getBook();

		final Writer writerLogged = this.writerService.findByPrincipal(LoginService.getPrincipal().getId());

		Assert.isTrue(book.getWriter().equals(writerLogged));

		Assert.isTrue(book.getDraft());

		return this.chapterRepository.save(chapter);

	}

	public void delete(final int idChapter) {

		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("WRITER"));
		final Chapter chapter = this.findOne(idChapter);

		final Book book = chapter.getBook();

		final Writer writerLogged = this.writerService.findByPrincipal(LoginService.getPrincipal().getId());

		Assert.isTrue(book.getWriter().equals(writerLogged));

		Assert.isTrue(book.getDraft());

		this.chapterRepository.delete(chapter);

	}

	public Chapter findOne(final int idChapter) {
		return this.chapterRepository.findOne(idChapter);
	}

}
