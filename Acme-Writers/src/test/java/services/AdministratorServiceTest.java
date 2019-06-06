
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import security.Authority;
import security.LoginService;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Administrator;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class AdministratorServiceTest extends AbstractTest {

	@Autowired
	private AdministratorService	administratorService;


	/**
	 * Este test hace referencia al registro de un nuevo administrador.
	 * Corresponde con el requisito funcional 11.1.
	 * Además de datos válidos, es requisito indispensable que para registrar
	 * un administrador nuevo, lo haga un adminsitrador ya existente.
	 * 1 test positivo
	 * 4 test negativos
	 */
	@Test
	public void AdminRegisterDriver() {
		final Object testingData[][] = {
			{
				/*
				 * usuario no logeado
				 * a)11.1.
				 * b)Negativo
				 * c)13%
				 * d)2/4
				 */
				null, true, IllegalArgumentException.class
			}, {
				/*
				 * usuario logeado como administratdor
				 * a)11.1
				 * b)Positivo
				 * c)100%
				 * d)2/4
				 */
				"admin", true, null
			}, {
				/*
				 * usuario logeado como rookie
				 * a)11.1
				 * b)Negativo
				 * c)39%
				 * d)2/4
				 */
				"reader", true, IllegalArgumentException.class
			}, {
				/*
				 * usuario logeado como company
				 * a)11.1
				 * b)Negativo
				 * c)39%
				 * d)2/4
				 */
				"writer", true, IllegalArgumentException.class
			}, {
				/*
				 * usuario logeado como administrador, datos invalidos.(email y photoURL)
				 * a)11.1
				 * b)Negativo
				 * c)100%
				 * d)2/4
				 */
				"admin", false, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.AdminRegisterTemplate((String) testingData[i][0], (boolean) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Ancillary methods ------------------------------------------------------

	protected void AdminRegisterTemplate(final String username, final boolean validData, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		Administrator admin = this.administratorService.create();
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
				admin.setEmail("<admin" + uniqueId + "@>");
				admin.setPhotoURL("https://tiny.url/dummyPhoto");
			}
			admin = AdministratorServiceTest.fillData(admin);
			this.administratorService.save(admin);
			this.administratorService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	private static Administrator fillData(final Administrator admin) {
		final Administrator res = admin;

		res.setAddress("Dirección de prueba");
		res.setBanned(false);

		res.setName("DummyBoss");
		res.setPhoneNumber("+34 555-2342");
		res.setSpammer(false);

		final String dummySurname = "Dummy Wane Dan";
		res.setSurname(dummySurname);

		final UserAccount a = new UserAccount();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.ADMINISTRATOR);
		a.addAuthority(auth);
		a.setUsername("DummyTest" + res.getEmail());
		a.setPassword("DummyPass");

		res.setUserAccount(a);

		return res;
	}
}
