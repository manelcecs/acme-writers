
package services;

import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AdminConfigRepository;
import utiles.AuthorityMethods;
import domain.AdminConfig;
import domain.Message;
import forms.AdminConfigForm;
import forms.CreditCardMakeForm;
import forms.SpamWordForm;

@Service
@Transactional
public class AdminConfigService {

	@Autowired
	private AdminConfigRepository	adminConfigRepository;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private Validator				validator;


	public AdminConfig getAdminConfig() {
		return this.adminConfigRepository.findAll().get(0);
	}

	public AdminConfig save(final AdminConfig adminConfig) {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.adminConfigRepository.save(adminConfig);
	}

	public AdminConfig reconstruct(final AdminConfigForm adminConfigForm, final BindingResult binding) {
		final AdminConfig adminConfig;

		adminConfig = this.getAdminConfig();

		adminConfig.setBannerURL(adminConfigForm.getBannerURL());
		adminConfig.setFinderCacheTime(adminConfigForm.getFinderCacheTime());
		adminConfig.setCountryCode(adminConfigForm.getCountryCode());
		adminConfig.setFinderResults(adminConfigForm.getFinderResults());
		adminConfig.setSystemName(adminConfigForm.getSystemName());
		adminConfig.setWelcomeMessageEN(adminConfigForm.getWelcomeMessageEN());
		adminConfig.setWelcomeMessageES(adminConfigForm.getWelcomeMessageES());
		adminConfig.setFlatRate(adminConfigForm.getFlatRate());
		adminConfig.setVAT(adminConfigForm.getVAT());
		

		this.validator.validate(adminConfig, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return adminConfig;
	}

	public boolean existSpamWord(final Message message) {
		boolean exist = false;
		final Collection<String> spamWords = this.getAdminConfig().getSpamWords();
		for (final String spamWord : spamWords) {
			final Integer spam = this.messageService.existsSpamWordInMessage(message.getId(), spamWord);
			if (spam == null) {
				exist = true;
				break;
			}
		}
		return exist;
	}

	public void deleteSpamWord(final String spamWord) {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		final AdminConfig adminConfig = this.getAdminConfig();
		final Collection<String> spamWords = adminConfig.getSpamWords();
		Assert.isTrue(spamWords.contains(spamWord));
		spamWords.remove(spamWord);
		adminConfig.setSpamWords(spamWords);
		this.save(adminConfig);
	}

	public void deleteCreditCardMake(final String creditCardMake) {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		final AdminConfig adminConfig = this.getAdminConfig();
		final Collection<String> creditCardMakes = adminConfig.getCreditCardMakes();
		Assert.isTrue(creditCardMakes.contains(creditCardMake));
		Assert.isTrue(creditCardMakes.size() > 1);
		creditCardMakes.remove(creditCardMake);
		adminConfig.setCreditCardMakes(creditCardMakes);
		this.save(adminConfig);
	}

	public void flush() {
		this.adminConfigRepository.flush();
	}

	public AdminConfig addSpamWord(final SpamWordForm spamWordForm, final BindingResult binding) {
		final AdminConfig adminConfig;

		adminConfig = this.getAdminConfig();

		final Collection<String> spamWords = adminConfig.getSpamWords();

		if (!(spamWordForm.getSpamWord().trim().isEmpty())) {
			if (spamWords.contains(spamWordForm.getSpamWord().trim().toLowerCase()))
				binding.rejectValue("spamWord", "adminConfig.error.existSpamWord");

			spamWords.add(spamWordForm.getSpamWord().toLowerCase());
		}
		adminConfig.setSpamWords(spamWords);

		this.validator.validate(adminConfig, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return adminConfig;
	}

	public AdminConfig addCreditCardMake(final CreditCardMakeForm creditCardMakeForm, final BindingResult binding) {
		final AdminConfig adminConfig;

		adminConfig = this.getAdminConfig();

		final Collection<String> creditCardMakes = adminConfig.getCreditCardMakes();

		if (!(creditCardMakeForm.getCreditCardMake().trim().isEmpty())) {
			if (creditCardMakes.contains(creditCardMakeForm.getCreditCardMake().trim().toUpperCase()))
				binding.rejectValue("creditCardMake", "adminConfig.error.existCreditCardMake");

			creditCardMakes.add(creditCardMakeForm.getCreditCardMake().toUpperCase());
		}
		adminConfig.setCreditCardMakes(creditCardMakes);

		this.validator.validate(adminConfig, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return adminConfig;
	}
}
