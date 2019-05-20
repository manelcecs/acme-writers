
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

	public Sponsor reconstruct(final SponsorForm auditorForm, final BindingResult binding) {

		if (!EmailValidator.validateEmail(auditorForm.getEmail(), Authority.SPONSOR))
			binding.rejectValue("email", "auditor.edit.email.error");
		if (!auditorForm.getUserAccount().getPassword().equals(auditorForm.getConfirmPassword()))
			binding.rejectValue("confirmPassword", "auditor.edit.confirmPassword.error");
		if (this.accountRepository.findByUsername(auditorForm.getUserAccount().getUsername()) != null)
			binding.rejectValue("userAccount.username", "auditor.edit.userAccount.username.error");
		if (!auditorForm.getTermsAndConditions())
			binding.rejectValue("termsAndConditions", "auditor.edit.termsAndConditions.error");

		auditorForm.setCreditCard(ValidateCreditCard.checkNumeroAnno(auditorForm.getCreditCard()));
		ValidateCreditCard.checkGregorianDate(auditorForm.getCreditCard(), binding);

		final Sponsor result;
		result = this.create();

		final UserAccount account = auditorForm.getUserAccount();

		final Authority a = new Authority();
		a.setAuthority(Authority.SPONSOR);
		account.addAuthority(a);

		result.setUserAccount(account);
		result.setAddress(auditorForm.getAddress());
		result.setEmail(auditorForm.getEmail());
		result.setName(auditorForm.getName());
		result.setPhoneNumber(AddPhoneCC.addPhoneCC(this.adminConfigService.getAdminConfig().getCountryCode(), auditorForm.getPhoneNumber()));
		result.setPhotoURL(auditorForm.getPhotoURL());
		result.setSurname(auditorForm.getSurname());

		result.setCreditCard(auditorForm.getCreditCard());

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

	public Sponsor reconstruct(final Sponsor auditor, final BindingResult binding) {

		if (!EmailValidator.validateEmail(auditor.getEmail(), Authority.SPONSOR))
			binding.rejectValue("email", "auditor.edit.email.error");

		auditor.setCreditCard(ValidateCreditCard.checkNumeroAnno(auditor.getCreditCard()));
		ValidateCreditCard.checkGregorianDate(auditor.getCreditCard(), binding);

		final Sponsor result;
		result = this.findByPrincipal(LoginService.getPrincipal());

		result.setAddress(auditor.getAddress());
		result.setEmail(auditor.getEmail());
		result.setName(auditor.getName());
		result.setPhoneNumber(AddPhoneCC.addPhoneCC(this.adminConfigService.getAdminConfig().getCountryCode(), auditor.getPhoneNumber()));
		result.setPhotoURL(auditor.getPhotoURL());
		result.setSurname(auditor.getSurname());

		result.setCreditCard(auditor.getCreditCard());

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

}
