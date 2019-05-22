
package controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.LoginService;
import security.UserAccount;
import services.ActorService;
import services.AdministratorService;
import services.MessageService;
import services.PublisherService;
import services.ReaderService;
import services.SocialProfileService;
import services.SponsorService;
import services.WriterService;
import utiles.AuthorityMethods;

import com.fasterxml.jackson.core.JsonProcessingException;

import domain.Actor;
import domain.Administrator;
import domain.Message;
import domain.Publisher;
import domain.Reader;
import domain.SocialProfile;
import domain.Sponsor;
import domain.Writer;

@Controller
@RequestMapping("/actor")
public class ActorController extends AbstractController {

	@Autowired
	private ActorService			actorService;

	@Autowired
	private PublisherService		publisherService;

	@Autowired
	private WriterService			writerService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private SocialProfileService	socialProfileService;

	@Autowired
	private SponsorService			sponsorService;

	@Autowired
	private ReaderService			readerService;

	@Autowired
	private MessageService			messageService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result;

		result = this.createModelAndViewDisplay();

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {

		ModelAndView res;

		res = this.createModelAndViewEditActor();

		return res;

	}

	protected ModelAndView createModelAndViewDisplay() {
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
			final Writer writer = this.writerService.findOne(actor.getId());
			result.addObject("writer", writer);
			break;

		case "READER":
			final Reader reader = this.readerService.findOne(actor.getId());
			result.addObject("reader", reader);
			break;

		case "PUBLISHER":
			final Publisher publisher = this.publisherService.findOne(actor.getId());
			result.addObject("publisher", publisher);

			break;

		case "SPONSOR":
			final Sponsor sponsor = this.sponsorService.findOne(actor.getId());
			result.addObject("sponsor", sponsor);
			break;
		}

		result.addObject("back", false);
		result.addObject("socialProfiles", socialProfiles);
		result.addObject("requestURI", "actor/display.do");

		this.configValues(result);
		return result;

	}

	protected ModelAndView createModelAndViewEditActor() {
		ModelAndView result;

		final Authority authority = AuthorityMethods.getLoggedAuthority();

		switch (authority.getAuthority()) {
		case "ADMINISTRATOR":
			final Administrator admin = this.administratorService.findByPrincipal(LoginService.getPrincipal());
			result = new ModelAndView("administrator/edit");
			result.addObject("administrator", admin);
			result.addObject("edit", true);
			break;

		case "WRITER":
			final Writer writer = this.writerService.findByPrincipal(LoginService.getPrincipal());
			result = new ModelAndView("writer/edit");
			result.addObject("writer", writer);
			result.addObject("edit", true);
			break;

		case "READER":
			final Reader reader = this.readerService.findByPrincipal(LoginService.getPrincipal());
			result = new ModelAndView("reader/edit");
			result.addObject("reader", reader);
			result.addObject("edit", true);
			break;

		case "PUBLISHER":
			final Publisher publisher = this.publisherService.findByPrincipal(LoginService.getPrincipal());
			result = new ModelAndView("publisher/edit");
			result.addObject("publisher", publisher);
			result.addObject("edit", true);
			break;

		case "SPONSOR":
			final Sponsor sponsor = this.sponsorService.findByPrincipal(LoginService.getPrincipal());
			result = new ModelAndView("sponsor/edit");
			result.addObject("sponsor", sponsor);
			result.addObject("edit", true);
			break;
		default:

			result = new ModelAndView("/");
			break;
		}

		this.configValues(result);
		return result;
	}

	@RequestMapping(value = "/displayData", method = RequestMethod.GET)
	public ModelAndView displayData() {
		final ModelAndView result = new ModelAndView("actor/displayData");
		List<Message> messages;
		final List<SocialProfile> socialProfiles = (List<SocialProfile>) this.socialProfileService.findAllSocialProfiles();

		final UserAccount principal = LoginService.getPrincipal();
		String authority = AuthorityMethods.getLoggedAuthority().getAuthority();

		if (authority.equals("BAN"))
			authority = this.actorService.checkAuthorityIsBanned(principal);

		result.addObject("authority", authority);

		switch (authority) {
		case "ADMINISTRATOR":
			final Administrator administrator = this.administratorService.findByPrincipal(principal);
			messages = (List<Message>) this.messageService.findAllByActor(administrator.getId());
			result.addObject("administrator", administrator);
			result.addObject("messages", messages);
			break;

		case "WRITER":
			final Writer writer = this.writerService.findByPrincipal(LoginService.getPrincipal());

			result.addObject("writer", writer);

			messages = (List<Message>) this.messageService.findAllByActor(writer.getId());

			result.addObject("messages", messages);

			break;

		case "READER":
			final Reader reader = this.readerService.findByPrincipal(LoginService.getPrincipal());
			messages = (List<Message>) this.messageService.findAllByActor(reader.getId());

			result.addObject("reader", reader);

			result.addObject("messages", messages);

			break;

		case "SPONSOR":
			final Sponsor sponsor = this.sponsorService.findByPrincipal(LoginService.getPrincipal());

			result.addObject("sponsor", sponsor);

			messages = (List<Message>) this.messageService.findAllByActor(sponsor.getId());
			result.addObject("messages", messages);
			break;

		case "PUBLISHER":
			final Publisher publisher = this.publisherService.findByPrincipal(LoginService.getPrincipal());

			result.addObject("publisher", publisher);

			messages = (List<Message>) this.messageService.findAllByActor(publisher.getId());
			result.addObject("messages", messages);
			break;
		}
		result.addObject("socialProfiles", socialProfiles);

		this.configValues(result);
		return result;

	}
	@RequestMapping(value = "/saveData", method = RequestMethod.GET)
	public ModelAndView fileJSON() throws JsonProcessingException {
		final ModelAndView result = new ModelAndView("actor/exportData");

		final String json = this.actorService.exportData();

		final UserAccount principal = LoginService.getPrincipal();
		final List<Authority> authorities = (List<Authority>) principal.getAuthorities();
		final String authority = authorities.get(0).getAuthority();

		result.addObject("authority", authority);
		result.addObject("json", json);

		this.configValues(result);
		return result;

	}

	@RequestMapping(value = "/deleteData", method = RequestMethod.GET)
	public ModelAndView deleteAllData() {
		ModelAndView result;
		try {
			this.actorService.deleteData();
			result = new ModelAndView("redirect:../j_spring_security_logout");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:displayData.do");
		}

		this.configValues(result);
		return result;
	}

}
