
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

import repositories.WriterRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;
import utiles.AddPhoneCC;
import utiles.AuthorityMethods;
import utiles.EmailValidator;
import utiles.ValidateCreditCard;
import domain.Writer;
import forms.WriterForm;

@Service
@Transactional
public class WriterService {

	@Autowired
	private UserAccountRepository	accountRepository;

	@Autowired
	private WriterRepository		writerRepository;

	@Autowired
	private AdminConfigService		adminConfigService;

	@Autowired
	private MessageBoxService		messageBoxService;

	@Autowired
	private Validator				validator;


	public Writer create() {
		final Writer res = new Writer();
		res.setSpammer(null);
		res.setBanned(false);
		res.setMessageBoxes(this.messageBoxService.initializeNewUserBoxes());
		return res;
	}

	public Writer save(final Writer writer) {
		Assert.isTrue(writer != null);

		if (writer.getId() == 0) {
			Assert.isTrue(!AuthorityMethods.checkIsSomeoneLogged());
			final UserAccount userAccount = writer.getUserAccount();

			final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			final String pass = encoder.encodePassword(userAccount.getPassword(), null);
			userAccount.setPassword(pass);

			final UserAccount finalAccount = this.accountRepository.save(userAccount);

			writer.setUserAccount(finalAccount);
		} else
			Assert.isTrue(!writer.getBanned());

		final Writer res = this.writerRepository.save(writer);

		return res;

	}

	public void flush() {
		this.writerRepository.flush();
	}

	public Writer findOne(final int writerId) {
		return this.writerRepository.findOne(writerId);
	}

	public Writer findByPrincipal(final UserAccount principal) {
		return this.writerRepository.findByPrincipal(principal.getId());
	}

	public Writer reconstruct(final WriterForm writerForm, final BindingResult binding) {

		if (!EmailValidator.validateEmail(writerForm.getEmail(), Authority.WRITER))
			binding.rejectValue("email", "writer.edit.email.error");
		if (!writerForm.getUserAccount().getPassword().equals(writerForm.getConfirmPassword()))
			binding.rejectValue("confirmPassword", "writer.edit.confirmPassword.error");
		if (this.accountRepository.findByUsername(writerForm.getUserAccount().getUsername()) != null)
			binding.rejectValue("userAccount.username", "writer.edit.userAccount.username.error");
		if (!writerForm.getTermsAndConditions())
			binding.rejectValue("termsAndConditions", "writer.edit.termsAndConditions.error");

		writerForm.setCreditCard(ValidateCreditCard.checkNumeroAnno(writerForm.getCreditCard()));
		ValidateCreditCard.checkGregorianDate(writerForm.getCreditCard(), binding);
		ValidateCreditCard.checkMakeCreditCard(writerForm.getCreditCard(), this.adminConfigService.getAdminConfig().getCreditCardMakes(), binding);

		final Writer result;
		result = this.create();

		final UserAccount account = writerForm.getUserAccount();

		final Authority a = new Authority();
		a.setAuthority(Authority.WRITER);
		account.addAuthority(a);

		result.setUserAccount(account);
		result.setAddress(writerForm.getAddress());
		result.setEmail(writerForm.getEmail());
		result.setName(writerForm.getName());
		result.setPhoneNumber(AddPhoneCC.addPhoneCC(this.adminConfigService.getAdminConfig().getCountryCode(), writerForm.getPhoneNumber()));
		result.setPhotoURL(writerForm.getPhotoURL());
		result.setSurname(writerForm.getSurname());

		result.setCreditCard(writerForm.getCreditCard());

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}
	public Writer reconstruct(final Writer writer, final BindingResult binding) {

		if (!EmailValidator.validateEmail(writer.getEmail(), Authority.WRITER))
			binding.rejectValue("email", "writer.edit.email.error");

		writer.setCreditCard(ValidateCreditCard.checkNumeroAnno(writer.getCreditCard()));
		ValidateCreditCard.checkGregorianDate(writer.getCreditCard(), binding);
		ValidateCreditCard.checkMakeCreditCard(writer.getCreditCard(), this.adminConfigService.getAdminConfig().getCreditCardMakes(), binding);

		final Writer result;
		result = this.findByPrincipal(LoginService.getPrincipal());

		result.setAddress(writer.getAddress());
		result.setEmail(writer.getEmail());
		result.setName(writer.getName());
		result.setPhoneNumber(AddPhoneCC.addPhoneCC(this.adminConfigService.getAdminConfig().getCountryCode(), writer.getPhoneNumber()));
		result.setPhotoURL(writer.getPhotoURL());
		result.setSurname(writer.getSurname());

		result.setCreditCard(writer.getCreditCard());

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

	public Collection<Writer> findAll() {
		return this.writerRepository.findAll();
	}

	public Writer findByPrincipal(final int idPrincipal) {
		return this.writerRepository.findByPrincipal(idPrincipal);
	}

	//DASHBOARD-----------------------------------------------------------------------------------

	public Collection<Writer> getWritersWithMoreBooks() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.writerRepository.getWritersWithMoreBooks();
	}

	//--------------------------------------------------------------------------------------------

	public Collection<Writer> getWritersWithLessBooks() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.writerRepository.getWritersWithLessBooks();
	}

}
