
package controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.LoginService;
import security.UserAccount;
import services.ActorService;
import services.AdministratorService;
import services.SponsorService;
import services.WriterService;
import services.ProviderService;
import services.PublisherService;
import services.SocialProfileService;
import utiles.AuthorityMethods;
import domain.Actor;
import domain.Administrator;
import domain.Publisher;
import domain.Reader;
import domain.SocialProfile;
import domain.Sponsor;
import domain.Writer;

@Controller
@RequestMapping("/socialProfile")
public class SocialProfileController extends AbstractController {

	@Autowired
	private SocialProfileService	socialProfileService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private PublisherService			rookieService;

	@Autowired
	private WriterService			companyService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ProviderService			providerService;

	@Autowired
	private SponsorService			auditorService;


	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(final int socialProfileId) {
		ModelAndView res;

		res = this.createModelAndViewEdit(this.socialProfileService.findOne(socialProfileId));

		return res;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView res;

		final SocialProfile socialProfile = this.socialProfileService.create();

		res = this.createModelAndViewEdit(socialProfile);

		return res;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView save(@Valid final SocialProfile socialProfile, final BindingResult binding) {
		ModelAndView res;

		if (binding.hasErrors())
			res = this.createModelAndViewEdit(socialProfile);
		else
			try {
				this.socialProfileService.save(socialProfile);
				res = new ModelAndView("redirect:/actor/display.do");
			} catch (final Throwable oops) {
				res = this.createModelAndViewEdit(socialProfile, "cannot.save.socialProfile");
			}
		return res;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int socialProfileId) {
		ModelAndView res;

		try {
			final SocialProfile sp = this.socialProfileService.findOne(socialProfileId);
			this.socialProfileService.delete(sp);
			res = new ModelAndView("redirect:/actor/display.do");
		} catch (final Throwable oops) {
			res = this.createModelAndViewDisplay("cannot.delete.socialProfile");
		}
		return res;
	}
	protected ModelAndView createModelAndViewEdit(final SocialProfile socialProfile) {
		return this.createModelAndViewEdit(socialProfile, null);
	}

	protected ModelAndView createModelAndViewEdit(final SocialProfile socialProfile, final String message) {
		ModelAndView res;

		Assert.isTrue(socialProfile.getActor().getId() == this.actorService.findByUserAccount(LoginService.getPrincipal()).getId());

		res = new ModelAndView("socialProfile/edit");
		res.addObject("socialProfile", socialProfile);
		res.addObject("message", message);

		this.configValues(res);

		return res;
	}

	protected ModelAndView createModelAndViewDisplay(final String message) {
		final ModelAndView result = new ModelAndView("actor/display");

		final UserAccount principal = LoginService.getPrincipal();

		final Actor actor = this.actorService.findByUserAccount(principal);

		result.addObject("actor", actor);
		result.addObject("userLogged", principal);

		final Authority authority = AuthorityMethods.getLoggedAuthority();

		result.addObject("authority", authority.getAuthority());

		final List<SocialProfile> socialProfiles = (List<SocialProfile>) this.socialProfileService.findAllSocialProfiles();
		switch (authority.getAuthority()) {
		case "ADMINISTRATOR":
			final Administrator administrator = this.administratorService.findOne(actor.getId());
			result.addObject("administrator", administrator);
			break;

		case "WRITER":
			final Writer writer = this.rookieService.findOne(actor.getId());
			result.addObject("writer", writer);
			break;

		case "READER":
			final Reader reader = this.companyService.findOne(actor.getId());
			result.addObject("reader", reader);
			break;

		case "SPONSOR":
			final Sponsor sponsor = this.auditorService.findOne(actor.getId());
			result.addObject("sponsor", sponsor);
			break;

		case "PUBLISHER":
			final Publisher publisher = this.providerService.findOne(actor.getId());
			result.addObject("publisher", publisher);
			break;
		}

		result.addObject("socialProfiles", socialProfiles);
		result.addObject("message", message);
		result.addObject("requestURI", "actor/display.do");

		this.configValues(result);
		return result;

	}

}
