
package controllers.sponsor;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.SponsorService;
import controllers.AbstractController;
import domain.Sponsor;
import forms.SponsorForm;

@Controller
@RequestMapping("/sponsor")
public class SponsorSponsorController extends AbstractController {

	@Autowired
	private SponsorService	sponsorService;


	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register() {
		ModelAndView res;

		final SponsorForm sponsorForm = new SponsorForm();

		res = this.createEditModelAndView(sponsorForm);

		return res;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView save(final SponsorForm sponsorForm, final BindingResult binding) {

		ModelAndView res;

		try {
			final Sponsor sponsorRect = this.sponsorService.reconstruct(sponsorForm, binding);
			this.sponsorService.save(sponsorRect);
			res = new ModelAndView("redirect:/welcome/index.do");
		} catch (final ValidationException oops) {
			res = this.createEditModelAndView(sponsorForm);
		} catch (final Throwable oops) {
			res = this.createEditModelAndView(sponsorForm, "sponsor.edit.commit.error");

		}

		return res;

	}

	@RequestMapping(value = "/sponsor/save", method = RequestMethod.POST)
	public ModelAndView save(final Sponsor sponsor, final BindingResult binding) {

		ModelAndView res;

		try {
			final Sponsor sponsorRect = this.sponsorService.reconstruct(sponsor, binding);
			this.sponsorService.save(sponsorRect);
			res = new ModelAndView("redirect:/welcome/index.do");
		} catch (final ValidationException oops) {
			res = this.createEditModelAndView(sponsor);
		} catch (final Throwable oops) {
			res = this.createEditModelAndView(sponsor, "sponsor.edit.commit.error");

		}

		return res;

	}

	protected ModelAndView createEditModelAndView(final SponsorForm sponsorForm, final String... messages) {

		final ModelAndView result;

		result = new ModelAndView("sponsor/edit");
		result.addObject("sponsorForm", sponsorForm);
		result.addObject("edit", false);

		final List<String> messageCodes = new ArrayList<>();
		for (final String s : messages)
			messageCodes.add(s);
		result.addObject("messages", messageCodes);

		this.configValues(result);

		return result;

	}

	protected ModelAndView createEditModelAndView(final Sponsor sponsor, final String... messages) {

		final ModelAndView result;

		result = new ModelAndView("sponsor/edit");
		result.addObject("sponsor", sponsor);
		result.addObject("edit", true);
		final List<String> messageCodes = new ArrayList<>();
		for (final String s : messages)
			messageCodes.add(s);
		result.addObject("messages", messageCodes);

		this.configValues(result);

		return result;

	}
}
