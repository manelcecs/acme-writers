
package services;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import repositories.AnswerRepository;
import repositories.ItemRepository;
import repositories.PositionRepository;
import repositories.ProblemRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;
import utiles.AuthorityMethods;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import domain.Actor;
import domain.Administrator;
import domain.Answer;
import domain.Application;
import domain.Audit;
import domain.Auditor;
import domain.Company;
import domain.CreditCard;
import domain.Curricula;
import domain.EducationData;
import domain.Item;
import domain.Message;
import domain.MiscellaneousData;
import domain.PersonalData;
import domain.Position;
import domain.PositionData;
import domain.Problem;
import domain.Provider;
import domain.Rookie;
import domain.SocialProfile;

@Service
@Transactional
public class ActorService {

	@Autowired
	private ActorRepository				actorRepository;

	@Autowired
	private UserAccountRepository		userAccountRepository;

	@Autowired
	private MessageService				messageService;

	@Autowired
	private WriterService				companyService;

	@Autowired
	private PublisherService				rookieService;

	@Autowired
	private CurriculaService			curriculaService;

	@Autowired
	private MiscellaneousDataService	miscellaneousDataService;

	@Autowired
	private PersonalDataService			personalDataService;

	@Autowired
	private EducationDataService		educationDataService;

	@Autowired
	private PositionDataService			positionDataService;

	@Autowired
	private PositionService				positionService;

	@Autowired
	private AdministratorService		administratorService;

	@Autowired
	private SocialProfileService		socialProfileService;

	@Autowired
	private FinderService				finderService;

	@Autowired
	private PositionRepository			positionRepository;

	@Autowired
	private AnswerRepository			answerRepository;

	@Autowired
	private ProblemRepository			problemRepository;

	@Autowired
	private ApplicationService			applicationService;

	@Autowired
	private SponsorService				auditorService;

	@Autowired
	private AuditService				auditService;

	@Autowired
	private ProviderService				providerService;

	@Autowired
	private ItemService					itemService;

	@Autowired
	private ItemRepository				itemRepository;


	public Actor save(final Actor actor) {
		return this.actorRepository.save(actor);
	}

	public Collection<Actor> findAll() {
		return this.actorRepository.findAll();
	}

	public Actor findOne(final int idActor) {
		return this.actorRepository.findOne(idActor);
	}

	public Actor findByUserAccount(final UserAccount userAccount) {
		return this.actorRepository.getByUserAccount(userAccount.getId());
	}

	public Actor getByMessageBox(final int idBox) {
		return this.actorRepository.getByMessageBox(idBox);
	}

	public Collection<Actor> findNonEliminatedActors() {
		return this.actorRepository.findNonEliminatedActors();
	}

	// Workaround for the problem of hibernate with inheritances
	public Actor getActor(final int idActor) {
		return this.actorRepository.getActor(idActor);
	}

	public void updateSpam() {
		AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR");
		for (final Actor a : this.actorRepository.findAll()) {
			final Collection<Message> allMessages = this.messageService.findAllByActor(a.getId());
			if (allMessages.size() > 0) {
				final Integer totalMessages = allMessages.size();
				final Integer spamMessages = this.messageService.getSpamMessages(allMessages);
				final boolean spammer = spamMessages >= 0.1 * totalMessages;
				if (a.getSpammer() != null) {
					if (a.getSpammer() != spammer) {
						a.setSpammer(spammer);
						this.actorRepository.save(a);
					}
				} else {
					a.setSpammer(spammer);
					this.actorRepository.save(a);
				}
			}
		}
	}

	public void ban(final Actor actor) {
		final UserAccount principal = LoginService.getPrincipal();
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		Assert.isTrue(!(actor.equals(this.findByUserAccount(principal))));
		Assert.isTrue(actor.getSpammer());

		actor.setBanned(true);

		final UserAccount userAccount = actor.getUserAccount();
		userAccount.removeAuthority(userAccount.getAuthorities().iterator().next());
		final Collection<Authority> authorities = new ArrayList<Authority>();
		final Authority auth = new Authority();

		auth.setAuthority(Authority.BAN);
		authorities.add(auth);
		userAccount.setAuthorities(authorities);

		final UserAccount res = this.userAccountRepository.save(userAccount);
		actor.setUserAccount(res);

		this.save(actor);
	}

	public void unban(final Actor actor) {
		final UserAccount principal = LoginService.getPrincipal();
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		Assert.isTrue(!(actor.equals(this.findByUserAccount(principal))));

		actor.setBanned(false);

		final UserAccount userAccount = actor.getUserAccount();
		userAccount.removeAuthority(userAccount.getAuthorities().iterator().next());
		final Collection<Authority> authorities = new ArrayList<Authority>();
		final Authority authority = new Authority();

		if (this.rookieService.findOne(actor.getId()) != null) {
			authority.setAuthority(Authority.ROOKIE);
			authorities.add(authority);
		} else if (this.companyService.findOne(actor.getId()) != null) {
			authority.setAuthority(Authority.COMPANY);
			authorities.add(authority);
		} else if (this.administratorService.findOne(actor.getId()) != null) {
			authority.setAuthority(Authority.ADMINISTRATOR);
			authorities.add(authority);
		} else if (this.providerService.findOne(actor.getId()) != null) {
			authority.setAuthority(Authority.PROVIDER);
			authorities.add(authority);
		} else if (this.auditorService.findOne(actor.getId()) != null) {
			authority.setAuthority(Authority.AUDITOR);
			authorities.add(authority);
		}

		userAccount.setAuthorities(authorities);
		final UserAccount res = this.userAccountRepository.save(userAccount);
		actor.setUserAccount(res);

		this.save(actor);
	}

	public Collection<Actor> getSpammerActors() {
		AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR");
		return this.actorRepository.findSpamActors();
	}

	public String exportData() throws JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
		List<Message> messages;
		List<SocialProfile> socialProfiles;

		String json = "";

		final UserAccount principal = LoginService.getPrincipal();
		String authority = AuthorityMethods.getLoggedAuthority().getAuthority();

		if (authority.equals("BAN"))
			authority = this.checkAuthorityIsBanned(principal);

		switch (authority) {
		case "ADMINISTRATOR":
			final Administrator administrator = this.administratorService.findByPrincipal(principal);
			messages = (List<Message>) this.messageService.findAllByActor(administrator.getId());
			socialProfiles = (List<SocialProfile>) this.socialProfileService.findAllSocialProfiles(administrator.getId());

			json = json + mapper.writeValueAsString(administrator);
			json = json + mapper.writeValueAsString(messages);
			json = json + mapper.writeValueAsString(socialProfiles);
			break;

		case "ROOKIE":
			final Rookie rookie = this.rookieService.findByPrincipal(principal);
			messages = (List<Message>) this.messageService.findAllByActor(rookie.getId());
			socialProfiles = (List<SocialProfile>) this.socialProfileService.findAllSocialProfiles(rookie.getId());
			final List<Curricula> curricula = (List<Curricula>) this.curriculaService.findAllNoCopy(rookie);
			final List<MiscellaneousData> miscellaneousDatas = new ArrayList<>();
			final List<PersonalData> personalDatas = new ArrayList<>();
			final List<PositionData> positionDatas = new ArrayList<>();
			final List<EducationData> educationDatas = new ArrayList<>();

			for (final Curricula cv : curricula) {
				miscellaneousDatas.addAll(this.miscellaneousDataService.findAllCurricula(cv));
				personalDatas.add(this.personalDataService.findByCurricula(cv));
				positionDatas.addAll(this.positionDataService.findAllCurricula(cv));
				educationDatas.addAll(this.educationDataService.findAllCurricula(cv));
			}

			json = json + mapper.writeValueAsString(rookie);
			json = json + mapper.writeValueAsString(socialProfiles);
			json = json + mapper.writeValueAsString(curricula);
			json = json + mapper.writeValueAsString(miscellaneousDatas);
			json = json + mapper.writeValueAsString(personalDatas);
			json = json + mapper.writeValueAsString(positionDatas);
			json = json + mapper.writeValueAsString(educationDatas);
			json = json + mapper.writeValueAsString(messages);
			break;

		case "COMPANY":
			final Company company = this.companyService.findByPrincipal(principal);
			messages = (List<Message>) this.messageService.findAllByActor(company.getId());
			socialProfiles = (List<SocialProfile>) this.socialProfileService.findAllSocialProfiles(company.getId());
			final List<Position> positions = (List<Position>) this.positionService.getAllPositionsFilteredOfCompany(company.getId());

			json = json + mapper.writeValueAsString(company);
			json = json + mapper.writeValueAsString(positions);
			json = json + mapper.writeValueAsString(messages);
			json = json + mapper.writeValueAsString(socialProfiles);

			break;
		case "AUDITOR":
			final Auditor auditor = this.auditorService.findByPrincipal(principal);
			messages = (List<Message>) this.messageService.findAllByActor(auditor.getId());
			socialProfiles = (List<SocialProfile>) this.socialProfileService.findAllSocialProfiles(auditor.getId());
			final List<Audit> audits = (List<Audit>) this.auditService.getAuditsOfAnAuditor(auditor.getId());

			json = json + mapper.writeValueAsString(auditor);
			json = json + mapper.writeValueAsString(audits);
			json = json + mapper.writeValueAsString(messages);
			json = json + mapper.writeValueAsString(socialProfiles);

			break;
		case "PROVIDER":

			final Provider provider = this.providerService.findByPrincipal(principal);
			messages = (List<Message>) this.messageService.findAllByActor(provider.getId());
			socialProfiles = (List<SocialProfile>) this.socialProfileService.findAllSocialProfiles(provider.getId());
			final List<Item> items = (List<Item>) this.itemService.getItemsOfProvider(provider.getId());

			json = json + mapper.writeValueAsString(provider);
			json = json + mapper.writeValueAsString(items);
			json = json + mapper.writeValueAsString(messages);
			json = json + mapper.writeValueAsString(socialProfiles);

			break;
		}
		return json;
	}
	public void deleteData() throws ParseException {

		final UserAccount principal = LoginService.getPrincipal();
		String authority = AuthorityMethods.getLoggedAuthority().getAuthority();

		if (authority.equals("BAN"))
			authority = this.checkAuthorityIsBanned(principal);

		switch (authority) {
		case "ADMINISTRATOR":

			final Administrator anonymousAdmin = this.anonymizeAdmin(this.administratorService.findByPrincipal(principal));
			this.administratorService.save(anonymousAdmin);

			break;

		case "ROOKIE":

			final Rookie anonymousRookie = this.anonymizeRookie(this.rookieService.findByPrincipal(principal));
			this.rookieService.save(anonymousRookie);

			break;

		case "COMPANY":

			final Company anonymousCompany = this.anonymizeCompany(this.companyService.findByPrincipal(principal));
			this.companyService.save(anonymousCompany);

			break;
		case "AUDITOR":
			final Auditor anonymousAuditor = this.anonymizeAuditor(this.auditorService.findByPrincipal(principal));
			this.auditorService.save(anonymousAuditor);
			break;

		case "PROVIDER":
			final Provider anonymousProvider = this.anonymizeProvider(this.providerService.findByPrincipal(principal));
			this.providerService.save(anonymousProvider);
			break;
		}
	}

	//Utiles
	private String generatePassword() {
		final SecureRandom random = new SecureRandom();
		final String text = new BigInteger(130, random).toString(32);
		return text;
	}

	public String checkAuthorityIsBanned(final UserAccount userAccount) {
		String res = "";
		final Actor actor = this.findByUserAccount(userAccount);

		if (this.rookieService.findOne(actor.getId()) != null)
			res = "ROOKIE";
		else if (this.companyService.findOne(actor.getId()) != null)
			res = "COMPANY";
		else if (this.administratorService.findOne(actor.getId()) != null)
			res = "ADMINISTRATOR";
		else if (this.providerService.findOne(actor.getId()) != null)
			res = "PROVIDER";
		else if (this.auditorService.findOne(actor.getId()) != null)
			res = "AUDITOR";

		return res;
	}

	private CreditCard anonymizeCreditCard(final CreditCard creditCard) {
		creditCard.setCvv(999);
		creditCard.setExpirationMonth(1);
		creditCard.setExpirationYear(0);
		creditCard.setHolder("---");
		creditCard.setMake("---");
		creditCard.setNumber("4636711719209732");
		return creditCard;
	}

	private UserAccount anonymizeUserAccount(final UserAccount userAccount) {
		userAccount.setAuthorities(null);
		userAccount.setPassword(this.generatePassword());
		return userAccount;
	}

	private List<String> anonymizeSurnames() {
		final List<String> surname = new ArrayList<>();
		surname.add("Anonymous");
		return surname;
	}

	private Administrator anonymizeAdmin(final Administrator admin) {
		admin.setName("anonymous");
		admin.setSurnames(this.anonymizeSurnames());
		admin.setVatNumber("----");
		admin.setPhoto(null);
		admin.setEmail("---");
		admin.setPhoneNumber("----");
		admin.setAddress("---");
		admin.setBanned(false);
		admin.setSpammer(null);

		admin.setUserAccount(this.anonymizeUserAccount(admin.getUserAccount()));
		admin.setCreditCard(this.anonymizeCreditCard(admin.getCreditCard()));

		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findAllSocialProfiles(admin.getId());
		this.socialProfileService.delete(socialProfiles);
		return admin;
	}

	private Rookie anonymizeRookie(final Rookie rookie) throws ParseException {
		rookie.setName("anonymous");
		rookie.setSurnames(this.anonymizeSurnames());
		rookie.setVatNumber("----");
		rookie.setPhoto(null);
		rookie.setEmail("---");
		rookie.setPhoneNumber("----");
		rookie.setAddress("---");
		rookie.setBanned(false);
		rookie.setSpammer(null);

		this.finderService.clear(rookie.getFinder());

		rookie.setUserAccount(this.anonymizeUserAccount(rookie.getUserAccount()));
		rookie.setCreditCard(this.anonymizeCreditCard(rookie.getCreditCard()));

		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findAllSocialProfiles(rookie.getId());
		this.socialProfileService.delete(socialProfiles);

		//Eliminar originales
		final Collection<Curricula> originalCurricula = this.curriculaService.findAllNoCopy(rookie);

		for (final Curricula cv : originalCurricula) {
			this.miscellaneousDataService.delete(this.miscellaneousDataService.findAllCurricula(cv));
			this.personalDataService.delete(this.personalDataService.findByCurricula(cv));
			this.positionDataService.delete(this.positionDataService.findAllCurricula(cv));
			this.educationDataService.delete(this.educationDataService.findAllCurricula(cv));
		}

		this.curriculaService.delete(originalCurricula);

		//Anonimizar las copias
		final Collection<Curricula> copyCurricula = this.curriculaService.findAllCopy(rookie);

		for (final Curricula cv : copyCurricula) {
			cv.setTitle("anonymous");
			final PersonalData personalData = this.personalDataService.findByCurricula(cv);
			personalData.setFullName("anonymous");
			personalData.setGitHubProfile("http://anonymous.com");
			personalData.setLinkedinProfile("http://anonymous.com");
			personalData.setPhoneNumber("anonymous");
			personalData.setStatement("anonymous");

			this.miscellaneousDataService.delete(this.miscellaneousDataService.findAllCurricula(cv));
			this.positionDataService.delete(this.positionDataService.findAllCurricula(cv));
			this.educationDataService.delete(this.educationDataService.findAllCurricula(cv));
		}

		//Anonimizar answers
		final Collection<Application> applications = this.applicationService.getApplicationOfRookie(rookie.getId());

		Answer answer;

		for (final Application application : applications) {
			answer = this.answerRepository.getAnswerOfApplication(application.getId());

			answer.setExplanation("------------------");
			answer.setLink("http://any.com");

			this.answerRepository.save(answer);

		}

		return rookie;
	}

	private Company anonymizeCompany(final Company company) {
		company.setUserAccount(this.anonymizeUserAccount(company.getUserAccount()));
		company.setCreditCard(this.anonymizeCreditCard(company.getCreditCard()));

		company.setName("anonymous");
		company.setSurnames(this.anonymizeSurnames());
		company.setVatNumber("----");
		company.setPhoto(null);
		company.setEmail("---");
		company.setPhoneNumber("----");
		company.setAddress("---");
		company.setBanned(false);
		company.setSpammer(null);
		company.setCompanyName("anonymous");

		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findAllSocialProfiles(company.getId());
		this.socialProfileService.delete(socialProfiles);

		//Anonimizar positions

		final Collection<Position> positions = this.positionRepository.getPositionsOfCompany(company.getId());

		for (final Position position : positions) {
			position.setCancelled(true);
			position.setDescription("-----------");
			position.setProfileRequired("-----------");
			position.setSalaryOffered(0.0);
			final Collection<String> anonymousCollection = new ArrayList<>();
			anonymousCollection.add("----------");
			position.setSkillsRequired(anonymousCollection);
			position.setTechnologiesRequired(anonymousCollection);
			position.setTitle("---------------");

			this.positionRepository.save(position);
		}

		final Collection<Problem> problems = this.problemRepository.getProblemsOfCompany(company.getId());

		for (final Problem problem : problems) {
			final Collection<String> anonymousCollection = new ArrayList<>();
			anonymousCollection.add("----------");
			problem.setAttachments(anonymousCollection);
			problem.setHint("----------");
			problem.setTitle("----------");
			problem.setStatement("----------");
			problem.setDraft(true);

			this.problemRepository.save(problem);
		}

		return company;
	}

	private Auditor anonymizeAuditor(final Auditor auditor) {
		auditor.setUserAccount(this.anonymizeUserAccount(auditor.getUserAccount()));
		auditor.setCreditCard(this.anonymizeCreditCard(auditor.getCreditCard()));

		auditor.setName("anonymous");
		auditor.setSurnames(this.anonymizeSurnames());
		auditor.setVatNumber("----");
		auditor.setPhoto(null);
		auditor.setEmail("---");
		auditor.setPhoneNumber("----");
		auditor.setAddress("---");
		auditor.setBanned(false);
		auditor.setSpammer(null);

		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findAllSocialProfiles(auditor.getId());
		this.socialProfileService.delete(socialProfiles);

		final Collection<Audit> audits = this.auditService.getAuditsOfAnAuditor(auditor.getId());

		for (final Audit audit : audits) {

			audit.setDraft(true);
			audit.setMoment(new Date());
			audit.setScore(null);
			audit.setText("----------");

			this.auditService.save(audit);
		}

		return auditor;
	}

	private Provider anonymizeProvider(final Provider provider) {
		provider.setUserAccount(this.anonymizeUserAccount(provider.getUserAccount()));
		provider.setCreditCard(this.anonymizeCreditCard(provider.getCreditCard()));

		provider.setName("anonymous");
		provider.setSurnames(this.anonymizeSurnames());
		provider.setVatNumber("----");
		provider.setPhoto(null);
		provider.setEmail("---");
		provider.setPhoneNumber("----");
		provider.setAddress("---");
		provider.setBanned(false);
		provider.setSpammer(null);
		provider.setProviderMake("anonymous");

		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findAllSocialProfiles(provider.getId());
		this.socialProfileService.delete(socialProfiles);

		final Collection<Item> items = this.itemService.getItemsOfProvider(provider.getId());

		for (final Item item : items)
			this.itemRepository.delete(item);

		return provider;
	}

	public void flush() {
		this.actorRepository.flush();
	}

	public Collection<Actor> findNonEliminatedActorsLessAdmin() {
		return this.actorRepository.findNonEliminatedActorsLessAdmin();
	}
}
