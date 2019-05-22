
package services;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repositories.ActorRepository;
import security.LoginService;
import security.UserAccount;
import utiles.AuthorityMethods;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import domain.Actor;
import domain.Administrator;
import domain.CreditCard;
import domain.Message;
import domain.Publisher;
import domain.Reader;
import domain.SocialProfile;
import domain.Sponsor;
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

	public String exportData() throws JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
		final List<Message> messages;
		final List<SocialProfile> socialProfiles;

		final String json = "";

		final UserAccount principal = LoginService.getPrincipal();
		String authority = AuthorityMethods.getLoggedAuthority().getAuthority();

		if (authority.equals("BAN"))
			authority = this.checkAuthorityIsBanned(principal);

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

		actor.setUserAccount(this.anonymizeUserAccount(actor.getUserAccount()));

		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findAllSocialProfiles(actor.getId());
		this.socialProfileService.delete(socialProfiles);
		return actor;
	}

	private Writer anonymizeWriter(final Writer actor) {
		//TODO: añadir los objetos que falten

		actor.setName("anonymous");
		actor.setPhotoURL(null);
		actor.setEmail("---");
		actor.setPhoneNumber("----");
		actor.setAddress("---");
		actor.setBanned(false);
		actor.setSpammer(null);

		actor.setUserAccount(this.anonymizeUserAccount(actor.getUserAccount()));

		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findAllSocialProfiles(actor.getId());
		this.socialProfileService.delete(socialProfiles);
		return actor;
	}

	private Reader anonymizeReader(final Reader actor) {
		//TODO: añadir los objetos que falten

		actor.setName("anonymous");
		actor.setPhotoURL(null);
		actor.setEmail("---");
		actor.setPhoneNumber("----");
		actor.setAddress("---");
		actor.setBanned(false);
		actor.setSpammer(null);

		actor.setUserAccount(this.anonymizeUserAccount(actor.getUserAccount()));

		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findAllSocialProfiles(actor.getId());
		this.socialProfileService.delete(socialProfiles);
		return actor;
	}

	private Sponsor anonymizeSponsor(final Sponsor actor) {
		//TODO: añadir los objetos que falten

		actor.setName("anonymous");
		actor.setPhotoURL(null);
		actor.setEmail("---");
		actor.setPhoneNumber("----");
		actor.setAddress("---");
		actor.setBanned(false);
		actor.setSpammer(null);

		actor.setUserAccount(this.anonymizeUserAccount(actor.getUserAccount()));

		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findAllSocialProfiles(actor.getId());
		this.socialProfileService.delete(socialProfiles);
		return actor;
	}

	private Publisher anonymizePublisher(final Publisher actor) {
		//TODO: añadir los objetos que falten

		actor.setName("anonymous");
		actor.setPhotoURL(null);
		actor.setEmail("---");
		actor.setPhoneNumber("----");
		actor.setAddress("---");
		actor.setBanned(false);
		actor.setSpammer(null);

		actor.setUserAccount(this.anonymizeUserAccount(actor.getUserAccount()));

		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findAllSocialProfiles(actor.getId());
		this.socialProfileService.delete(socialProfiles);
		return actor;
	}

}
