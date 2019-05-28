
package controllers.writer;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.BookService;
import services.ChapterService;
import services.GenreService;
import services.OpinionService;
import services.PublisherService;
import services.WriterService;
import controllers.AbstractController;
import domain.Book;
import domain.Publisher;
import domain.Writer;
import forms.BookForm;

@Controller
@RequestMapping("/book/writer")
public class BookWriterController extends AbstractController {

	@Autowired
	private BookService			bookService;

	@Autowired
	private WriterService		writerService;

	@Autowired
	private ChapterService		chapterService;

	@Autowired
	private PublisherService	publisherService;

	@Autowired
	private GenreService		genreService;

	@Autowired
	private OpinionService		opionionService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		return this.listModelAndView();
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final Integer idBook) {
		ModelAndView result;
		final Writer writerLogged = this.writerService.findByPrincipal(LoginService.getPrincipal().getId());

		final Book book = this.bookService.findOne(idBook);

		if (book.getWriter().equals(writerLogged)) {
			result = new ModelAndView("book/display");
			result.addObject("book", book);
			result.addObject("logged", true);
			result.addObject("chapters", this.chapterService.getChaptersOfABook(book.getId()));

			//FIXME: ADD THE OPINIONS
			result.addObject("opinions", this.opionionService.getOpinionsOfBook(idBook));
			result.addObject("requestURIChapters", "book/writer/display.do?idBook=" + idBook);
			result.addObject("requestURIOpinions", "book/writer/display.do?idBook=" + idBook);
			this.configValues(result);
		} else
			result = new ModelAndView("redirect:list.do");

		return result;
	}
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final BookForm bookForm = new BookForm();
		return this.createAndEditModelAndView(bookForm);
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final Integer idBook) {

		ModelAndView result;
		final Writer writerLogged = this.writerService.findByPrincipal(LoginService.getPrincipal().getId());

		final Book book = this.bookService.findOne(idBook);

		if (!book.getWriter().equals(writerLogged) || !book.getDraft())
			result = this.listModelAndView("cannot.edit.book");
		else
			result = this.createAndEditModelAndView(book.castToForm());

		return result;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView save(@Valid final BookForm bookForm, final BindingResult bindingResult) {

		ModelAndView result;

		if (bindingResult.hasErrors())
			result = this.createAndEditModelAndView(bookForm);
		else
			try {
				this.bookService.save(bookForm, bindingResult);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = this.createAndEditModelAndView(bookForm, "cannot.save.book");
			}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final Integer idBook) {
		ModelAndView result;

		try {
			this.bookService.delete(idBook);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			oops.printStackTrace();
			result = this.listModelAndView("cannot.delete.book");
		}

		return result;
	}

	@RequestMapping(value = "/changeDraft", method = RequestMethod.GET)
	public ModelAndView changeDraft(@RequestParam final Integer idBook) {
		ModelAndView result;

		try {
			this.bookService.changeDraft(idBook);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			oops.printStackTrace();
			result = this.listModelAndView("cannot.changeDraft.book");
		}

		return result;
	}

	@RequestMapping(value = "/copy", method = RequestMethod.GET)
	public ModelAndView copy(@RequestParam final Integer idBook) {
		ModelAndView result;

		try {
			this.bookService.copyBook(idBook);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			oops.printStackTrace();
			result = this.listModelAndView("cannot.copy.book");
		}

		return result;
	}

	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public ModelAndView cancel(@RequestParam final Integer idBook) {
		ModelAndView result;

		try {
			this.bookService.cancelBook(idBook);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			oops.printStackTrace();
			result = this.listModelAndView("cannot.cancel.book");
		}

		return result;
	}

	protected ModelAndView createAndEditModelAndView(final BookForm bookForm) {
		return this.createAndEditModelAndView(bookForm, null);
	}

	protected ModelAndView createAndEditModelAndView(final BookForm bookForm, final String message) {
		final ModelAndView result = new ModelAndView("book/edit");

		final Collection<Publisher> publishers = this.publisherService.findAll();
		result.addObject("bookForm", bookForm);
		result.addObject("publishers", publishers);
		result.addObject("message", message);
		result.addObject("genres", this.genreService.findAll());

		this.configValues(result);

		return result;

	}
	protected ModelAndView listModelAndView() {
		return this.listModelAndView(null);
	}

	protected ModelAndView listModelAndView(final String message) {
		final ModelAndView result = new ModelAndView("book/list");
		final Collection<Book> books = this.bookService.getAllBooksOfLoggedWriter();
		final Collection<Book> booksCanChangeDraft = this.bookService.getBooksCanChangeDraft();

		result.addObject("books", books);
		result.addObject("booksCanChangeDraft", booksCanChangeDraft);
		result.addObject("myList", true);
		result.addObject("message", message);
		result.addObject("requestURI", "book/writer/list.do");
		result.addObject("title", "book.title.myBooks");

		this.configValues(result);

		return result;
	}
}
