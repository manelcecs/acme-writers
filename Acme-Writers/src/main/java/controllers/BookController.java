
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.BookService;
import services.ChapterService;
import services.GenreService;
import services.PublisherService;
import services.ReaderService;
import services.WriterService;
import domain.Book;
import domain.Reader;

@Controller
@RequestMapping("/book")
public class BookController extends AbstractController {

	@Autowired
	BookService			bookService;

	@Autowired
	WriterService		writerService;

	@Autowired
	ChapterService		chapterService;

	@Autowired
	PublisherService	publisherService;

	@Autowired
	GenreService		genreService;

	@Autowired
	ReaderService		readerService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final Integer idBook) {
		ModelAndView result;

		final Book book = this.bookService.findOne(idBook);

		if (!book.getDraft() && !book.getCancelled() && (book.getStatus().equals("INDEPENDENT") || book.getStatus().equals("ACCEPTED"))) {
			result = new ModelAndView("book/display");
			result.addObject("book", book);
			result.addObject("chapters", this.chapterService.getChaptersOfABook(book.getId()));

			//FIXME: ADD THE OPINIONS
			result.addObject("requestURIChapters", "book/display.do?idBook=" + idBook);
			result.addObject("requestURIOpinions", "book/display.do?idBook=" + idBook);
		} else
			result = new ModelAndView("redirect:listAll.do");

		return result;
	}

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ModelAndView listAll() {

		final Collection<Book> books = this.bookService.getAllVisibleBooks();
		return this.listModelAndView(books, "book/listAll.do");
	}

	@RequestMapping(value = "/listByWriter", method = RequestMethod.GET)
	public ModelAndView listByWriter(@RequestParam final int idWriter) {
		final Collection<Book> books = this.bookService.getAllVisibleBooksOfWriter(idWriter);
		return this.listModelAndView(books, "book/listByWriter.do");
	}

	@RequestMapping(value = "/listByPublisher", method = RequestMethod.GET)
	public ModelAndView listByPublisher(@RequestParam final int idPublisher) {
		final Collection<Book> books = this.bookService.getAllVisibleBooksOfPublisher(idPublisher);
		return this.listModelAndView(books, "book/listByPublisher.do");
	}

	//FIXME ESTO QUE ES?
	protected ModelAndView listModelAndView(final Collection<Book> books, final String requestURI) {
		final ModelAndView result = new ModelAndView("book/list");
		result.addObject("books", books);
		result.addObject("requestURI", requestURI);

		try {
			final Reader reader = this.readerService.findByPrincipal(LoginService.getPrincipal());
			final Boolean favourites = true;
			final Collection<Book> favouritesBooks = reader.getBooks();
			result.addObject("favouritesBooks", favouritesBooks);
			result.addObject("favourites", favourites);
		} catch (final Throwable socorro) {

		}

		return result;

	}
}
