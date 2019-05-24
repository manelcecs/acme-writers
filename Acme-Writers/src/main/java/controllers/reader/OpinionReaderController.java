
package controllers.reader;

import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.BookService;
import services.OpinionService;
import services.ReaderService;
import controllers.AbstractController;
import domain.Book;
import domain.Opinion;

@Controller
@RequestMapping("/opinion/reader")
public class OpinionReaderController extends AbstractController {

	@Autowired
	private OpinionService	opinionService;

	@Autowired
	private ReaderService	readerService;

	@Autowired
	private BookService		bookService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result = new ModelAndView("opinion/list");
		final Collection<Opinion> opinions = this.opinionService.findOpinionsByReader(this.readerService.findByPrincipal(LoginService.getPrincipal()).getId());

		result.addObject("opinions", opinions);
		result.addObject("requestURI", "opinion/list.do");

		this.configValues(result);
		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int idOpinion) {
		ModelAndView result;

		final Opinion opinion = this.opinionService.findOne(idOpinion);

		result = new ModelAndView("opinion/display");
		result.addObject("opinion", opinion);
		result.addObject("requestURI", "opinion/display.do?idOpinion=" + idOpinion);

		this.configValues(result);
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam(required = true) final int idBook) {
		ModelAndView result;
		final Book book = this.bookService.findOne(idBook);
		final Opinion opinion = this.opinionService.create(book);
		result = this.createEditModelAndView(opinion);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam(required = true) final int idOpinion) {
		ModelAndView result;

		final Opinion opinion = this.opinionService.findOne(idOpinion);

		if (!opinion.getReader().equals(this.readerService.findByPrincipal(LoginService.getPrincipal())))
			result = new ModelAndView("redirect:list.do");
		else
			result = this.createEditModelAndView(opinion);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Opinion opinion, final BindingResult binding) {
		ModelAndView result;
		try {
			final Opinion opinionRect = this.opinionService.reconstruct(opinion, binding);
			this.opinionService.save(opinionRect);
			result = new ModelAndView("redirect:list.do");
		} catch (final ValidationException oops) {
			result = this.createEditModelAndView(opinion);
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(opinion, "opinion.edit.commit.error");

		}
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam(required = true) final int idOpinion) {
		ModelAndView result;
		try {
			this.opinionService.delete(idOpinion);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:list.do");
		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final Opinion opinion) {
		return this.createEditModelAndView(opinion, null);
	}

	protected ModelAndView createEditModelAndView(final Opinion opinion, final String message) {
		final ModelAndView result = new ModelAndView("opinion/edit");

		result.addObject("opinion", opinion);
		result.addObject("message", message);

		return result;
	}

}
