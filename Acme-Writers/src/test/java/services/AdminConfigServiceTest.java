
package services;

import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.AdminConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AdminConfigServiceTest extends AbstractTest {

	@Autowired
	private AdminConfigService	adminConfigService;


	/**
	 * #1 Test for Case use: Administrator can edit the system config
	 * 1 positive
	 * 14 negative
	 */
	@Test
	public void editAdminConfigDriver() {
		final Object testingData[][] = {
			{
				/**
				 * a)#1 | admin edit with legacy data
				 * b) Positive
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"admin", "http://url.com", 1, "+34", 10, "Acme-Rookie", "Hi, you are welcome", "Hola, bienvenido", 100.0, 0.0, 0.0, null
			}, {
				/**
				 * a)#1 | reader edit with legacy data
				 * b) Negative
				 * c) Sequence coverage: 75%
				 * d) Data coverage: 100%
				 * 
				 */
				"reader0", "http://url.com", 1, "+34", 10, "Acme-Rookie", "Hi, you are welcome", "Hola, bienvenido", 21.0, 3000.0, 0.0, IllegalArgumentException.class
			}, {
				/**
				 * a)#1 | writer edit with legacy data
				 * b) Negative
				 * c) Sequence coverage: 75%
				 * d) Data coverage: 100%
				 * 
				 */
				"writer0", "http://url.com", 1, "+34", 10, "Acme-Rookie", "Hi, you are welcome", "Hola, bienvenido", 21.0, 3000.0, 0.0, IllegalArgumentException.class
			}, {
				/**
				 * a)#1 | admin edit with unvalid url
				 * b) Negative
				 * c) Sequence coverage: 75%
				 * d) Data coverage: 100%
				 * 
				 */
				"admin", "es no es una url", 1, "+34", 10, "Acme-Rookie", "Hi, you are welcome", "Hola, bienvenido", 21.0, 3000.0, 0.0, ConstraintViolationException.class
			}, {
				/**
				 * a)#1 | admin edit with unvalid cache time finder
				 * b) Negative
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"admin", "http://url.com", -1, "+34", 10, "Acme-Rookie", "Hi, you are welcome", "Hola, bienvenido", 21.0, 3000.0, 0.0, ConstraintViolationException.class
			}, {
				/**
				 * a)#1 | admin edit with unvalid cache time finder
				 * b) Negative
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"admin", "http://url.com", 25, "+34", 10, "Acme-Rookie", "Hi, you are welcome", "Hola, bienvenido", 21.0, 3000.0, 0.0, ConstraintViolationException.class
			}, {
				/**
				 * a)#1 | admin edit with unlegal finder number results
				 * b) Negative
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"admin", "http://url.com", 1, "+34", 0, "Acme-Rookie", "Hi, you are welcome", "Hola, bienvenido", 21.0, 3000.0, 0.0, ConstraintViolationException.class
			}, {
				/**
				 * a)#1 | admin edit with unlegal finder number results
				 * b) Negative
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"admin", "http://url.com", 1, "+34", 101, "Acme-Rookie", "Hi, you are welcome", "Hola, bienvenido", 21.0, 3000.0, 0.0, ConstraintViolationException.class
			}, {
				/**
				 * a)#1 | admin edit with unlegal country code
				 * b) Negative
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"admin", "http://url.com", 1, "+1000", 10, "Acme-Rookie", "Hi, you are welcome", "Hola, bienvenido", 21.0, 3000.0, 0.0, ConstraintViolationException.class
			}, {
				/**
				 * a)#1 | admin edit with blank system name
				 * b) Negative
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"admin", "http://url.com", 1, "+34", 10, "", "Hi, you are welcome", "Hola, bienvenido", 21.0, 3000.0, 0.0, ConstraintViolationException.class
			}, {
				/**
				 * a)#1 | admin edit with blank welcome message spanish
				 * b) Negative
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"admin", "http://url.com", 1, "+34", 10, "Acme-Rookie", "", "Hola, bienvenido", 21.0, 3000.0, 0.0, ConstraintViolationException.class
			}, {
				/**
				 * a)#1 | admin edit with blank welcome message english
				 * b) Negative
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"admin", "http://url.com", 1, "+34", 10, "Acme-Rookie", "Hi, you are welcome", "", 21.0, 3000.0, 0.0, ConstraintViolationException.class
			}, {
				/**
				 * a)#1 | admin edit with invalid VAT (lower limit data)
				 * b) Negative
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"admin", "http://url.com", 1, "+34", 10, "Acme-Rookie", "Hi, you are welcome", "Hola, bienvenido", -0.1, 3000.0, 0.0, ConstraintViolationException.class
			}, {
				/**
				 * a)#1 | admin edit with invalid VAT (upper limit data)
				 * b) Negative
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"admin", "http://url.com", 1, "+34", 10, "Acme-Rookie", "Hi, you are welcome", "Hola, bienvenido", 101.0, 3000.0, 0.0, ConstraintViolationException.class
			}, {
				/**
				 * a)#1 | admin edit with invalid Fare
				 * b) Negative
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"admin", "http://url.com", 1, "+34", 10, "Acme-Rookie", "Hi, you are welcome", "Hola, bienvenido", 100.0, -0.1, 0.0, ConstraintViolationException.class
			}, {
				/**
				 * a)#1 | admin edit with invalid spammerPercentage (lower limit data)
				 * b) Negative
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"admin", "http://url.com", 1, "+34", 10, "Acme-Rookie", "Hi, you are welcome", "Hola, bienvenido", 100.0, 0.0, -0.1, ConstraintViolationException.class
			}, {
				/**
				 * a)#1 | admin edit with invalid Fare (upper limit data)
				 * b) Negative
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"admin", "http://url.com", 1, "+34", 10, "Acme-Rookie", "Hi, you are welcome", "Hola, bienvenido", 100.0, 0., 100.1, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editAdminConfigTemplate((String) testingData[i][0], (String) testingData[i][1], (Integer) testingData[i][2], (String) testingData[i][3], (Integer) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (Double) testingData[i][8], (Double) testingData[i][9], (Double) testingData[i][10], (Class<?>) testingData[i][11]);
	}
	protected void editAdminConfigTemplate(final String beanName, final String bannerURL, final Integer cacheFinder, final String countryCode, final Integer resultsFinder, final String systemName, final String welcomeMessageEN,
		final String welcomeMessageES, final Double VAT, final Double flatRate, final Double spammerPercentage, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			super.authenticate(beanName);
			final AdminConfig adminConfig = this.adminConfigService.getAdminConfig();

			adminConfig.setSystemName(systemName);
			adminConfig.setBannerURL(bannerURL);
			adminConfig.setWelcomeMessageEN(welcomeMessageEN);
			adminConfig.setWelcomeMessageES(welcomeMessageES);
			adminConfig.setCountryCode(countryCode);
			adminConfig.setFinderResults(resultsFinder);
			adminConfig.setFinderCacheTime(cacheFinder);
			adminConfig.setVAT(VAT);
			adminConfig.setFlatRate(flatRate);
			adminConfig.setSpammerPercentage(spammerPercentage);

			this.adminConfigService.save(adminConfig);
			this.adminConfigService.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	/**
	 * #2 Test for Case use: Administrator can manage the list of spam words
	 * 2 positive
	 * 7 negative
	 */
	@Test
	public void editSpamWordDrive() {
		final Object testingData[][] = {
			{
				/**
				 * a)#2 | admin add spamword
				 * b) Positive
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"admin", "nueva palabra", "add", null
			}, {
				/**
				 * a)#2 | admin delete a spam word
				 * b) Positive
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"admin", "sex", "delete", null
			}, {
				/**
				 * a)#2 | admin delete nonexistent spam word
				 * b) Negative
				 * c) Sequence coverage: 26%
				 * d) Data coverage: 100%
				 * 
				 */
				"admin", "dvdsdvsdv", "delete", IllegalArgumentException.class
			}, {
				/**
				 * a)#2 | rookie delete spam word
				 * b) Negative
				 * c) Sequence coverage: 6.5%
				 * d) Data coverage: 100%
				 * 
				 */
				"rookie0", "sex", "delete", IllegalArgumentException.class
			}, {
				/**
				 * a)#2 | rookie add spam word
				 * b) Negative
				 * c) Sequence coverage: 75%
				 * d) Data coverage: 100%
				 * 
				 */
				"rookie0", "nueva palabra", "add", IllegalArgumentException.class
			}, {
				/**
				 * a)#2 | company delete spam word
				 * b) Negative
				 * c) Sequence coverage: 6.5%
				 * d) Data coverage: 100%
				 * 
				 */
				"company0", "sex", "delete", IllegalArgumentException.class
			}, {
				/**
				 * a)#2 | company add spam word
				 * b) Negative
				 * c) Sequence coverage: 75%
				 * d) Data coverage: 100%
				 * 
				 */
				"company0", "palabra", "add", IllegalArgumentException.class
			}, {
				/**
				 * a)#2 | unlogged add spam word
				 * b) Negative
				 * c) Sequence coverage: 6.5%
				 * d) Data coverage: 100%
				 * 
				 */
				null, "sex", "delete", IllegalArgumentException.class
			}, {
				/**
				 * a)#2 | unlogged add spam word
				 * b) Negative
				 * c) Sequence coverage: 75%
				 * d) Data coverage: 100%
				 * 
				 */
				null, "palabra", "add", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editSpamWordTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	protected void editSpamWordTemplate(final String user, final String spamWord, final String action, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			super.authenticate(user);
			final AdminConfig adminConfig = this.adminConfigService.getAdminConfig();

			final Collection<String> spamWords = adminConfig.getSpamWords();

			if (action == "add") {
				spamWords.add(spamWord);
				adminConfig.setSpamWords(spamWords);
				this.adminConfigService.save(adminConfig);
			} else if (action == "delete")
				this.adminConfigService.deleteSpamWord(spamWord);

			this.adminConfigService.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	/**
	 * #2 Test for Case use: Administrator can manage the list of credit card makes
	 * 2 positive
	 * 7 negative
	 */
	@Test
	public void editCreditCardMakeDrive() {
		final Object testingData[][] = {
			{
				/**
				 * a)#2 | admin add credit card make
				 * b) Positive
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"admin", "nueva marca", "add", null
			}, {
				/**
				 * a)#2 | admin delete a credit card make
				 * b) Positive
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"admin", "VISA", "delete", null
			}, {
				/**
				 * a)#2 | admin delete nonexistent credit card make
				 * b) Negative
				 * c) Sequence coverage: 26%
				 * d) Data coverage: 100%
				 * 
				 */
				"admin", "dvdsdvsdv", "delete", IllegalArgumentException.class
			}, {
				/**
				 * a)#2 | reader delete credit card make
				 * b) Negative
				 * c) Sequence coverage: 6.5%
				 * d) Data coverage: 100%
				 * 
				 */
				"reader0", "VISA", "delete", IllegalArgumentException.class
			}, {
				/**
				 * a)#2 | reader add credit card make
				 * b) Negative
				 * c) Sequence coverage: 75%
				 * d) Data coverage: 100%
				 * 
				 */
				"reader0", "nueva marca", "add", IllegalArgumentException.class
			}, {
				/**
				 * a)#2 | writer delete credit card make
				 * b) Negative
				 * c) Sequence coverage: 6.5%
				 * d) Data coverage: 100%
				 * 
				 */
				"writer0", "VISA", "delete", IllegalArgumentException.class
			}, {
				/**
				 * a)#2 | writer add credit card make
				 * b) Negative
				 * c) Sequence coverage: 75%
				 * d) Data coverage: 100%
				 * 
				 */
				"writer0", "marca", "add", IllegalArgumentException.class
			}, {
				/**
				 * a)#2 | unlogged add credit card make
				 * b) Negative
				 * c) Sequence coverage: 6.5%
				 * d) Data coverage: 100%
				 * 
				 */
				null, "sex", "delete", IllegalArgumentException.class
			}, {
				/**
				 * a)#2 | unlogged add credit card make
				 * b) Negative
				 * c) Sequence coverage: 75%
				 * d) Data coverage: 100%
				 * 
				 */
				null, "marca", "add", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editCreditCardMakeTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	protected void editCreditCardMakeTemplate(final String user, final String creditCardMake, final String action, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {
			super.authenticate(user);
			final AdminConfig adminConfig = this.adminConfigService.getAdminConfig();

			final Collection<String> creditCardMakes = adminConfig.getCreditCardMakes();

			if (action == "add") {
				creditCardMakes.add(creditCardMake);
				adminConfig.setCreditCardMakes(creditCardMakes);
				this.adminConfigService.save(adminConfig);
			} else if (action == "delete")
				this.adminConfigService.deleteCreditCardMake(creditCardMake);

			this.adminConfigService.flush();
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
