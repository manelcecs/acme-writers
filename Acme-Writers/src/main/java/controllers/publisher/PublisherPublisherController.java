
package controllers.publisher;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.PublisherService;
import services.SocialProfileService;
import controllers.AbstractController;
import domain.Publisher;
import domain.SocialProfile;
import forms.PublisherForm;

@Controller
@RequestMapping("/publisher")
public class PublisherPublisherController extends AbstractController {

	@Autowired
	private PublisherService		publisherService;

	@Autowired
	private SocialProfileService	socialProfileService;


	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register() {
		ModelAndView res;

		final PublisherForm publisherForm = new PublisherForm();

		res = this.createEditModelAndView(publisherForm);

		return res;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView save(final PublisherForm publisherForm, final BindingResult binding) {

		ModelAndView res;

		try {
			final Publisher publisherRect = this.publisherService.reconstruct(publisherForm, binding);
			this.publisherService.save(publisherRect);
			res = new ModelAndView("redirect:/welcome/index.do");
		} catch (final ValidationException oops) {
			res = this.createEditModelAndView(publisherForm);
		} catch (final Throwable oops) {
			res = this.createEditModelAndView(publisherForm, "publisher.edit.commit.error");

		}

		return res;

	}

	@RequestMapping(value = "/publisher/save", method = RequestMethod.POST)
	public ModelAndView save(final Publisher publisher, final BindingResult binding) {

		ModelAndView res;

		try {
			final Publisher publisherRect = this.publisherService.reconstruct(publisher, binding);
			this.publisherService.save(publisherRect);
			res = new ModelAndView("redirect:/actor/display.do");
		} catch (final ValidationException oops) {
			res = this.createEditModelAndView(publisher);
		} catch (final Throwable oops) {
			res = this.createEditModelAndView(publisher, "publisher.edit.commit.error");

		}

		return res;

	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(final Integer publisherId, final String targetURL) {
		return this.createModelAndViewDisplay(publisherId, targetURL);
	}

	protected ModelAndView createModelAndViewDisplay(final Integer publisherId, final String targetURL) {

		final Publisher actor = this.publisherService.findOne(publisherId);

		if (actor == null)
			return new ModelAndView("redirect:/");
		else {

			final ModelAndView result = new ModelAndView("actor/display");

			result.addObject("actor", actor);
			result.addObject("userLogged", null);

			result.addObject("back", true);

			result.addObject("authority", "PUBLISHER");

			final List<SocialProfile> socialProfiles = (List<SocialProfile>) this.socialProfileService.findAllSocialProfiles(publisherId);
			result.addObject("publisher", actor);

			result.addObject("socialProfiles", socialProfiles);
			result.addObject("requestURI", "actor/display.do");

			result.addObject("targetURL", targetURL);
			this.configValues(result);
			return result;
		}

	}
	protected ModelAndView createEditModelAndView(final PublisherForm publisherForm, final String... messages) {

		final ModelAndView result;

		result = new ModelAndView("publisher/edit");
		result.addObject("publisherForm", publisherForm);
		result.addObject("edit", false);

		final List<String> messageCodes = new ArrayList<>();
		for (final String s : messages)
			messageCodes.add(s);
		result.addObject("messages", messageCodes);

		this.setCreditCardMakes(result);
		this.configValues(result);

		return result;

	}

	protected ModelAndView createEditModelAndView(final Publisher publisher, final String... messages) {

		final ModelAndView result;

		result = new ModelAndView("publisher/edit");
		result.addObject("publisher", publisher);
		result.addObject("edit", true);
		final List<String> messageCodes = new ArrayList<>();
		for (final String s : messages)
			messageCodes.add(s);
		result.addObject("messages", messageCodes);

		this.setCreditCardMakes(result);
		this.configValues(result);

		return result;

	}

}
