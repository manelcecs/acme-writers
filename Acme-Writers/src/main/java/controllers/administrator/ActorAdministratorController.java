
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.LoginService;
import security.UserAccount;
import services.ActorService;
import services.AdministratorService;
import services.PublisherService;
import services.ReaderService;
import services.SponsorService;
import services.WriterService;
import controllers.AbstractController;
import domain.Actor;
import domain.Administrator;
import domain.Publisher;
import domain.Reader;
import domain.Sponsor;
import domain.Writer;

@Controller
@RequestMapping("/actor")
public class ActorAdministratorController extends AbstractController {

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private WriterService			writerService;

	@Autowired
	private ReaderService			readerService;

	@Autowired
	private SponsorService			sponsorService;

	@Autowired
	private PublisherService		publisherService;


	@RequestMapping(value = "/administrator/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int idActor, @RequestParam final String targetURL) {

		ModelAndView result;

		result = this.createModelAndViewDisplayByAdmin(idActor, targetURL);

		return result;

	}

	protected ModelAndView createModelAndViewDisplayByAdmin(final int idActor, final String targetURL) {

		ModelAndView result = new ModelAndView("actor/display");

		final UserAccount principal = LoginService.getPrincipal();
		result.addObject("userLogged", principal);

		final Authority authority = this.actorService.getActor(idActor).getUserAccount().getAuthorities().iterator().next();

		if (authority.getAuthority().equals("BAN"))
			authority.setAuthority(this.actorService.checkAuthorityIsBanned(this.actorService.getActor(idActor).getUserAccount()));

		result.addObject("authority", authority.getAuthority());

		switch (authority.getAuthority()) {
		case "ADMINISTRATOR":
			final Administrator administrator = this.administratorService.findOne(idActor);
			result.addObject("administrator", administrator);
			break;

		case "WRITER":
			final Writer writer = this.writerService.findOne(idActor);
			result.addObject("writer", writer);
			break;

		case "READER":
			final Reader reader = this.readerService.findOne(idActor);
			result.addObject("reader", reader);
			break;

		case "PUBLISHER":
			final Publisher publisher = this.publisherService.findOne(idActor);
			result.addObject("publisher", publisher);
			break;

		case "SPONSOR":
			final Sponsor sponsor = this.sponsorService.findOne(idActor);
			result.addObject("sponsor", sponsor);
			break;
		default:
			result = new ModelAndView("actor/display.do");
			break;
		}

		result.addObject("targetURL", targetURL);
		this.configValues(result);
		return result;
	}

	@RequestMapping(value = "/administrator/listActors", method = RequestMethod.GET)
	public ModelAndView listActors() {
		final ModelAndView res = new ModelAndView("administrator/listActors");

		final Collection<Actor> actors = this.actorService.findNonEliminatedActors();
		final Collection<Administrator> administrators = this.administratorService.findAll();
		actors.removeAll(administrators);

		res.addObject("actors", actors);
		res.addObject("requestURI", "actor/administrator/listActors.do");

		this.configValues(res);
		return res;

	}

}
