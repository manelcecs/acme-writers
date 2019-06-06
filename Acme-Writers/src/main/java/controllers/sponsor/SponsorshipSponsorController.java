
package controllers.sponsor;

import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.AdminConfigService;
import services.ContestService;
import services.SponsorService;
import services.SponsorshipService;
import utiles.ValidateCreditCard;
import controllers.AbstractController;
import domain.AdminConfig;
import domain.Contest;
import domain.Sponsor;
import domain.Sponsorship;
import forms.SponsorshipForm;

@Controller
@RequestMapping("/sponsorship/sponsor")
public class SponsorshipSponsorController extends AbstractController {

	@Autowired
	private SponsorService		sponsorService;

	@Autowired
	private SponsorshipService	sponsorshipService;

	@Autowired
	private ContestService		contestService;

	@Autowired
	private AdminConfigService	adminConfigService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		return this.listModelAndView();
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int idSponsorship) {
		ModelAndView result;

		try {
			final Sponsorship sponsorship = this.sponsorshipService.findOne(idSponsorship);
			Assert.notNull(sponsorship);
			result = new ModelAndView("sponsorship/display");
			final String status = sponsorship.getCancelled() ? "CANCELLED" : "AVAILABLE";
			result.addObject("sponsorship", sponsorship);
			result.addObject("status", status);
			result.addObject("flatRateAppliedWithVAT", this.sponsorshipService.calculateFlateRateVAT(idSponsorship));
			result.addObject("requestURI", "/sponsorship/sponsor/display.do?idSponsorship=" + idSponsorship);
		} catch (final Exception e) {
			result = this.listModelAndView("security.error.accessDenied");
		}

		this.configValues(result);
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;

		final SponsorshipForm sponsorshipForm = new SponsorshipForm();
		final Sponsor sponsor = this.sponsorService.findByPrincipal(LoginService.getPrincipal());

		if (ValidateCreditCard.isCaducate(sponsor.getCreditCard()))
			result = this.listModelAndView("sponsorship.error.creditCardCaducate");
		else
			result = this.createEditModelAndView(sponsorshipForm);

		return result;
	}
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int idSponsorship) {
		ModelAndView result;

		final Sponsorship sponsorship = this.sponsorshipService.findOne(idSponsorship);

		try {
			Assert.notNull(sponsorship);
			this.sponsorshipService.delete(sponsorship);
			result = new ModelAndView("redirect:list.do");
		} catch (final Exception e) {
			result = this.listModelAndView("sponsorship.commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int idSponsorship) {
		ModelAndView result;

		try {
			final Sponsorship sponsorship = this.sponsorshipService.findOne(idSponsorship);
			Assert.notNull(sponsorship);
			final SponsorshipForm sponsorshipForm = sponsorship.castToForm();
			result = this.createEditModelAndView(sponsorshipForm);
		} catch (final Exception e) {
			result = this.listModelAndView("security.error.accessDenied");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final SponsorshipForm sponsorshipForm, final BindingResult binding) {
		ModelAndView result;

		try {
			final Sponsorship sponsorshipRect = this.sponsorshipService.reconstruct(sponsorshipForm, binding);
			this.sponsorshipService.save(sponsorshipRect);
			result = new ModelAndView("redirect:list.do");
		} catch (final ValidationException oops) {
			result = this.createEditModelAndView(sponsorshipForm);
			oops.printStackTrace();
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(sponsorshipForm, "sponsorship.commit.error");
		}

		return result;
	}

	protected ModelAndView listModelAndView() {
		return this.listModelAndView(null);
	}

	protected ModelAndView listModelAndView(final String messageCode) {
		final ModelAndView result = new ModelAndView("sponsorship/list");

		final Sponsor sponsor = this.sponsorService.findByPrincipal(LoginService.getPrincipal());
		final Collection<Sponsorship> sponsorships = this.sponsorshipService.findAllBySponsor(sponsor.getId());

		result.addObject("sponsorships", sponsorships);
		result.addObject("creditCardCaducate", ValidateCreditCard.isCaducate(sponsor.getCreditCard()));
		result.addObject("requestURI", "/sponsorship/provider/list.do");
		result.addObject("message", messageCode);

		this.configValues(result);
		return result;
	}

	protected ModelAndView createEditModelAndView(final SponsorshipForm sponsorshipForm) {
		return this.createEditModelAndView(sponsorshipForm, null);
	}

	protected ModelAndView createEditModelAndView(final SponsorshipForm sponsorshipForm, final String messageCode) {
		final ModelAndView result;

		result = new ModelAndView("sponsorship/edit");

		final Collection<Contest> posibleContests = this.contestService.getAllContestMinusAnonymous();

		final AdminConfig adminConfig = this.adminConfigService.getAdminConfig();

		final Double flatRate;
		if (sponsorshipForm.getId() == 0)
			flatRate = adminConfig.getFlatRate();
		else
			flatRate = this.sponsorshipService.findOne(sponsorshipForm.getId()).getFlatRateApplied();

		result.addObject("sponsorshipForm", sponsorshipForm);
		result.addObject("posibleContests", posibleContests);
		result.addObject("flatRate", flatRate);
		result.addObject("flatRateWithVAT", this.sponsorshipService.calculateFlateRateVAT(sponsorshipForm.getId()));
		result.addObject("message", messageCode);

		this.configValues(result);
		return result;
	}

}
