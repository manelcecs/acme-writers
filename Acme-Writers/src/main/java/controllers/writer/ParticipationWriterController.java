
package controllers.writer;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;

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
import services.ParticipationService;
import services.WriterService;
import controllers.AbstractController;
import domain.Book;
import domain.Participation;
import domain.Writer;

@Controller
@RequestMapping("/participation/writer")
public class ParticipationWriterController extends AbstractController {

	@Autowired
	private ParticipationService	participationService;

	@Autowired
	private WriterService			writerService;

	@Autowired
	private BookService				bookService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int idContest) throws ParseException {
		final Participation participation = this.participationService.create(idContest);
		return this.createEditModelAndView(participation);

	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Participation participation, final BindingResult binding) {
		ModelAndView result;

		try {
			final Participation participationRec = this.participationService.reconstruct(participation, binding);
			this.participationService.save(participationRec);
			result = new ModelAndView("redirect:list.do");
		} catch (final ValidationException oops) {
			oops.printStackTrace();
			result = this.createEditModelAndView(participation);
		} catch (final Throwable oops) {
			oops.printStackTrace();
			result = this.createEditModelAndView(participation, "cannot.save.participation");
		}

		return result;
	}
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() throws ParseException {
		return this.listModelAndView(null);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int idParticipation) throws ParseException {
		ModelAndView result;
		final Participation participation = this.participationService.findOne(idParticipation);

		try {
			this.participationService.delete(participation);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.listModelAndView("participation.cannot.delete");
		}
		return result;

	}

	protected ModelAndView listModelAndView(final String message) throws ParseException {
		final ModelAndView result = new ModelAndView("participation/list");
		final Writer writer = this.writerService.findByPrincipal(LoginService.getPrincipal());
		final Collection<Participation> participations = this.participationService.getParticipationsOfWriter(writer.getId());
		final Date actual = new Date();
		result.addObject("participations", participations);
		result.addObject("writer", true);
		result.addObject("actual", actual);
		result.addObject("requestURI", "participation/writer/list.do");
		result.addObject("message", message);
		//this.configValues(result);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Participation participation) {
		return this.createEditModelAndView(participation, null);
	}

	protected ModelAndView createEditModelAndView(final Participation participation, final String message) {
		final ModelAndView result = new ModelAndView("participation/create");
		final Collection<Book> books = this.bookService.getAllBooksOfLoggedWriter();

		result.addObject("participation", participation);
		result.addObject("books", books);
		result.addObject("message", message);
		//	this.configValues(result);

		return result;
	}
}
