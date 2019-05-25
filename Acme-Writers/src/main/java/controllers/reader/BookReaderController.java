
package controllers.reader;

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
import controllers.AbstractController;
import domain.Reader;

@Controller
@RequestMapping("/book/reader")
public class BookReaderController extends AbstractController {

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


	@RequestMapping(value = "/listFavourites", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result = new ModelAndView("book/list");
		final Reader reader = this.readerService.findByPrincipal(LoginService.getPrincipal());
		result.addObject("books", reader.getBooks());
		result.addObject("favourites", true);
		result.addObject("favouritesBooks", reader.getBooks());
		result.addObject("requestURI", "book/reader/listFavourites.do");

		this.configValues(result);

		return result;
	}

	@RequestMapping(value = "/addToList", method = RequestMethod.GET)
	public ModelAndView addToList(@RequestParam final int idBook) {
		ModelAndView result;
		try {
			this.bookService.addBookToFavouriteList(idBook);
			result = new ModelAndView("redirect:listFavourites.do");
		} catch (final Throwable oops) {
			result = this.listModelAndView("cannot.add.book");
		}

		return result;
	}

	@RequestMapping(value = "/removeFromFavourites", method = RequestMethod.GET)
	public ModelAndView removeFromFavourites(@RequestParam final int idBook) {
		ModelAndView result;
		try {
			this.bookService.deleteBookFromFavouriteList(idBook);
			result = new ModelAndView("redirect:listFavourites.do");
		} catch (final Throwable oops) {
			result = this.listModelAndView("cannot.remove.book");
		}

		return result;
	}

	protected ModelAndView listModelAndView() {

		return this.listModelAndView(null);
	}

	protected ModelAndView listModelAndView(final String message) {
		final ModelAndView result = new ModelAndView("book/list");
		final Reader reader = this.readerService.findByPrincipal(LoginService.getPrincipal());
		result.addObject("books", reader.getBooks());
		result.addObject("favourites", true);
		result.addObject("message", message);
		result.addObject("requestURI", "book/reader/listFavourites.do");

		this.configValues(result);

		return result;
	}
}
