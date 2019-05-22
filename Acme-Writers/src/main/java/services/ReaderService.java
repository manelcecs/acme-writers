
package services;

import java.text.ParseException;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ReaderRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;
import utiles.AddPhoneCC;
import utiles.AuthorityMethods;
import utiles.EmailValidator;
import domain.Reader;
import forms.ReaderForm;

@Service
@Transactional
public class ReaderService {

	@Autowired
	private UserAccountRepository	accountRepository;

	@Autowired
	private ReaderRepository		readerRepository;

	@Autowired
	private AdminConfigService		adminConfigService;

	@Autowired
	private Validator				validator;

	@Autowired
	private FinderService			finderService;


	//TODO: messageBoxesService

	public Reader create() throws ParseException {
		final Reader reader = new Reader();

		reader.setFinder(this.finderService.generateNewFinder());
		reader.setBanned(false);
		reader.setSpammer(false);
		//TODO: reader.setMessageBoxes(this.)

		return reader;
	}

	public Reader save(final Reader reader) {
		Assert.isTrue(reader != null);
		if (reader.getId() == 0) {
			Assert.isTrue(!AuthorityMethods.checkIsSomeoneLogged());
			final UserAccount userAccount = reader.getUserAccount();

			final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			final String pass = encoder.encodePassword(userAccount.getPassword(), null);
			userAccount.setPassword(pass);

			final UserAccount finalAccount = this.accountRepository.save(userAccount);

			reader.setUserAccount(finalAccount);
		} else
			Assert.isTrue(AuthorityMethods.chechAuthorityLogged(Authority.READER) || AuthorityMethods.chechAuthorityLogged(Authority.ADMINISTRATOR));
		return this.readerRepository.saveAndFlush(reader);
	}

	public Reader reconstruct(final ReaderForm readerForm, final BindingResult binding) throws ParseException {

		if (!EmailValidator.validateEmail(readerForm.getEmail(), Authority.READER))
			binding.rejectValue("email", "reader.edit.email.error");
		if (!readerForm.getUserAccount().getPassword().equals(readerForm.getConfirmPassword()))
			binding.rejectValue("confirmPassword", "reader.edit.confirmPassword.error");
		if (this.accountRepository.findByUsername(readerForm.getUserAccount().getUsername()) != null)
			binding.rejectValue("userAccount.username", "reader.edit.userAccount.username.error");
		if (!readerForm.getTermsAndConditions())
			binding.rejectValue("termsAndConditions", "reader.edit.termsAndConditions.error");

		final Reader result;
		result = this.create();

		final UserAccount account = readerForm.getUserAccount();

		final Authority a = new Authority();
		a.setAuthority(Authority.READER);
		account.addAuthority(a);

		result.setUserAccount(account);
		result.setAddress(readerForm.getAddress());
		result.setEmail(readerForm.getEmail());
		result.setName(readerForm.getName());
		result.setPhoneNumber(AddPhoneCC.addPhoneCC(this.adminConfigService.getAdminConfig().getCountryCode(), readerForm.getPhoneNumber()));
		result.setPhotoURL(readerForm.getPhotoURL());
		result.setSurname(readerForm.getSurname());

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

	public Reader reconstruct(final Reader reader, final BindingResult binding) {

		if (!EmailValidator.validateEmail(reader.getEmail(), Authority.READER))
			binding.rejectValue("email", "reader.edit.email.error");

		final Reader result;
		result = this.findByPrincipal(LoginService.getPrincipal());

		result.setAddress(reader.getAddress());
		result.setEmail(reader.getEmail());
		result.setName(reader.getName());
		result.setPhoneNumber(AddPhoneCC.addPhoneCC(this.adminConfigService.getAdminConfig().getCountryCode(), reader.getPhoneNumber()));
		result.setPhotoURL(reader.getPhotoURL());
		result.setSurname(reader.getSurname());

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

	public Reader findByPrincipal(final UserAccount principal) {
		return this.readerRepository.findByPrincipal(principal.getId());
	}

	public Reader findOne(final int readerId) {
		return this.readerRepository.findOne(readerId);
	}

}
