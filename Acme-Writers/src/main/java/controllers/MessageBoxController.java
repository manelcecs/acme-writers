
package controllers;

import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ActorService;
import services.MessageBoxService;
import domain.Actor;
import domain.MessageBox;

@Controller
@RequestMapping("/messageBox")
public class MessageBoxController extends AbstractController {

	@Autowired
	private ActorService		actorService;

	@Autowired
	private MessageBoxService	messageBoxService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;

		final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
		final MessageBox boxDefault = this.messageBoxService.findOriginalBox(actor.getId(), "In Box");

		result = this.listModelAndView(boxDefault);
		result.addObject("requestURI", "messageBox/list.do");

		this.configValues(result);
		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int idBox) {
		final ModelAndView result;
		final MessageBox boxToGo = this.messageBoxService.findOne(idBox);
		final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());

		if (boxToGo == null) {
			final MessageBox boxDefault = this.messageBoxService.findOriginalBox(actor.getId(), "In Box");
			result = this.listModelAndView(boxDefault);
		} else
			result = this.listModelAndView(boxToGo);

		result.addObject("requestURI", "messageBox/display.do?idBox=" + idBox);
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;

		final MessageBox messageBox = this.messageBoxService.create();

		result = this.createEditModelAndView(messageBox);

		this.configValues(result);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int idMessageBox) {
		final ModelAndView result;

		final MessageBox messageBox = this.messageBoxService.findOne(idMessageBox);
		result = this.createEditModelAndView(messageBox);

		this.configValues(result);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final MessageBox messageBox, final BindingResult binding) {
		ModelAndView result;

		try {
			final MessageBox messageBoxRec = this.messageBoxService.reconstruct(messageBox, binding);
			this.messageBoxService.save(messageBoxRec);
			result = new ModelAndView("redirect:list.do");
		} catch (final ValidationException oops) {
			result = this.createEditModelAndView(messageBox);
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(messageBox, "messageBox.commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int idMessageBox) {
		ModelAndView result;
		final MessageBox box = this.messageBoxService.findOne(idMessageBox);

		try {
			this.messageBoxService.delete(box);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
			result = this.listModelAndView(this.messageBoxService.findOriginalBox(actor.getId(), "In Box"), "messageBox.commit.error");
			result.addObject("message", "messageBox.commit.error");
		}

		this.configValues(result);
		return result;
	}

	protected ModelAndView listModelAndView(final MessageBox boxSelect) {
		return this.listModelAndView(boxSelect, null);
	}

	protected ModelAndView listModelAndView(final MessageBox boxSelect, final String message) {
		final ModelAndView result = new ModelAndView("messageBox/list");

		final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());

		result.addObject("boxes", actor.getMessageBoxes());
		result.addObject("boxSelect", boxSelect);
		result.addObject("messages", boxSelect.getMessages());
		result.addObject("message", message);

		this.configValues(result);
		return result;
	}

	protected ModelAndView createEditModelAndView(final MessageBox messageBox) {
		return this.createEditModelAndView(messageBox, null);
	}

	protected ModelAndView createEditModelAndView(final MessageBox messageBox, final String messageCode) {
		final ModelAndView result = new ModelAndView("messageBox/edit");

		final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());

		final Collection<MessageBox> posibleParents = this.messageBoxService.findPosibleParents(actor.getId());
		posibleParents.remove(messageBox);
		final Collection<MessageBox> childrens = this.messageBoxService.allChildren(messageBox);
		posibleParents.removeAll(childrens);

		result.addObject("messageBox", messageBox);
		result.addObject("posibleParents", posibleParents);
		result.addObject("message", messageCode);

		this.configValues(result);
		return result;
	}
}
