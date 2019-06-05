
package controllers.publisher;

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
import services.OpinionService;
import services.PublisherService;
import controllers.AbstractController;
import domain.Book;
import domain.Publisher;

@Controller
@RequestMapping("/book/publisher")
public class BookPublisherController extends AbstractController {

	@Autowired
	private BookService			bookService;

	@Autowired
	private ChapterService		chapterService;

	@Autowired
	private PublisherService	publisherService;

	@Autowired
	private OpinionService		opinionService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		return this.listModelAndView();
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final Integer idBook) {
		ModelAndView result;
		final Publisher publiserLogged = this.publisherService.findByPrincipal(LoginService.getPrincipal());

		final Book book = this.bookService.findOne(idBook);

		if (book.getPublisher().equals(publiserLogged) || !book.getCancelled() || !book.getDraft()) {
			result = new ModelAndView("book/display");
			result.addObject("book", book);
			result.addObject("publisher", true);
			result.addObject("chapters", this.chapterService.getChaptersOfABook(book.getId()));

			//FIXME: ADD THE OPINIONS

			result.addObject("opinions", this.opinionService.getOpinionsOfBook(idBook));
			result.addObject("requestURIChapters", "book/publisher/display.do?idBook=" + idBook);
			result.addObject("requestURIOpinions", "book/publisher/display.do?idBook=" + idBook);

			this.configValues(result);
		} else
			result = new ModelAndView("redirect:list.do");

		return result;
	}

	@RequestMapping(value = "/changeStatus", method = RequestMethod.GET)
	public ModelAndView changeStatus(@RequestParam final String status, @RequestParam final int idBook) {
		ModelAndView result;

		try {
			this.bookService.changeStatus(idBook, status);
			result = new ModelAndView("redirect:list.do");
		} catch (final Exception e) {
			result = this.listModelAndView("cannot.changeStatus.book");
		}
		return result;
	}

	protected ModelAndView listModelAndView() {
		return this.listModelAndView(null);
	}

	protected ModelAndView listModelAndView(final String message) {
		final ModelAndView result = new ModelAndView("book/list");
		final Collection<Book> books = this.bookService.getBooksOfLoggedPublisher();

		result.addObject("books", books);
		result.addObject("message", message);
		result.addObject("publisher", true);
		result.addObject("title", "book.title.myBooks");
		result.addObject("targetURL", "/book/publisher/list.do");
		this.configValues(result);

		return result;
	}

}
