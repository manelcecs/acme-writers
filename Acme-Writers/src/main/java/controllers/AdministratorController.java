
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ActorService;
import services.BookService;
import services.SponsorshipService;
import domain.Actor;

@Controller
@RequestMapping("/administrator")
public class AdministratorController extends AbstractController {

	@Autowired
	private ActorService		actorService;

	@Autowired
	private BookService			bookService;

	@Autowired
	private SponsorshipService	sponsorshipService;


	@RequestMapping(value = "/process", method = RequestMethod.GET)
	public ModelAndView process() {
		return this.processModelAndView();
	}

	@RequestMapping(value = "/updateSpam", method = RequestMethod.GET)
	public ModelAndView updateSpam() {
		ModelAndView result;
		try {
			this.actorService.updateSpam();
			result = this.processModelAndView();
		} catch (final Throwable oops) {
			result = this.processModelAndView("administrator.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/ban", method = RequestMethod.GET)
	public ModelAndView ban(@RequestParam final Integer idActor) {
		ModelAndView result;
		try {
			final Actor actor = this.actorService.getActor(idActor);
			this.actorService.ban(actor);
			result = this.processModelAndView();
		} catch (final Throwable oops) {
			result = this.processModelAndView("administrator.commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/unban", method = RequestMethod.GET)
	public ModelAndView unban(@RequestParam final Integer idActor) {
		ModelAndView result;
		try {
			final Actor actor = this.actorService.getActor(idActor);
			this.actorService.unban(actor);
			result = this.processModelAndView();
		} catch (final Throwable oops) {
			result = this.processModelAndView("administrator.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/computeScore", method = RequestMethod.GET)
	public ModelAndView computeScore() {
		ModelAndView result;
		try {
			this.bookService.computeScore();
			result = this.processModelAndView();
		} catch (final Exception e) {
			result = this.processModelAndView("administrator.commit.error");
		}
		return result;

	}

	@RequestMapping(value = "/cancelSponsorship", method = RequestMethod.GET)
	public ModelAndView cancelSponsorship() {
		ModelAndView result;
		try {
			this.sponsorshipService.cancelSponsorshipCaducate();
			result = this.processModelAndView();
		} catch (final Exception e) {
			result = this.processModelAndView("administrator.commit.error");
		}
		return result;

	}

	protected ModelAndView processModelAndView() {
		return this.processModelAndView(null);
	}

	protected ModelAndView processModelAndView(final String messageCode) {
		final ModelAndView result = new ModelAndView("administrator/process");

		final Collection<Actor> spammers = this.actorService.getSpammerActors();

		result.addObject("spamActors", spammers);
		result.addObject("actorLogged", LoginService.getPrincipal());
		result.addObject("message", messageCode);

		this.configValues(result);
		return result;
	}
}
