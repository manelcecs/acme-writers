
package services;

import java.text.ParseException;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
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
import utiles.ValidateCreditCard;
import domain.Reader;
import forms.WriterForm;

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
		if (reader.getId() == 0)
			Assert.isTrue(!AuthorityMethods.checkIsSomeoneLogged());
		else
			Assert.isTrue(AuthorityMethods.chechAuthorityLogged(Authority.READER) || AuthorityMethods.chechAuthorityLogged(Authority.ADMINISTRATOR));
		return this.readerRepository.saveAndFlush(reader);
	}

	public Reader reconstruct(final WriterForm writerForm, final BindingResult binding) throws ParseException {

		if (!EmailValidator.validateEmail(writerForm.getEmail(), Authority.READER))
			binding.rejectValue("email", "reader.edit.email.error");
		if (!writerForm.getUserAccount().getPassword().equals(writerForm.getConfirmPassword()))
			binding.rejectValue("confirmPassword", "reader.edit.confirmPassword.error");
		if (this.accountRepository.findByUsername(writerForm.getUserAccount().getUsername()) != null)
			binding.rejectValue("userAccount.username", "reader.edit.userAccount.username.error");
		if (!writerForm.getTermsAndConditions())
			binding.rejectValue("termsAndConditions", "reader.edit.termsAndConditions.error");

		writerForm.setCreditCard(ValidateCreditCard.checkNumeroAnno(writerForm.getCreditCard()));
		ValidateCreditCard.checkGregorianDate(writerForm.getCreditCard(), binding);
		;

		final Reader result;
		result = this.create();

		final UserAccount account = writerForm.getUserAccount();

		final Authority a = new Authority();
		a.setAuthority(Authority.READER);
		account.addAuthority(a);

		result.setUserAccount(account);
		result.setAddress(writerForm.getAddress());
		result.setEmail(writerForm.getEmail());
		result.setName(writerForm.getName());
		result.setPhoneNumber(AddPhoneCC.addPhoneCC(this.adminConfigService.getAdminConfig().getCountryCode(), writerForm.getPhoneNumber()));
		result.setPhotoURL(writerForm.getPhotoURL());
		result.setSurname(writerForm.getSurname());

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

}
