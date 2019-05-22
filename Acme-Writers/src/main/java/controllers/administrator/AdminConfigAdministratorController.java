
package controllers.administrator;

import java.util.List;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdminConfigService;
import controllers.AbstractController;
import domain.AdminConfig;
import forms.AdminConfigForm;
import forms.CreditCardMakeForm;
import forms.SpamWordForm;

@Controller
@RequestMapping("/adminConfig/administrator")
public class AdminConfigAdministratorController extends AbstractController {

	@Autowired
	private AdminConfigService	adminConfigService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		final ModelAndView result = new ModelAndView("adminConfig/display");

		final AdminConfig adminConfig = this.adminConfigService.getAdminConfig();
		result.addObject("adminConfig", adminConfig);

		this.configValues(result);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;

		final AdminConfigForm adminConfigForm = this.adminConfigService.getAdminConfig().castToForm();
		result = this.createModelAndView(adminConfigForm);

		return result;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final AdminConfigForm adminConfigForm, final BindingResult binding) {
		ModelAndView result;
		result = new ModelAndView("adminConfig/edit");

		try {
			final AdminConfig adminConfig = this.adminConfigService.reconstruct(adminConfigForm, binding);
			this.adminConfigService.save(adminConfig);
			result = new ModelAndView("redirect:display.do");
		} catch (final ValidationException oops) {
			result = this.createModelAndView(adminConfigForm);
		} catch (final Throwable oops) {
			result = this.createModelAndView(adminConfigForm, "adminConfig.save.error");
		}

		return result;
	}

	@RequestMapping(value = "/addSpamWord", method = RequestMethod.POST, params = "save")
	public ModelAndView addSpamWord(@Valid final SpamWordForm spamWordForm, final BindingResult binding) {
		ModelAndView result;
		result = new ModelAndView("adminConfig/edit");

		try {
			final AdminConfig adminConfig = this.adminConfigService.addSpamWord(spamWordForm, binding);
			this.adminConfigService.save(adminConfig);
			result = new ModelAndView("redirect:edit.do");
		} catch (final ValidationException oops) {
			result = this.createModelAndView(spamWordForm);
		} catch (final Throwable oops) {
			result = this.createModelAndView(spamWordForm, "adminConfig.save.error");
		}

		return result;
	}
	@RequestMapping(value = "/addCreditCardMake", method = RequestMethod.POST, params = "save")
	public ModelAndView addCreditCardMake(final CreditCardMakeForm creditCardMakeForm, final BindingResult binding) {
		ModelAndView result;
		result = new ModelAndView("adminConfig/edit");

		try {
			final AdminConfig adminConfig = this.adminConfigService.addCreditCardMake(creditCardMakeForm, binding);
			this.adminConfigService.save(adminConfig);
			result = new ModelAndView("redirect:edit.do");
		} catch (final ValidationException oops) {
			result = this.createModelAndView(creditCardMakeForm);
		} catch (final Throwable oops) {
			result = this.createModelAndView(creditCardMakeForm, "adminConfig.save.error");
			oops.printStackTrace();
		}

		return result;
	}

	@RequestMapping(value = "/deleteSpamWord", method = RequestMethod.POST)
	public ModelAndView deleteSpamWord(final String spamWord) {
		ModelAndView result;

		try {
			this.adminConfigService.deleteSpamWord(spamWord);
			result = new ModelAndView("redirect:edit.do");
		} catch (final Throwable oops) {
			result = this.createModelAndView("adminConfig.save.error");
		}

		this.configValues(result);
		return result;
	}

	@RequestMapping(value = "/deleteCreditCardMake", method = RequestMethod.POST)
	public ModelAndView deleteCreditCardMake(final String creditCardMake) {
		ModelAndView result;

		try {
			this.adminConfigService.deleteCreditCardMake(creditCardMake);
			result = new ModelAndView("redirect:edit.do");
		} catch (final Throwable oops) {
			result = this.createModelAndView("adminConfig.save.error");
		}

		this.configValues(result);
		return result;
	}

	protected ModelAndView createModelAndView(final AdminConfigForm adminConfigForm, final CreditCardMakeForm creditCardMakeForm, final SpamWordForm spamWordForm, final String message) {
		final ModelAndView result = new ModelAndView("adminConfig/edit");
		final List<String> spamWords = (List<String>) this.adminConfigService.getAdminConfig().getSpamWords();
		final List<String> creditCardMakes = (List<String>) this.adminConfigService.getAdminConfig().getCreditCardMakes();
		Boolean lastMake = false;
		if (creditCardMakes.size() == 1)
			lastMake = true;

		result.addObject("adminConfigForm", adminConfigForm);
		result.addObject("creditCardMakeForm", creditCardMakeForm);
		result.addObject("spamWordForm", spamWordForm);
		result.addObject("requestURI", "adminConfig/administrator/edit.do");
		result.addObject("spamWords", spamWords);
		result.addObject("creditCardMakes", creditCardMakes);
		result.addObject("lastMake", lastMake);
		result.addObject("message", message);

		this.configValues(result);
		return result;
	}

	protected ModelAndView createModelAndView(final AdminConfigForm adminConfigForm) {
		return this.createModelAndView(adminConfigForm, new CreditCardMakeForm(), new SpamWordForm(), null);
	}

	protected ModelAndView createModelAndView(final AdminConfigForm adminConfigForm, final String message) {
		return this.createModelAndView(adminConfigForm, new CreditCardMakeForm(), new SpamWordForm(), message);
	}

	protected ModelAndView createModelAndView(final CreditCardMakeForm creditCardMakeForm) {
		return this.createModelAndView(this.adminConfigService.getAdminConfig().castToForm(), creditCardMakeForm, new SpamWordForm(), null);
	}

	protected ModelAndView createModelAndView(final CreditCardMakeForm creditCardMakeForm, final String message) {
		return this.createModelAndView(this.adminConfigService.getAdminConfig().castToForm(), creditCardMakeForm, new SpamWordForm(), message);
	}

	protected ModelAndView createModelAndView(final SpamWordForm spamWordForm) {
		return this.createModelAndView(this.adminConfigService.getAdminConfig().castToForm(), new CreditCardMakeForm(), spamWordForm, null);
	}

	protected ModelAndView createModelAndView(final SpamWordForm spamWordForm, final String message) {
		return this.createModelAndView(this.adminConfigService.getAdminConfig().castToForm(), new CreditCardMakeForm(), spamWordForm, message);
	}

	protected ModelAndView createModelAndView(final String message) {
		return this.createModelAndView(this.adminConfigService.getAdminConfig().castToForm(), new CreditCardMakeForm(), new SpamWordForm(), message);
	}

}
