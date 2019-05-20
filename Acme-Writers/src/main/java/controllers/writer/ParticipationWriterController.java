
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
import services.ParticipationService;
import services.WriterService;
import controllers.AbstractController;
import domain.Book;
import domain.Participation;
import domain.Writer;

@Controller
@RequestMapping("/item/provider")
public class ParticipationWriterController extends AbstractController {

	@Autowired
	private ParticipationService	participationService;

	@Autowired
	private WriterService			writerService;

	@Autowired
	private BookService				bookService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int idContest) {
		final Participation participation = this.participationService.create(idContest);
		return this.createEditModelAndView(participation);

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int idParticipation) {
		ModelAndView result;

		final Participation participation = this.participationService.findOne(idParticipation);
		final Writer writer = this.writerService.findByPrincipal(LoginService.getPrincipal());
		if (this.bookService.getBookOfParticipation(participation.getId()).getWriter().getId() != writer.getId())
			result = this.listModelAndView("participation.cannot.edit");
		else
			result = this.createEditModelAndView(participation);
		this.configValues(result);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Participation participation, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(participation);
		else
			try {
				final Participation participationRec = this.participationService.reconstruct(participation, binding);
				this.participationService.save(participationRec);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(participation, "cannot.save.participation");
			}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		return this.listModelAndView(null);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int idParticipation) {
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

	protected ModelAndView listModelAndView(final String message) {
		final ModelAndView result = new ModelAndView("participation/list");
		final Writer writer = this.writerService.findByPrincipal(LoginService.getPrincipal());
		final Collection<Participation> participations = this.participationService.getParticipationsOfWriter(writer.getId());
		result.addObject("participations", participations);
		result.addObject("writer", true);
		result.addObject("requestURI", "participation/writer/list.do");
		result.addObject("message", message);
		this.configValues(result);
		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int idParticipation) {
		final ModelAndView result;

		final Participation participation = this.participationService.findOne(idParticipation);

		result = new ModelAndView("participation/display");

		result.addObject("participation", participation);

		this.configValues(result);
		return result;

	}

	protected ModelAndView createEditModelAndView(final Participation participation) {
		return this.createEditModelAndView(participation, null);
	}

	protected ModelAndView createEditModelAndView(final Participation participation, final String message) {
		final ModelAndView result = new ModelAndView("participation/edit");
		final Collection<Book> books = this.bookService.getBooksOfWriter(this.writerService.findByPrincipal(LoginService.getPrincipal()).getId());

		result.addObject("participation", participation);
		result.addObject("books", books);
		result.addObject("message", message);
		this.configValues(result);

		return result;
	}
}
