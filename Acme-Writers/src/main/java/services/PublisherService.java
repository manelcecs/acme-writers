
package services;

import java.text.ParseException;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.PublisherRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;
import utiles.AddPhoneCC;
import utiles.AuthorityMethods;
import utiles.ValidateCreditCard;
import domain.Publisher;
import forms.PublisherForm;

@Service
@Transactional
public class PublisherService {

	@Autowired
	private UserAccountRepository	accountRepository;

	@Autowired
	private PublisherRepository		publisherRepository;

	@Autowired
	private AdminConfigService		adminConfigService;

	@Autowired
	private MessageBoxService		messageBoxService;

	@Autowired
	private Validator				validator;


	public Publisher create() throws ParseException {
		final Publisher res = new Publisher();
		res.setSpammer(null);
		res.setBanned(false);
		res.setMessageBoxes(this.messageBoxService.initializeNewUserBoxes());
		return res;
	}
	public Publisher save(final Publisher publisher) {
		Assert.isTrue(publisher != null);

		if (publisher.getId() == 0) {
			Assert.isTrue(!AuthorityMethods.checkIsSomeoneLogged());
			final UserAccount userAccount = publisher.getUserAccount();

			final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			final String pass = encoder.encodePassword(userAccount.getPassword(), null);
			userAccount.setPassword(pass);

			final UserAccount finalAccount = this.accountRepository.save(userAccount);

			publisher.setUserAccount(finalAccount);
		} else
			Assert.isTrue(!publisher.getBanned());

		final Publisher res = this.publisherRepository.save(publisher);

		return res;

	}

	public void flush() {
		this.publisherRepository.flush();
	}

	public Publisher findOne(final int publisherId) {
		return this.publisherRepository.findOne(publisherId);
	}

	public Publisher findByPrincipal(final UserAccount principal) {
		return this.publisherRepository.findByPrincipal(principal.getId());
	}

	public Publisher reconstruct(final PublisherForm publisherForm, final BindingResult binding) throws ParseException {

		if (!this.validateEmail(publisherForm.getEmail()))
			binding.rejectValue("email", "publisher.edit.email.error");
		if (!publisherForm.getUserAccount().getPassword().equals(publisherForm.getConfirmPassword()))
			binding.rejectValue("confirmPassword", "publisher.edit.confirmPassword.error");
		if (this.accountRepository.findByUsername(publisherForm.getUserAccount().getUsername()) != null)
			binding.rejectValue("userAccount.username", "publisher.edit.userAccount.username.error");
		if (!publisherForm.getTermsAndConditions())
			binding.rejectValue("termsAndConditions", "publisher.edit.termsAndConditions.error");

		publisherForm.setCreditCard(ValidateCreditCard.checkNumeroAnno(publisherForm.getCreditCard()));

		ValidateCreditCard.checkGregorianDate(publisherForm.getCreditCard(), binding);
		ValidateCreditCard.checkMakeCreditCard(publisherForm.getCreditCard(), this.adminConfigService.getAdminConfig().getCreditCardMakes(), binding);

		final Publisher result;
		result = this.create();

		final UserAccount account = publisherForm.getUserAccount();

		final Authority a = new Authority();
		a.setAuthority(Authority.PUBLISHER);
		account.addAuthority(a);

		result.setUserAccount(account);
		result.setAddress(publisherForm.getAddress());
		result.setEmail(publisherForm.getEmail());
		result.setName(publisherForm.getName());
		result.setVAT(publisherForm.getVAT());
		result.setPhoneNumber(AddPhoneCC.addPhoneCC(this.adminConfigService.getAdminConfig().getCountryCode(), publisherForm.getPhoneNumber()));
		result.setPhotoURL(publisherForm.getPhotoURL());
		result.setSurname(publisherForm.getSurname());
		result.setCommercialName(publisherForm.getCommercialName());

		result.setCreditCard(publisherForm.getCreditCard());

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();
		return result;
	}

	public Publisher reconstruct(final Publisher publisher, final BindingResult binding) {

		if (!this.validateEmail(publisher.getEmail()))
			binding.rejectValue("email", "publisher.edit.email.error");

		publisher.setCreditCard(ValidateCreditCard.checkNumeroAnno(publisher.getCreditCard()));
		ValidateCreditCard.checkGregorianDate(publisher.getCreditCard(), binding);
		ValidateCreditCard.checkMakeCreditCard(publisher.getCreditCard(), this.adminConfigService.getAdminConfig().getCreditCardMakes(), binding);

		final Publisher result;
		result = this.findByPrincipal(LoginService.getPrincipal());
		result.setAddress(publisher.getAddress());
		result.setEmail(publisher.getEmail());
		result.setName(publisher.getName());
		result.setPhoneNumber(AddPhoneCC.addPhoneCC(this.adminConfigService.getAdminConfig().getCountryCode(), publisher.getPhoneNumber()));
		result.setPhotoURL(publisher.getPhotoURL());
		result.setVAT(publisher.getVAT());
		result.setCommercialName(publisher.getCommercialName());
		result.setSurname(publisher.getSurname());

		result.setCreditCard(publisher.getCreditCard());

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();
		return result;
	}

	public Boolean validateEmail(final String email) {

		Boolean valid = false;

		final Pattern emailPattern = Pattern.compile("^([0-9a-zA-Z ]{1,}[ ]{1}[<]{1}[0-9a-zA-Z ]{1,}[@]{1}[0-9a-zA-Z.]{1,}[>]{1}|[0-9a-zA-Z ]{1,}[@]{1}[0-9a-zA-Z.]{1,})$");

		final Matcher mEmail = emailPattern.matcher(email.toLowerCase());
		if (mEmail.matches())
			valid = true;
		return valid;
	}

	public Collection<Publisher> findAll() {
		return this.publisherRepository.findAll();
	}
	public void saveAnonymize(final Publisher anonymousPublisher) {
		Assert.isTrue(anonymousPublisher != null);
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged(Authority.ADMINISTRATOR) || AuthorityMethods.chechAuthorityLogged(Authority.BAN));
		this.publisherRepository.save(anonymousPublisher);
	}

}
