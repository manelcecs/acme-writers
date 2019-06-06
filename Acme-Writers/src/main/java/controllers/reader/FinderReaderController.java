
package controllers.reader;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import security.UserAccount;
import services.BookService;
import services.FinderService;
import services.GenreService;
import services.ReaderService;
import controllers.AbstractController;
import domain.Book;
import domain.Finder;
import domain.Genre;
import domain.Reader;

@Controller
@RequestMapping("/finder/reader")
public class FinderReaderController extends AbstractController {

	@Autowired
	private ReaderService	readerService;

	@Autowired
	private FinderService	finderService;

	@Autowired
	private BookService		bookService;

	@Autowired
	private GenreService	genreService;


	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() throws ParseException {
		final ModelAndView result;

		final UserAccount principal = LoginService.getPrincipal();
		final Reader reader = this.readerService.findByPrincipal(principal);

		final Finder finder = reader.getFinder();

		result = this.createEditModelAndView(finder);
		result.addObject("requestURI", "finder/reader/edit.do");
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Finder finder, final BindingResult binding) throws ParseException {
		ModelAndView result;

		final Finder finderBD = this.readerService.findByPrincipal(LoginService.getPrincipal()).getFinder();

		finder = this.finderService.reconstruct(finder, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(finder);
		else
			try {
				if (!this.finderService.cacheFinder(finderBD, finder))
					this.finderService.save(finder);

				result = new ModelAndView("redirect:edit.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(finder, "finder.commit.error");
			}
		this.configValues(result);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "clear")
	public ModelAndView clear() throws ParseException {
		ModelAndView result;
		final Finder finder = this.readerService.findByPrincipal(LoginService.getPrincipal()).getFinder();
		try {
			this.finderService.clear(finder);
			result = new ModelAndView("redirect:edit.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(finder, "finder.commit.error");
		}
		this.configValues(result);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Finder finder) {

		final ModelAndView result;

		result = this.createEditModelAndView(finder, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Finder finder, final String messageCode) {
		final ModelAndView result;

		result = new ModelAndView("finder/edit");

		final Collection<Book> books = this.bookService.getBooksByFinder(finder.getId());
		final Collection<Genre> genres = this.genreService.findAll();
		final List<String> languages = Arrays.asList("ES", "EN", "IT", "FR", "DE", "OTHER");

		result.addObject("finder", finder);
		result.addObject("books", books);
		result.addObject("genres", genres);
		result.addObject("languages", languages);

		result.addObject("message", messageCode);

		this.configValues(result);
		return result;
	}
}
