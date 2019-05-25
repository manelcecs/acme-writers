
package controllers.publisher;

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
import security.UserAccount;
import services.ParticipationService;
import services.PublisherService;
import controllers.AbstractController;
import domain.Participation;
import domain.Publisher;

@Controller
@RequestMapping("/participation/publisher")
public class ParticipationPublisherController extends AbstractController {

	@Autowired
	private ParticipationService	participationService;

	@Autowired
	private PublisherService		publisherService;


	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int idParticipation) throws ParseException {
		ModelAndView result;

		final Participation participation = this.participationService.findOne(idParticipation);
		final Publisher publisher = this.publisherService.findByPrincipal(LoginService.getPrincipal());
		if (participation.getContest().getPublisher().getId() != publisher.getId())
			result = this.listModelAndView("participation.cannot.edit");
		else {
			final Date actual = new Date();

			result = this.createEditModelAndView(participation);
			result.addObject("actual", actual);
		}
		this.configValues(result);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Participation participation, final BindingResult binding) {
		ModelAndView result;

		try {
			final Participation participationRec = this.participationService.reconstruct(participation, binding);
			this.participationService.save(participationRec);
			result = new ModelAndView("redirect:list.do");
		} catch (final ValidationException oops) {
			participation.setBook(this.participationService.findOne(participation.getId()).getBook());
			participation.setComment(this.participationService.findOne(participation.getId()).getComment());
			participation.setContest(this.participationService.findOne(participation.getId()).getContest());
			participation.setMoment(this.participationService.findOne(participation.getId()).getMoment());

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

	protected ModelAndView listModelAndView(final String message) throws ParseException {
		final ModelAndView result = new ModelAndView("participation/list");
		final UserAccount principal = LoginService.getPrincipal();
		final Collection<Participation> participations = this.participationService.getParticipationsOfPublisher(this.publisherService.findByPrincipal(principal).getId());
		final Date actual = new Date();
		result.addObject("actual", actual);
		result.addObject("participations", participations);
		result.addObject("publisher", true);
		result.addObject("requestURI", "participation/publisher/list.do");
		result.addObject("message", message);
		//this.configValues(result);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Participation participation) {
		return this.createEditModelAndView(participation, null);
	}

	protected ModelAndView createEditModelAndView(final Participation participation, final String message) {
		final ModelAndView result = new ModelAndView("participation/edit");
		result.addObject("participation", participation);
		result.addObject("message", message);
		//	this.configValues(result);

		return result;
	}
}
