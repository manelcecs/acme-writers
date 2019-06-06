
package services;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AdministratorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;
import utiles.AddPhoneCC;
import utiles.AuthorityMethods;
import utiles.EmailValidator;
import domain.Administrator;
import forms.AdministratorForm;

@Service
@Transactional
public class AdministratorService {

	@Autowired
	private UserAccountRepository	accountRepository;

	@Autowired
	private AdministratorRepository	adminRepository;

	@Autowired
	private AdminConfigService		adminConfigService;

	@Autowired
	private MessageBoxService		messageBoxService;

	@Autowired
	private Validator				validator;


	public Administrator create() {
		final Administrator res = new Administrator();
		res.setSpammer(null);
		res.setBanned(false);
		res.setMessageBoxes(this.messageBoxService.initializeNewUserBoxes());
		return res;
	}

	public Administrator save(final Administrator admin) {

		Assert.isTrue(admin != null);
		Assert.isTrue(AuthorityMethods.checkIsSomeoneLogged());
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged(Authority.ADMINISTRATOR));
		Assert.isTrue(!admin.getBanned());

		if (admin.getId() == 0) {
			final UserAccount userAccount = admin.getUserAccount();

			final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			final String pass = encoder.encodePassword(userAccount.getPassword(), null);
			userAccount.setPassword(pass);

			final UserAccount finalAccount = this.accountRepository.save(userAccount);

			admin.setUserAccount(finalAccount);
		}

		final Administrator res = this.adminRepository.save(admin);

		return res;

	}

	public Administrator findOne(final int actorId) {
		return this.adminRepository.findOne(actorId);
	}

	public Administrator findByPrincipal(final UserAccount principal) {
		return this.adminRepository.findByPrincipal(principal.getId());
	}

	public Administrator reconstruct(final AdministratorForm adminForm, final BindingResult binding) {

		if (!EmailValidator.validateEmail(adminForm.getEmail(), Authority.ADMINISTRATOR))
			binding.rejectValue("email", "administrator.edit.email.error");
		if (!adminForm.getUserAccount().getPassword().equals(adminForm.getConfirmPassword()))
			binding.rejectValue("confirmPassword", "administrator.edit.confirmPassword.error");
		if (this.accountRepository.findByUsername(adminForm.getUserAccount().getUsername()) != null)
			binding.rejectValue("userAccount.username", "administrator.edit.userAccount.username.error");
		if (!adminForm.getTermsAndConditions())
			binding.rejectValue("termsAndConditions", "administrator.edit.termsAndConditions.error");

		final Administrator result;
		result = this.create();

		final UserAccount account = adminForm.getUserAccount();

		final Authority a = new Authority();
		a.setAuthority(Authority.ADMINISTRATOR);
		account.addAuthority(a);

		result.setUserAccount(account);
		result.setAddress(adminForm.getAddress());
		result.setEmail(adminForm.getEmail());
		result.setName(adminForm.getName());
		result.setPhoneNumber(AddPhoneCC.addPhoneCC(this.adminConfigService.getAdminConfig().getCountryCode(), adminForm.getPhoneNumber()));
		result.setPhotoURL(adminForm.getPhotoURL());

		result.setSurname(adminForm.getSurname());

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();
		return result;
	}
	public Administrator reconstruct(final Administrator admin, final BindingResult binding) {

		if (!EmailValidator.validateEmail(admin.getEmail(), Authority.ADMINISTRATOR))
			binding.rejectValue("email", "administrator.edit.email.error");

		final Administrator result;
		result = this.findByPrincipal(LoginService.getPrincipal());
		result.setAddress(admin.getAddress());
		result.setEmail(admin.getEmail());
		result.setName(admin.getName());
		result.setPhoneNumber(AddPhoneCC.addPhoneCC(this.adminConfigService.getAdminConfig().getCountryCode(), admin.getPhoneNumber()));
		result.setPhotoURL(admin.getPhotoURL());
		result.setSurname(admin.getSurname());

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();
		return result;
	}

	public void flush() {
		this.adminRepository.flush();
	}

	public Administrator getOne() {
		return this.adminRepository.findAll().get(0);
	}

	public Collection<Administrator> findAll() {
		return this.adminRepository.findAll();
	}

	//DASHBOARD----------------------------------------------------------

	public Double getAvgOfBooksPerWriter() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.adminRepository.getAvgOfBooksPerWriter();
	}

	public Integer getMinimumOfBooksPerWriter() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.adminRepository.getMinimumOfBooksPerWriter();
	}

	public Integer getMaximumOfBooksPerWriter() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.adminRepository.getMaximumOfBooksPerWriter();
	}

	public Double getSDOfBooksPerWriter() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.adminRepository.getSDOfBooksPerWriter();
	}

	//---------------------------------------------------------------------

	public Double getAvgOfContestPerPublisher() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.adminRepository.getAvgOfContestPerPublisher();
	}

	public Integer getMinimumOfContestPerPublisher() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.adminRepository.getMinimumOfContestPerPublisher();
	}

	public Integer getMaximumOfContestPerPublisher() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.adminRepository.getMaximumOfContestPerPublisher();
	}

	public Double getSDOfContestPerPublisher() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.adminRepository.getSDOfContestPerPublisher();
	}

	//------------------------------------------------------------------------

	public Double getRatioOfBooksWithPublisherVsBooksIndependients() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.adminRepository.getRatioOfBooksWithPublisherVsBooksIndependients();
	}

	//------------------------------------------------------------------------

	public Double getRatioOfBooksAcceptedVsBooksRejected() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.adminRepository.getRatioOfBooksAcceptedVsBooksRejected();
	}

	//------------------------------------------------------------------------

	public Double getAvgOfChaptersPerBook() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.adminRepository.getAvgOfChaptersPerBook();
	}

	public Integer getMinimumOfChaptersPerBook() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.adminRepository.getMinimumOfChaptersPerBook();
	}

	public Integer getMaximumOfChaptersPerBook() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.adminRepository.getMaximumOfChaptersPerBook();
	}

	public Double getSDOfChaptersPerBook() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.adminRepository.getSDOfChaptersPerBook();
	}

	//--------------------------------------------------------------------------

	public List<Object[]> getHistogramData() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.adminRepository.getHistogramData();
	}

	//--------------------------------------------------------------------------

	public Double getAvgOfSponsorshipsPerSponsor() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.adminRepository.getAvgOfSponsorshipsPerSponsor();
	}

	public Integer getMinimumOfSponsorshipsPerSponsor() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.adminRepository.getMinimumOfSponsorshipsPerSponsor();
	}

	public Integer getMaximumOfSponsorshipsPerSponsor() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.adminRepository.getMaximumOfSponsorshipsPerSponsor();
	}

	public Double getSDOfSponsorshipsPerSponsor() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.adminRepository.getSDOfSponsorshipsPerSponsor();
	}

	//--------------------------------------------------------------------------

	public Double getRatioOfSponsorshipsCancelledVsSponsorshipsNotCancelled() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.adminRepository.getRatioOfSponsorshipsCancelledVsSponsorshipsNotCancelled();
	}

	//--------------------------------------------------------------------------

	public Double getAvgOfViewsPerSponsorship() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.adminRepository.getAvgOfViewsPerSponsorship();
	}

	public Integer getMinimumOfViewsPerSponsorship() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.adminRepository.getMinimumOfViewsPerSponsorship();
	}

	public Integer getMaximumOfViewsPerSponsorship() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.adminRepository.getMaximumOfViewsPerSponsorship();
	}

	public Double getSDOfViewsPerSponsorship() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.adminRepository.getSDOfViewsPerSponsorship();
	}

	public Integer getMaximumOfParticipationsContest() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.adminRepository.getMaximumOfParticipationsContest();
	}

	public Integer getMaximumOfSponsorshipsContest() {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		return this.adminRepository.getMaximumOfSponsorshipsContest();
	}

	public void saveAnonymize(final Administrator anonymousAdmin) {
		Assert.isTrue(anonymousAdmin != null);
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged(Authority.ADMINISTRATOR) || AuthorityMethods.chechAuthorityLogged(Authority.BAN));
		this.adminRepository.save(anonymousAdmin);
	}

}
