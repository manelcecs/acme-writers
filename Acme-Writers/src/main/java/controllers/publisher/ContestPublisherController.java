
package controllers.publisher;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ContestService;
import services.PublisherService;
import controllers.AbstractController;
import domain.Contest;
import domain.Publisher;

@Controller
@RequestMapping("/contest/publisher")
public class ContestPublisherController extends AbstractController {

	@Autowired
	private ContestService		contestService;

	@Autowired
	private PublisherService	publisherService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final Contest contest = new Contest();
		return this.createEditModelAndView(contest);

	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Contest contest, final BindingResult binding) throws ParseException {
		ModelAndView result;

		contest.setRules(utiles.ValidatorCollection.deleteStringsBlanksInCollection(contest.getRules()));

		if (contest.getDeadline() != null && !this.contestService.isBeforeDeadline(contest.getDeadline()))
			binding.rejectValue("deadline", "javax.validation.constraints.Future.message");
		if (binding.hasErrors())
			result = this.createEditModelAndView(contest);
		else
			try {
				this.contestService.save(contest);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(contest, "cannot.save.contest");
			}

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() throws ParseException {
		return this.listModelAndView(null);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int idContest) throws ParseException {
		ModelAndView result;
		final Contest contest = this.contestService.findOne(idContest);

		try {
			this.contestService.delete(contest);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.listModelAndView("contest.cannot.delete");
		}
		return result;

	}

	protected ModelAndView listModelAndView(final String message) throws ParseException {
		final ModelAndView result = new ModelAndView("contest/list");
		final Publisher publisher = this.publisherService.findByPrincipal(LoginService.getPrincipal());
		final Collection<Contest> contests = this.contestService.getContestsOfPublisher(publisher.getId());
		final Date actual = new Date();
		result.addObject("contests", contests);
		result.addObject("publisher", true);
		result.addObject("actual", actual);
		result.addObject("requestURI", "contest/publisher/list.do");
		result.addObject("message", message);
		this.configValues(result);
		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int idContest) {
		final ModelAndView result;

		final Contest contest = this.contestService.findOne(idContest);

		result = new ModelAndView("contest/display");
		result.addObject("urlBack", "contest/publisher/list.do");

		result.addObject("contest", contest);

		this.configValues(result);
		return result;

	}

	protected ModelAndView createEditModelAndView(final Contest contest) {
		return this.createEditModelAndView(contest, null);
	}

	protected ModelAndView createEditModelAndView(final Contest contest, final String message) {
		final ModelAndView result = new ModelAndView("contest/create");
		result.addObject("contest", contest);
		result.addObject("message", message);
		this.configValues(result);

		return result;
	}
}
