
package controllers;

import java.text.ParseException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ActorService;
import services.MessageBoxService;
import services.MessageService;
import utiles.ValidatorCollection;
import domain.Actor;
import domain.Message;
import domain.MessageBox;

@Controller
@RequestMapping("/message")
public class MessageController extends AbstractController {

	@Autowired
	private ActorService		actorService;

	@Autowired
	private MessageBoxService	messageBoxService;

	@Autowired
	private MessageService		messageService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int idMessage) {
		final ModelAndView result;

		final Message message = this.messageService.findOne(idMessage);

		if (this.messageService.checkMessagePermissions(message))
			result = this.displayModelAndView(message);
		else
			result = new ModelAndView("redirect:../messageBox/list.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;

		final Message message = this.messageService.create();

		result = this.createEditModelAndView(message);
		return result;
	}

	@RequestMapping(value = "/send", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("Message") Message message, final BindingResult binding) throws ParseException {
		ModelAndView result;

		final Collection<String> tags = ValidatorCollection.deleteStringsBlanksInCollection(message.getTags());
		message.setTags(tags);

		message = this.messageService.reconstruct(message, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(message);
		else
			try {
				this.messageService.save(message);
				result = new ModelAndView("redirect:../messageBox/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(message, "message.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/addTo", method = RequestMethod.POST)
	public ModelAndView addTo(@ModelAttribute("Message") Message message, final BindingResult binding) throws ParseException {
		ModelAndView result;
		message = this.messageService.reconstruct(message, binding);

		if (binding.hasErrors())
			result = this.displayModelAndView(message);
		else
			try {
				this.messageService.save(message);
				result = new ModelAndView("redirect:../messageBox/list.do");
			} catch (final Throwable oops) {
				result = this.displayModelAndView(message, "message.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/removeFrom", method = RequestMethod.GET)
	public ModelAndView removeFrom(@RequestParam final int idMessageBox, final int idMessage) {
		ModelAndView result;
		final Message message = this.messageService.findOne(idMessage);
		final MessageBox messageBox = this.messageBoxService.findOne(idMessageBox);

		try {
			this.messageService.removeFrom(message, messageBox);
			result = this.listModelAndView(this.messageBoxService.findOne(idMessageBox));
		} catch (final Throwable oops) {
			result = this.listModelAndView(this.messageBoxService.findOne(idMessageBox), "message.commit.error");
		}

		this.configValues(result);
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int idMessage) {
		ModelAndView result;
		final Message message = this.messageService.findOne(idMessage);
		final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());

		try {
			this.messageService.delete(message);
			final MessageBox trashBox = this.messageBoxService.findOriginalBox(actor.getId(), "Trash Box");
			result = this.listModelAndView(trashBox);
		} catch (final Throwable oops) {
			final MessageBox inBox = this.messageBoxService.findOriginalBox(actor.getId(), "In Box");
			result = this.listModelAndView(inBox, "message.commit.error");

		}

		this.configValues(result);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Message message) {
		return this.createEditModelAndView(message, null);
	}

	protected ModelAndView createEditModelAndView(final Message message, final String messageCode) {
		final ModelAndView result;

		result = new ModelAndView("message/send");

		final Actor sender = this.actorService.findByUserAccount(LoginService.getPrincipal());

		final Collection<Actor> actors = this.actorService.findNonEliminatedActors();
		actors.remove(sender);

		result.addObject("Message", message);
		result.addObject("actors", actors);
		result.addObject("message", messageCode);

		this.configValues(result);
		return result;
	}

	protected ModelAndView displayModelAndView(final Message message) {
		return this.displayModelAndView(message, null);
	}

	protected ModelAndView displayModelAndView(final Message message, final String messageCode) {
		final ModelAndView result = new ModelAndView("message/display");

		final Collection<MessageBox> boxesToMove = this.messageBoxService.findBoxToMove(message);

		result.addObject("Message", message);
		result.addObject("recipients", this.messageService.getRecipients(message.getId()));
		result.addObject("tags", message.getTags());
		result.addObject("boxesToMove", boxesToMove);
		result.addObject("message", messageCode);

		this.configValues(result);
		return result;
	}

	protected ModelAndView listModelAndView(final MessageBox boxSelect) {
		return this.listModelAndView(boxSelect, null);
	}
	protected ModelAndView listModelAndView(final MessageBox boxSelect, final String messageCode) {
		final ModelAndView result;
		result = new ModelAndView("messageBox/list");

		final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
		result.addObject("boxes", actor.getMessageBoxes());
		result.addObject("boxSelect", boxSelect);
		result.addObject("messages", boxSelect.getMessages());
		result.addObject("message", messageCode);
		this.configValues(result);
		return result;
	}
}
