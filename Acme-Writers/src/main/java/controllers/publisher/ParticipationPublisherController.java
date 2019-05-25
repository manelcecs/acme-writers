
package controllers.publisher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.validation.ValidationException;

import org.joda.time.LocalDateTime;
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

	private final SimpleDateFormat	FORMAT	= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int idParticipation) throws ParseException {
		ModelAndView result;

		final Participation participation = this.participationService.findOne(idParticipation);
		final Publisher publisher = this.publisherService.findByPrincipal(LoginService.getPrincipal());
		if (participation.getContest().getPublisher().getId() != publisher.getId())
			result = this.listModelAndView("participation.cannot.edit");
		else
			result = this.createEditModelAndView(participation);
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
		final LocalDateTime DATETIMENOW = LocalDateTime.now();
		final Date actual = this.FORMAT.parse(DATETIMENOW.getYear() + "/" + DATETIMENOW.getMonthOfYear() + "/" + DATETIMENOW.getDayOfMonth() + " " + DATETIMENOW.getHourOfDay() + ":" + LocalDateTime.now().getMinuteOfHour() + ":"
			+ DATETIMENOW.getSecondOfMinute());
		result.addObject("participations", participations);
		result.addObject("publisher", true);
		result.addObject("actual", actual);
		result.addObject("requestURI", "participation/publisher/list.do");
		result.addObject("message", message);
		//this.configValues(result);
		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int idParticipation) {
		final ModelAndView result;

		final Participation participation = this.participationService.findOne(idParticipation);

		result = new ModelAndView("participation/display");

		result.addObject("participation", participation);

		//	this.configValues(result);
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
