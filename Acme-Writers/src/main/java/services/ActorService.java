
package services;

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
import domain.Actor;
import domain.Administrator;
import domain.Message;
import domain.Publisher;
import domain.SocialProfile;
import domain.Sponsor;

@Service
@Transactional
public class ActorService {

	@Autowired
	private ActorRepository	actorRepository;


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

}
