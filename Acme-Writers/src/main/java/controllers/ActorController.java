
package controllers;

import java.text.ParseException;
import java.util.Collection;
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
import services.AnnouncementService;
import services.BookService;
import services.ChapterService;
import services.ContestService;
import services.MessageService;
import services.OpinionService;
import services.ParticipationService;
import services.PublisherService;
import services.ReaderService;
import services.SocialProfileService;
import services.SponsorService;
import services.SponsorshipService;
import services.WriterService;
import utiles.AuthorityMethods;

import com.fasterxml.jackson.core.JsonProcessingException;

import domain.Actor;
import domain.Administrator;
import domain.Announcement;
import domain.Book;
import domain.Chapter;
import domain.Contest;
import domain.Message;
import domain.Opinion;
import domain.Participation;
import domain.Publisher;
import domain.Reader;
import domain.SocialProfile;
import domain.Sponsor;
import domain.Sponsorship;
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
	@Autowired
	private BookService				bookService;

	@Autowired
	private ContestService			contestService;

	@Autowired
	private AnnouncementService		announcementService;

	@Autowired
	private ChapterService			chapterService;

	@Autowired
	private SponsorshipService		sponsorshipService;

	@Autowired
	private ParticipationService	participationService;

	@Autowired
	private OpinionService			opinionService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(final String targetURL) {
		ModelAndView result;

		if (AuthorityMethods.chechAuthorityLogged("BAN"))
			result = new ModelAndView("/");
		else
			result = this.createModelAndViewDisplay(targetURL);

		return result;
	}

	@RequestMapping(value = "/listWriters", method = RequestMethod.GET)
	public ModelAndView listWriters() {
		final ModelAndView result = new ModelAndView("actor/list");

		result.addObject("actors", this.writerService.findAll());
		result.addObject("rolView", "WRITER");
		result.addObject("requestURI", "actor/listWriters.do");
		result.addObject("targetURL", "/actor/listWriters.do");
		this.configValues(result);

		return result;
	}

	@RequestMapping(value = "/listPublishers", method = RequestMethod.GET)
	public ModelAndView listPublisher() {
		final ModelAndView result = new ModelAndView("actor/list");

		result.addObject("actors", this.publisherService.findAll());
		result.addObject("rolView", "PUBLISHER");
		result.addObject("requestURI", "actor/listPublishers.do");
		result.addObject("targetURL", "/actor/listPublishers.do");

		this.configValues(result);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {

		ModelAndView res;

		res = this.createModelAndViewEditActor();

		return res;

	}

	protected ModelAndView createModelAndViewDisplay(final String targetURL) {
		final ModelAndView result = new ModelAndView("actor/display");

		final UserAccount principal = LoginService.getPrincipal();

		final Actor actor = this.actorService.findByUserAccount(principal);

		result.addObject("actor", actor);
		result.addObject("userLogged", principal);

		final Authority authority = AuthorityMethods.getLoggedAuthority();

		result.addObject("authority", authority.getAuthority());

		final List<SocialProfile> socialProfiles = (List<SocialProfile>) this.socialProfileService.findAllSocialProfiles();

		if (authority.equals("BAN"))
			authority.setAuthority(this.actorService.checkAuthorityIsBanned(LoginService.getPrincipal()));

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
		result.addObject("targetURL", targetURL);
		this.configValues(result);
		return result;

	}

	protected ModelAndView createModelAndViewEditActor() {
		ModelAndView result;

		final Authority authority = AuthorityMethods.getLoggedAuthority();
		if (authority.equals("BAN"))
			authority.setAuthority(this.actorService.checkAuthorityIsBanned(LoginService.getPrincipal()));

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
			this.setCreditCardMakes(result);
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
			this.setCreditCardMakes(result);
			break;

		case "SPONSOR":
			final Sponsor sponsor = this.sponsorService.findByPrincipal(LoginService.getPrincipal());
			result = new ModelAndView("sponsor/edit");
			result.addObject("sponsor", sponsor);
			result.addObject("edit", true);
			this.setCreditCardMakes(result);
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
		List<Message> messages = null;
		List<SocialProfile> socialProfiles = null;

		final UserAccount principal = LoginService.getPrincipal();
		String authority = AuthorityMethods.getLoggedAuthority().getAuthority();

		if (authority.equals("BAN"))
			authority = this.actorService.checkAuthorityIsBanned(principal);

		result.addObject("authority", authority);

		switch (authority) {
		case "ADMINISTRATOR":

			final Administrator administrator = this.administratorService.findByPrincipal(principal);
			messages = (List<Message>) this.messageService.findAllByActor(administrator.getId());
			socialProfiles = (List<SocialProfile>) this.socialProfileService.findAllSocialProfiles(administrator.getId());

			result.addObject("administrator", administrator);

			break;

		case "WRITER":

			final Writer writer = this.writerService.findByPrincipal(LoginService.getPrincipal());
			socialProfiles = (List<SocialProfile>) this.socialProfileService.findAllSocialProfiles(writer.getId());
			messages = (List<Message>) this.messageService.findAllByActor(writer.getId());
			final Collection<Book> books = this.bookService.getAllVisibleBooksOfWriter(writer.getId());
			final Collection<Chapter> chapters = this.chapterService.getAllChaptersOfWriter(writer.getId());

			final Collection<Participation> participations = this.participationService.getParticipationsOfWriter(writer.getId());

			final Collection<Announcement> announcements = this.announcementService.findAllWriter(writer.getId());

			result.addObject("writer", writer);

			result.addObject("books", books);
			result.addObject("chapters", chapters);
			result.addObject("participations", participations);
			result.addObject("announcements", announcements);

			break;
		case "READER":

			final Reader reader = this.readerService.findByPrincipal(LoginService.getPrincipal());
			final Collection<Opinion> opinions = this.opinionService.findOpinionsByReader(reader.getId());
			messages = (List<Message>) this.messageService.findAllByActor(reader.getId());
			socialProfiles = (List<SocialProfile>) this.socialProfileService.findAllSocialProfiles(reader.getId());

			result.addObject("opinions", opinions);
			result.addObject("reader", reader);
			break;
		case "SPONSOR":
			final Sponsor sponsor = this.sponsorService.findByPrincipal(LoginService.getPrincipal());
			socialProfiles = (List<SocialProfile>) this.socialProfileService.findAllSocialProfiles(sponsor.getId());
			this.socialProfileService.delete(socialProfiles);
			messages = (List<Message>) this.messageService.findAllByActor(sponsor.getId());
			final Collection<Sponsorship> sponsorships = this.sponsorshipService.findAllBySponsor(sponsor.getId());

			result.addObject("sponsor", sponsor);
			result.addObject("sponsorships", sponsorships);

			break;

		case "PUBLISHER":
			final Publisher publisher = this.publisherService.findByPrincipal(LoginService.getPrincipal());
			socialProfiles = (List<SocialProfile>) this.socialProfileService.findAllSocialProfiles(publisher.getId());
			final Collection<Contest> contests = this.contestService.getContestsOfPublisher(publisher.getId());
			final Collection<Book> booksPublisher = this.bookService.getAllVisibleBooksOfPublisher(publisher.getId());
			final Collection<Chapter> chaptersPublisher = this.chapterService.getAllChaptersOfPublisher(booksPublisher);
			messages = (List<Message>) this.messageService.findAllByActor(publisher.getId());

			result.addObject("contests", contests);
			result.addObject("books", booksPublisher);
			result.addObject("chapters", chaptersPublisher);

			result.addObject("publisher", publisher);

			break;
		}
		result.addObject("messages", messages);
		result.addObject("socialProfiles", socialProfiles);

		this.configValues(result);
		return result;

	}
	@RequestMapping(value = "/saveData", method = RequestMethod.GET)
	public ModelAndView fileJSON() throws JsonProcessingException, ParseException {
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
