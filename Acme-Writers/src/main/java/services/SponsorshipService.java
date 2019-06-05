
package services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SponsorshipRepository;
import security.LoginService;
import utiles.AuthorityMethods;
import utiles.ValidateCreditCard;
import domain.Actor;
import domain.AdminConfig;
import domain.Sponsorship;
import forms.SponsorshipForm;

@Service
@Transactional
public class SponsorshipService {

	@Autowired
	private SponsorshipRepository	sponsorshipRepository;

	@Autowired
	private AdminConfigService		adminConfigService;

	@Autowired
	private SponsorService			sponsorService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private ContestService			contestService;

	@Autowired
	private Validator				validator;


	public Sponsorship create() {
		return new Sponsorship();
	}

	public void save(final Sponsorship sponsorship) {
		Assert.isTrue(LoginService.getPrincipal().equals(sponsorship.getSponsor().getUserAccount()));

		if (!(sponsorship.getContests().contains(null))) //Esto es porque el contest de la vista se lo trae como un [null]
			Assert.isTrue(this.contestService.getAllContestMinusAnonymous().containsAll(sponsorship.getContests()));

		if (sponsorship.getId() == 0)
			Assert.isTrue(!ValidateCreditCard.isCaducate(sponsorship.getSponsor().getCreditCard()));

		this.sponsorshipRepository.save(sponsorship);
	}
	public Collection<Sponsorship> findAllBySponsor(final int idSponsor) {
		Assert.isTrue(this.sponsorService.findOne(idSponsor).getUserAccount().equals(LoginService.getPrincipal()));
		return this.sponsorshipRepository.findAllBySponsor(idSponsor);
	}

	public Sponsorship findOne(final int idSponsorship) {
		final Sponsorship sponsorship = this.sponsorshipRepository.findOne(idSponsorship);
		Assert.isTrue(LoginService.getPrincipal().equals(sponsorship.getSponsor().getUserAccount()));
		return sponsorship;
	}

	public Sponsorship reconstruct(final SponsorshipForm sponsorshipForm, final BindingResult binding) {
		Sponsorship result;

		if (sponsorshipForm.getId() == 0) {
			result = this.create();
			result.setSponsor(this.sponsorService.findByPrincipal(LoginService.getPrincipal()));
			result.setFlatRateApplied(0.0);
			result.setCancelled(false);
			result.setViews(0);
		} else
			result = this.findOne(sponsorshipForm.getId());

		result.setBannerURL(sponsorshipForm.getBannerURL());
		result.setTargetPageURL(sponsorshipForm.getTargetPageURL());
		result.setContests(sponsorshipForm.getContests());

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

	public Sponsorship getRandomOfAContest(final int idContest) {
		Sponsorship res = null;
		final List<Sponsorship> sponsorships = this.findAllByContest(idContest);

		if (sponsorships.size() != 0) {
			final int index = ThreadLocalRandom.current().nextInt(sponsorships.size());
			res = sponsorships.get(index);

			Integer views = res.getViews();
			views++;
			res.setViews(views);

			Double flatRateApplied = res.getFlatRateApplied();
			flatRateApplied = flatRateApplied + this.adminConfigService.getAdminConfig().getFlatRate();
			res.setFlatRateApplied(flatRateApplied);

			this.sponsorshipRepository.save(res);
		}

		return res;
	}

	public List<Sponsorship> findAllByContest(final int idContest) {
		return this.sponsorshipRepository.findAllByContest(idContest);
	}

	public void delete(final Sponsorship sponsorship) {
		Assert.isTrue(LoginService.getPrincipal().equals(sponsorship.getSponsor().getUserAccount()));
		this.sponsorshipRepository.delete(sponsorship);
	}

	public Double calculateFlateRateVAT(final int idSponsorship) {
		final AdminConfig adminConfig = this.adminConfigService.getAdminConfig();

		final Double flatRate;
		if (idSponsorship == 0)
			flatRate = adminConfig.getFlatRate();
		else
			flatRate = this.findOne(idSponsorship).getFlatRateApplied();

		final Double flatRateWithVAT = flatRate + flatRate * (adminConfig.getVAT() / 100);

		return flatRateWithVAT;
	}

	public void flush() {
		this.sponsorshipRepository.flush();
	}

	public void cancelSponsorshipCaducate() throws ParseException {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));

		final Collection<Actor> sponsors = this.sponsorService.findSponsorsWithExpiredCreditCard();
		final Collection<Actor> recipients = new ArrayList<>();

		for (final Actor actor : sponsors) {
			final Collection<Sponsorship> sponsorships = this.sponsorshipRepository.findAllBySponsor(actor.getId());
			if (!sponsorships.isEmpty()) {
				recipients.add(actor);
				for (final Sponsorship sponsorship : sponsorships)
					sponsorship.setCancelled(true);
				this.sponsorshipRepository.save(sponsorships);
			}
		}

		if (recipients.size() != 0)
			this.messageService.notifySponsorshipCancelled(recipients);
	}

	public Collection<Sponsorship> save(final Collection<Sponsorship> sponsorships) {
		return this.sponsorshipRepository.save(sponsorships);
	}

	public Collection<Sponsorship> getSponsorshipsOfContest(final int idContest) {
		return this.sponsorshipRepository.getSponsorshipsOfContest(idContest);
	}
}
