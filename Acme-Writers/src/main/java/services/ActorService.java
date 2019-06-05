
package services;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;
import utiles.AuthorityMethods;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import domain.Actor;
import domain.Administrator;
import domain.Announcement;
import domain.Book;
import domain.Chapter;
import domain.Contest;
import domain.CreditCard;
import domain.Message;
import domain.Opinion;
import domain.Participation;
import domain.Publisher;
import domain.Reader;
import domain.SocialProfile;
import domain.Sponsor;
import domain.Sponsorship;
import domain.Writer;

@Service
@Transactional
public class ActorService {

	@Autowired
	private ActorRepository			actorRepository;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private PublisherService		publisherService;

	@Autowired
	private ReaderService			readerService;

	@Autowired
	private SponsorService			sponsorService;

	@Autowired
	private WriterService			writerService;

	@Autowired
	private SocialProfileService	socialProfileService;

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
	private MessageService			messageService;

	@Autowired
	private AdminConfigService		adminConfigService;

	@Autowired
	private UserAccountRepository	userAccountRepository;

	@Autowired
	private ParticipationService	participationService;

	@Autowired
	private OpinionService			opinionService;


	public Actor save(final Actor actor) {
		return this.actorRepository.save(actor);
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

	public Collection<Actor> findAll() {
		return this.actorRepository.findAll();
	}

	// Workaround for the problem of hibernate with inheritances
	public Actor getActor(final int idActor) {
		return this.actorRepository.getActor(idActor);
	}

	public void updateSpam() {
		AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR");
		final Double spammerPercentaje = this.adminConfigService.getAdminConfig().getSpammerPercentage() / 100.0;
		for (final Actor a : this.actorRepository.findAll()) {
			final Collection<Message> allMessages = this.messageService.findAllByActor(a.getId());
			if (allMessages.size() > 0) {
				final Integer totalMessages = allMessages.size();
				final Integer spamMessages = this.messageService.getSpamMessages(allMessages);
				final boolean spammer = spamMessages >= spammerPercentaje * totalMessages;
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

		if (this.publisherService.findOne(actor.getId()) != null) {
			authority.setAuthority(Authority.PUBLISHER);
			authorities.add(authority);
		} else if (this.readerService.findOne(actor.getId()) != null) {
			authority.setAuthority(Authority.READER);
			authorities.add(authority);
		} else if (this.administratorService.findOne(actor.getId()) != null) {
			authority.setAuthority(Authority.ADMINISTRATOR);
			authorities.add(authority);
		} else if (this.sponsorService.findOne(actor.getId()) != null) {
			authority.setAuthority(Authority.SPONSOR);
			authorities.add(authority);
		} else if (this.writerService.findOne(actor.getId()) != null) {
			authority.setAuthority(Authority.WRITER);
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

	public String exportData() throws JsonProcessingException, ParseException {
		final ObjectMapper mapper = new ObjectMapper();
		final List<Message> messages;
		List<SocialProfile> socialProfiles;

		final StringBuilder json = new StringBuilder();

		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		final UserAccount principal = LoginService.getPrincipal();
		String authority = AuthorityMethods.getLoggedAuthority().getAuthority();

		if (authority.equals("BAN"))
			authority = this.checkAuthorityIsBanned(principal);

		switch (authority) {
		case "ADMINISTRATOR":

			final Administrator administrator = this.administratorService.findByPrincipal(principal);
			messages = (List<Message>) this.messageService.findAllByActor(administrator.getId());
			socialProfiles = (List<SocialProfile>) this.socialProfileService.findAllSocialProfiles(administrator.getId());

			json.append(mapper.writeValueAsString(administrator));
			json.append(mapper.writeValueAsString(messages));
			json.append(mapper.writeValueAsString(socialProfiles));

			break;

		case "WRITER":

			final Writer writer = this.writerService.findByPrincipal(LoginService.getPrincipal());
			socialProfiles = (List<SocialProfile>) this.socialProfileService.findAllSocialProfiles(writer.getId());

			json.append(mapper.writeValueAsString(writer));

			final Collection<Book> books = this.bookService.getAllVisibleBooksOfWriter(writer.getId());
			for (final Book book : books) {
				json.append(mapper.writeValueAsString(book));

				final Collection<Chapter> chapters = this.chapterService.getChaptersOfABook(book.getId());
				for (final Chapter chapter : chapters)
					json.append(mapper.writeValueAsString(chapter));
			}

			final Collection<Participation> participations = this.participationService.getParticipationsOfWriter(writer.getId());
			for (final Participation p : participations)
				json.append(mapper.writeValueAsString(p));

			final Collection<Announcement> announcements = this.announcementService.findAllWriter(writer.getId());
			for (final Announcement ann : announcements)
				json.append(mapper.writeValueAsString(ann));
			json.append(mapper.writeValueAsString(socialProfiles));

			break;
		case "READER":

			final Reader reader = this.readerService.findByPrincipal(LoginService.getPrincipal());
			json.append(mapper.writeValueAsString(reader));
			final Collection<Opinion> opinions = this.opinionService.findOpinionsByReader(reader.getId());
			for (final Opinion o : opinions)
				json.append(mapper.writeValueAsString(o));

			socialProfiles = (List<SocialProfile>) this.socialProfileService.findAllSocialProfiles(reader.getId());
			json.append(mapper.writeValueAsString(socialProfiles));

			break;
		case "SPONSOR":
			final Sponsor sponsor = this.sponsorService.findByPrincipal(LoginService.getPrincipal());
			json.append(mapper.writeValueAsString(sponsor));
			socialProfiles = (List<SocialProfile>) this.socialProfileService.findAllSocialProfiles(sponsor.getId());
			this.socialProfileService.delete(socialProfiles);

			final Collection<Sponsorship> sponsorships = this.sponsorshipService.findAllBySponsor(sponsor.getId());
			for (final Sponsorship s : sponsorships)
				json.append(mapper.writeValueAsString(s));
			json.append(mapper.writeValueAsString(socialProfiles));

			break;

		case "PUBLISHER":
			final Publisher publisher = this.publisherService.findByPrincipal(LoginService.getPrincipal());
			socialProfiles = (List<SocialProfile>) this.socialProfileService.findAllSocialProfiles(publisher.getId());
			json.append(mapper.writeValueAsString(publisher));
			final Collection<Contest> contests = this.contestService.getContestsOfPublisher(publisher.getId());
			for (final Contest c : contests)
				json.append(mapper.writeValueAsString(c));

			final Collection<Book> booksPublisher = this.bookService.getAllVisibleBooksOfPublisher(publisher.getId());
			for (final Book book : booksPublisher) {
				json.append(mapper.writeValueAsString(book));

				final Collection<Chapter> chapters = this.chapterService.getChaptersOfABook(book.getId());
				for (final Chapter chapter : chapters)
					json.append(mapper.writeValueAsString(chapter));
			}

			json.append(mapper.writeValueAsString(socialProfiles));

			break;
		}
		return json.toString();
	}
	public void deleteData() throws ParseException {

		final UserAccount principal = LoginService.getPrincipal();
		String authority = AuthorityMethods.getLoggedAuthority().getAuthority();

		if (authority.equals("BAN"))
			authority = this.checkAuthorityIsBanned(principal);

		try {
			switch (authority) {
			case "ADMINISTRATOR":

				final Administrator anonymousAdmin = this.anonymizeAdmin(this.administratorService.findByPrincipal(principal));
				this.administratorService.save(anonymousAdmin);

				break;

			case "WRITER":

				final Writer anonymousWriter = this.anonymizeWriter(this.writerService.findByPrincipal(principal));
				this.writerService.save(anonymousWriter);

				break;

			case "READER":

				final Reader anonymousReader = this.anonymizeReader(this.readerService.findByPrincipal(principal));
				this.readerService.save(anonymousReader);

				break;
			case "SPONSOR":
				final Sponsor anonymousSponsor = this.anonymizeSponsor(this.sponsorService.findByPrincipal(principal));
				this.sponsorService.save(anonymousSponsor);
				break;

			case "PUBLISHER":
				final Publisher anonymousPublisher = this.anonymizePublisher(this.publisherService.findByPrincipal(principal));
				this.publisherService.save(anonymousPublisher);
				break;
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public String checkAuthorityIsBanned(final UserAccount userAccount) {
		String res = "";
		final Actor actor = this.findByUserAccount(userAccount);

		if (this.writerService.findOne(actor.getId()) != null)
			res = "WRITER";
		else if (this.readerService.findOne(actor.getId()) != null)
			res = "READER";
		else if (this.administratorService.findOne(actor.getId()) != null)
			res = "ADMINISTRATOR";
		else if (this.publisherService.findOne(actor.getId()) != null)
			res = "PUBLISHER";
		else if (this.sponsorService.findOne(actor.getId()) != null)
			res = "SPONSOR";

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

	private String generatePassword() {
		final SecureRandom random = new SecureRandom();
		final String text = new BigInteger(130, random).toString(32);
		return text;
	}

	private Administrator anonymizeAdmin(final Administrator actor) {

		actor.setName("anonymous");
		actor.setPhotoURL(null);
		actor.setEmail("---");
		actor.setPhoneNumber("----");
		actor.setAddress("---");
		actor.setBanned(false);
		actor.setSpammer(null);
		actor.setSurname(".....");

		actor.setUserAccount(this.anonymizeUserAccount(actor.getUserAccount()));

		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findAllSocialProfiles(actor.getId());
		this.socialProfileService.delete(socialProfiles);

		final UserAccount antique = actor.getUserAccount();
		final UserAccount ua = this.userAccountRepository.findByUsername("anonymous");

		actor.setUserAccount(ua);
		this.userAccountRepository.delete(antique);

		return actor;
	}
	private Writer anonymizeWriter(final Writer actor) throws ParseException {

		actor.setName("anonymous");
		actor.setPhotoURL(null);
		actor.setEmail("---");
		actor.setPhoneNumber("----");
		actor.setAddress("---");
		actor.setBanned(false);
		actor.setSpammer(null);
		actor.setSurname(".....");

		actor.setUserAccount(this.anonymizeUserAccount(actor.getUserAccount()));

		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findAllSocialProfiles(actor.getId());
		this.socialProfileService.delete(socialProfiles);

		this.anonymizeCreditCard(actor.getCreditCard());

		final Collection<Book> books = this.bookService.getAllVisibleBooksOfWriter(actor.getId());
		for (final Book book : books) {
			book.setTitle("Anonymous Book");
			book.setCover(null);
			book.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
			book.setCancelled(true);

			final Collection<Chapter> chapters = this.chapterService.getChaptersOfABook(book.getId());
			for (final Chapter chapter : chapters) {
				chapter.setTitle("Lorem ipsum dolor");
				chapter
					.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
				this.chapterService.saveAnonymize(chapter);
				this.chapterService.flush();
			}

			this.bookService.saveAnonimize(book);
			this.bookService.flush();
		}

		final Collection<Participation> participations = this.participationService.getParticipationsOfWriter(actor.getId());
		for (final Participation p : participations) {
			p.setComment("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
			this.participationService.saveAnonimize(p);
		}
		final Collection<Announcement> announcements = this.announcementService.findAllWriter(actor.getId());
		for (final Announcement ann : announcements)
			this.announcementService.delete(ann);

		final UserAccount antique = actor.getUserAccount();
		final UserAccount ua = this.userAccountRepository.findByUsername("anonymous");

		actor.setUserAccount(ua);
		this.userAccountRepository.delete(antique);

		return actor;
	}
	private Reader anonymizeReader(final Reader actor) {

		actor.setName("anonymous");
		actor.setPhotoURL(null);
		actor.setEmail("---");
		actor.setPhoneNumber("----");
		actor.setAddress("---");
		actor.setBanned(false);
		actor.setSpammer(null);
		actor.setSurname(".....");

		actor.setUserAccount(this.anonymizeUserAccount(actor.getUserAccount()));

		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findAllSocialProfiles(actor.getId());
		this.socialProfileService.delete(socialProfiles);

		final Collection<Opinion> opinions = this.opinionService.findOpinionsByReader(actor.getId());
		for (final Opinion o : opinions) {
			o.setReview("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
			this.opinionService.save(o);
		}

		final UserAccount antique = actor.getUserAccount();
		final UserAccount ua = this.userAccountRepository.findByUsername("anonymous");

		actor.setUserAccount(ua);
		this.userAccountRepository.delete(antique);

		return actor;
	}

	private Sponsor anonymizeSponsor(final Sponsor actor) {

		actor.setName("anonymous");
		actor.setPhotoURL(null);
		actor.setEmail("---");
		actor.setPhoneNumber("----");
		actor.setAddress("---");
		actor.setBanned(false);
		actor.setSpammer(null);

		actor.setCompanyName("noCompany");
		actor.setSurname(".....");

		actor.setUserAccount(this.anonymizeUserAccount(actor.getUserAccount()));

		this.anonymizeCreditCard(actor.getCreditCard());

		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findAllSocialProfiles(actor.getId());
		this.socialProfileService.delete(socialProfiles);

		final Collection<Sponsorship> sponsorships = this.sponsorshipService.findAllBySponsor(actor.getId());
		for (final Sponsorship s : sponsorships) {
			this.sponsorshipService.delete(s);
			this.socialProfileService.flush();
		}

		final UserAccount antique = actor.getUserAccount();
		final UserAccount ua = this.userAccountRepository.findByUsername("anonymous");

		actor.setUserAccount(ua);
		this.userAccountRepository.delete(antique);

		return actor;
	}

	private Publisher anonymizePublisher(final Publisher actor) throws ParseException {

		actor.setName("anonymous");
		actor.setPhotoURL(null);
		actor.setEmail("---");
		actor.setPhoneNumber("----");
		actor.setAddress("---");
		actor.setBanned(false);
		actor.setSpammer(null);

		actor.setCommercialName("anonymousCompany");

		actor.setUserAccount(this.anonymizeUserAccount(actor.getUserAccount()));

		this.anonymizeCreditCard(actor.getCreditCard());

		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findAllSocialProfiles(actor.getId());
		this.socialProfileService.delete(socialProfiles);

		final Collection<Contest> contests = this.contestService.getContestsOfPublisher(actor.getId());
		for (final Contest c : contests) {
			c.setTitle("NONE");
			c.setPrize("LOREM IPSUM DOLOR");
			c.setDescription("Lorem ipsum dolor");
			c.setRules(null);
			this.contestService.saveAnonymize(c);
			this.contestService.flush();
		}

		final UserAccount antique = actor.getUserAccount();
		final UserAccount ua = this.userAccountRepository.findByUsername("anonymous");

		actor.setUserAccount(ua);
		this.userAccountRepository.delete(antique);

		return actor;
	}

	public void flush() {
		this.actorRepository.flush();
	}

}
