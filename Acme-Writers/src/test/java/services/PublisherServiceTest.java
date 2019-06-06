
package services;

import java.text.ParseException;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import security.Authority;
import security.LoginService;
import security.UserAccount;
import utilities.AbstractTest;
import domain.CreditCard;
import domain.Publisher;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PublisherServiceTest extends AbstractTest {

	@Autowired
	private PublisherService	publisherService;


	@Override
	@Before
	public void setUp() {

		this.unauthenticate();

	}

	@Test
	public void PublisherRegisterDriver() throws ParseException {
		final Object testingData[][] = {
			{
				/*
				 * usuario no logeado, con datos válidos.
				 * a)7.1
				 * b)Positivo
				 * c)74%
				 * d)2/4
				 */
				null, true, null
			}, {
				/*
				 * usuario logeado como admin, con datos válidos.
				 * a)7.1
				 * b)Positivo
				 * c)39%
				 * d)2/4
				 */
				"admin", true, IllegalArgumentException.class
			}, {
				/*
				 * usuario logeado como admin, con datos válidos.
				 * a)7.1
				 * b)Positivo
				 * c)39%
				 * d)2/4
				 */
				"publisher", true, IllegalArgumentException.class
			}, {
				/*
				 * usuario logeado como admin, con datos válidos.
				 * a)7.1
				 * b)Positivo
				 * c)39%
				 * d)2/4
				 */
				"publisher", true, IllegalArgumentException.class
			}, {
				/*
				 * usuario no logeado, con datos no válidos.
				 * a)7.1
				 * b)Positivo
				 * c)74%
				 * d)2/4
				 */
				null, false, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.PublisherRegisterTemplate((String) testingData[i][0], (boolean) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Ancillary methods ------------------------------------------------------

	protected void PublisherRegisterTemplate(final String username, final boolean validData, final Class<?> expected) throws ParseException {
		Class<?> caught;

		caught = null;
		Publisher admin = this.publisherService.create();
		try {
			this.authenticate(username);

			if (!validData) {
				admin.setEmail("");
				admin.setPhotoURL("thisisanonvalidurl");
			} else {
				String uniqueId = "";
				if (username == null)
					uniqueId = "NonAuth";
				else
					uniqueId = "" + (LoginService.getPrincipal().hashCode() * 31);
				admin.setEmail("dummyMail" + uniqueId + "@mailto.com");
				admin.setPhotoURL("https://tiny.url/dummyPhoto");
			}
			admin = PublisherServiceTest.fillData(admin);
			this.publisherService.save(admin);
			this.publisherService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
	@Override
	@After
	public void tearDown() {

		this.unauthenticate();

	}

	private static Publisher fillData(final Publisher admin) {
		final Publisher res = admin;

		res.setAddress("Dirección de prueba");
		res.setBanned(false);

		res.setName("DummyBoss");
		res.setPhoneNumber("+34 555-2342");
		res.setSpammer(false);
		res.setCommercialName("Dummy Corp. Test Inc.");

		final String dummySurname = "Dummy Wane Dan";
		res.setSurname(dummySurname);

		final UserAccount a = new UserAccount();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.PUBLISHER);
		a.addAuthority(auth);
		a.setUsername("DummyTest" + res.getEmail().hashCode());
		a.setPassword("DummyPass");

		res.setUserAccount(a);

		final CreditCard cc = new CreditCard();
		cc.setCvv(231);
		cc.setExpirationMonth(04);
		cc.setExpirationYear(22);
		cc.setHolder(res.getName());
		cc.setMake("VISA");
		cc.setNumber("4308543581357191");

		res.setCreditCard(cc);

		res.setVAT("888848545-R");

		return res;
	}

}
