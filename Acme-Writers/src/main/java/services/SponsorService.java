
package services;

import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SponsorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;
import utiles.AddPhoneCC;
import utiles.AuthorityMethods;
import utiles.EmailValidator;
import utiles.ValidateCreditCard;
import domain.Actor;
import domain.Sponsor;
import forms.SponsorForm;

@Service
@Transactional
public class SponsorService {

	@Autowired
	private UserAccountRepository	accountRepository;

	@Autowired
	private AdminConfigService		adminConfigService;

	@Autowired
	private MessageBoxService		messageBoxService;

	@Autowired
	private Validator				validator;

	@Autowired
	private SponsorRepository		sponsorRepository;


	public Sponsor create() {
		final Sponsor res = new Sponsor();

		res.setSpammer(false);
		res.setBanned(false);
		res.setMessageBoxes(this.messageBoxService.initializeNewUserBoxes());

		return res;
	}

	public Sponsor save(final Sponsor auditor) {
		Assert.isTrue(auditor != null);

		if (auditor.getId() == 0) {
			final UserAccount userAccount = auditor.getUserAccount();

			final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			final String pass = encoder.encodePassword(userAccount.getPassword(), null);
			userAccount.setPassword(pass);

			final UserAccount finalAccount = this.accountRepository.save(userAccount);

			auditor.setUserAccount(finalAccount);
		} else {
			Assert.isTrue(AuthorityMethods.checkIsSomeoneLogged());
			Assert.isTrue(AuthorityMethods.chechAuthorityLogged(Authority.SPONSOR));
			Assert.isTrue(!auditor.getBanned());
		}

		final Sponsor res = this.sponsorRepository.save(auditor);

		return res;
	}

	public void flush() {
		this.sponsorRepository.flush();
	}

	public Sponsor findOne(final int auditorId) {
		return this.sponsorRepository.findOne(auditorId);
	}

	public Sponsor findByPrincipal(final UserAccount principal) {
		return this.sponsorRepository.findByPrincipal(principal.getId());
	}

	public Collection<Sponsor> findAll() {
		return this.sponsorRepository.findAll();
	}

	public Sponsor reconstruct(final SponsorForm sponsorForm, final BindingResult binding) {

		if (!EmailValidator.validateEmail(sponsorForm.getEmail(), Authority.SPONSOR))
			binding.rejectValue("email", "sponsor.edit.email.error");
		if (!sponsorForm.getUserAccount().getPassword().equals(sponsorForm.getConfirmPassword()))
			binding.rejectValue("confirmPassword", "sponsor.edit.confirmPassword.error");
		if (this.accountRepository.findByUsername(sponsorForm.getUserAccount().getUsername()) != null)
			binding.rejectValue("userAccount.username", "sponsor.edit.userAccount.username.error");
		if (!sponsorForm.getTermsAndConditions())
			binding.rejectValue("termsAndConditions", "sponsor.edit.termsAndConditions.error");

		sponsorForm.setCreditCard(ValidateCreditCard.checkNumeroAnno(sponsorForm.getCreditCard()));
		ValidateCreditCard.checkGregorianDate(sponsorForm.getCreditCard(), binding);

		final Sponsor result;
		result = this.create();

		final UserAccount account = sponsorForm.getUserAccount();

		final Authority a = new Authority();
		a.setAuthority(Authority.SPONSOR);
		account.addAuthority(a);

		result.setUserAccount(account);
		result.setAddress(sponsorForm.getAddress());
		result.setEmail(sponsorForm.getEmail());
		result.setName(sponsorForm.getName());
		result.setPhoneNumber(AddPhoneCC.addPhoneCC(this.adminConfigService.getAdminConfig().getCountryCode(), sponsorForm.getPhoneNumber()));
		result.setPhotoURL(sponsorForm.getPhotoURL());
		result.setSurname(sponsorForm.getSurname());
		result.setCompanyName(sponsorForm.getCompanyName());

		result.setCreditCard(sponsorForm.getCreditCard());

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

	public Sponsor reconstruct(final Sponsor sponsor, final BindingResult binding) {

		if (!EmailValidator.validateEmail(sponsor.getEmail(), Authority.SPONSOR))
			binding.rejectValue("email", "sponsor.edit.email.error");

		sponsor.setCreditCard(ValidateCreditCard.checkNumeroAnno(sponsor.getCreditCard()));
		ValidateCreditCard.checkGregorianDate(sponsor.getCreditCard(), binding);

		final Sponsor result;
		result = this.findByPrincipal(LoginService.getPrincipal());

		result.setAddress(sponsor.getAddress());
		result.setEmail(sponsor.getEmail());
		result.setName(sponsor.getName());
		result.setPhoneNumber(AddPhoneCC.addPhoneCC(this.adminConfigService.getAdminConfig().getCountryCode(), sponsor.getPhoneNumber()));
		result.setPhotoURL(sponsor.getPhotoURL());
		result.setSurname(sponsor.getSurname());
		result.setCompanyName(sponsor.getCompanyName());

		result.setCreditCard(sponsor.getCreditCard());

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

	//DASHBOARD------------------------------------------------------------
	public Collection<Sponsor> getSponsorsWithMoreSponsorships() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.sponsorRepository.getSponsorsWithMoreSponsorships();
	}

	public Collection<Actor> findSponsorsWithExpiredCreditCard() {
		return this.sponsorRepository.findSponsorsWithExpiredCreditCard();
	}

}
