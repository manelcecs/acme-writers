
package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.parsing.Problem;
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
import services.SponsorService;
import services.WriterService;
import services.ProviderService;
import services.PublisherService;
import services.SocialProfileService;
import utiles.AuthorityMethods;
import domain.Actor;
import domain.Administrator;
import domain.Message;
import domain.SocialProfile;

@Controller
@RequestMapping("/actor")
public class ActorController extends AbstractController {

	@Autowired
	private ActorService				actorService;

	@Autowired
	private PublisherService				rookieService;

	@Autowired
	private WriterService				companyService;

	@Autowired
	private AdministratorService		administratorService;

	@Autowired
	private SocialProfileService		socialProfileService;

	@Autowired
	private MessageService				messageService;

	@Autowired
	private CurriculaService			curriculaService;

	@Autowired
	private MiscellaneousDataService	miscellaneousDataService;

	@Autowired
	private PersonalDataService			personalDataService;

	@Autowired
	private PositionDataService			positionDataService;

	@Autowired
	private EducationDataService		educationDataService;

	@Autowired
	private PositionService				positionService;

	@Autowired
	private AnswerService				answerService;

	@Autowired
	private ProblemService				problemService;

	@Autowired
	private ApplicationService			applicationService;

	@Autowired
	private ProviderService				providerService;

	@Autowired
	private SponsorService				auditorService;

	@Autowired
	private AuditService				auditService;

	@Autowired
	private ItemService					itemService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result;

		result = this.createModelAndViewDisplay();

		return result;
	}

	@RequestMapping(value = "/displayCompany", method = RequestMethod.GET)
	public ModelAndView displayCompany(@RequestParam(required = true) final int idCompany) {
		final ModelAndView result = new ModelAndView("actor/display");

		final Company company = this.companyService.findOne(idCompany);

		result.addObject("authority", "COMPANY");

		final List<SocialProfile> socialProfiles = (List<SocialProfile>) this.socialProfileService.findAllSocialProfiles(company.getId());

		result.addObject("company", company);
		result.addObject("userLogged", null);
		result.addObject("actor", company);

		result.addObject("socialProfiles", socialProfiles);
		this.configValues(result);
		return result;
	}

	@RequestMapping(value = "/displayProvider", method = RequestMethod.GET)
	public ModelAndView displayProvider(@RequestParam(required = true) final int providerId) {
		final ModelAndView result = new ModelAndView("actor/display");

		final Provider provider = this.providerService.findOne(providerId);

		result.addObject("authority", "PROVIDER");

		final List<SocialProfile> socialProfiles = (List<SocialProfile>) this.socialProfileService.findAllSocialProfiles(provider.getId());

		result.addObject("provider", provider);
		result.addObject("userLogged", null);
		result.addObject("actor", provider);

		result.addObject("socialProfiles", socialProfiles);
		this.configValues(result);
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

		case "ROOKIE":
			final Rookie rookie = this.rookieService.findOne(actor.getId());
			result.addObject("rookie", rookie);
			break;

		case "COMPANY":
			final Company company = this.companyService.findOne(actor.getId());
			result.addObject("company", company);
			break;

		case "AUDITOR":
			final Auditor auditor = this.auditorService.findOne(actor.getId());
			result.addObject("auditor", auditor);

			break;

		case "PROVIDER":
			final Provider provider = this.providerService.findOne(actor.getId());
			result.addObject("provider", provider);
			break;
		}

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

		case "ROOKIE":
			final Rookie rookie = this.rookieService.findByPrincipal(LoginService.getPrincipal());
			result = new ModelAndView("rookie/edit");
			result.addObject("rookie", rookie);
			result.addObject("edit", true);
			break;

		case "COMPANY":
			final Company company = this.companyService.findByPrincipal(LoginService.getPrincipal());
			result = new ModelAndView("company/edit");
			result.addObject("company", company);
			result.addObject("edit", true);
			break;

		case "AUDITOR":
			final Auditor auditor = this.auditorService.findByPrincipal(LoginService.getPrincipal());
			result = new ModelAndView("auditor/edit");
			result.addObject("auditor", auditor);
			result.addObject("edit", true);
			break;

		case "PROVIDER":
			final Provider provider = this.providerService.findByPrincipal(LoginService.getPrincipal());
			result = new ModelAndView("provider/edit");
			result.addObject("provider", provider);
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

		case "ROOKIE":
			final Rookie rookie = this.rookieService.findByPrincipal(principal);
			final Collection<Curricula> curricula = this.curriculaService.findAllNoCopy(rookie);
			final Collection<PersonalData> personalDatas = new ArrayList<>();
			final Collection<MiscellaneousData> miscellaneousDatas = new ArrayList<>();
			final Collection<PositionData> positionDatas = new ArrayList<>();
			final Collection<EducationData> educationDatas = new ArrayList<>();
			for (final Curricula cv : curricula) {
				personalDatas.add(this.personalDataService.findByCurricula(cv));
				miscellaneousDatas.addAll(this.miscellaneousDataService.findAllCurricula(cv));
				positionDatas.addAll(this.positionDataService.findAllCurricula(cv));
				educationDatas.addAll(this.educationDataService.findAllCurricula(cv));
			}

			messages = (List<Message>) this.messageService.findAllByActor(rookie.getId());
			result.addObject("rookie", rookie);
			result.addObject("messages", messages);
			result.addObject("personalDatas", personalDatas);
			result.addObject("miscellaneousDatas", miscellaneousDatas);
			result.addObject("positionDatas", positionDatas);
			result.addObject("educationDatas", educationDatas);

			final Collection<Application> applications = this.applicationService.getApplicationOfRookie(rookie.getId());
			result.addObject("applications", applications);

			final Collection<Answer> answers = new ArrayList<>();

			for (final Application application : applications)
				answers.add(this.answerService.getAnswerOfApplication(application.getId()));

			result.addObject("answersOfApplications", answers);

			break;

		case "COMPANY":
			final Company company = this.companyService.findByPrincipal(principal);
			messages = (List<Message>) this.messageService.findAllByActor(company.getId());
			result.addObject("company", company);
			result.addObject("messages", messages);

			final Collection<Position> positions = this.positionService.getPositionsOfCompany(company.getId());
			result.addObject("positions", positions);

			final Collection<Problem> problems = this.problemService.getProblemsOfCompany(company.getId());
			result.addObject("problems", problems);

			break;

		case "AUDITOR":
			final Auditor auditor = this.auditorService.findByPrincipal(LoginService.getPrincipal());
			result.addObject("auditor", auditor);
			messages = (List<Message>) this.messageService.findAllByActor(auditor.getId());
			result.addObject("messages", messages);
			final Collection<Audit> auditions = this.auditService.getAuditsOfAnAuditor(auditor.getId());
			result.addObject("auditions", auditions);
			break;

		case "PROVIDER":
			final Provider provider = this.providerService.findByPrincipal(LoginService.getPrincipal());
			result.addObject("provider", provider);
			messages = (List<Message>) this.messageService.findAllByActor(provider.getId());
			result.addObject("messages", messages);
			final Collection<Item> items = this.itemService.getItemsOfProvider(provider.getId());
			result.addObject("items", items);
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
