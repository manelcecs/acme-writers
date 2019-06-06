
package services;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;

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
import domain.Reader;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ReaderServiceTest extends AbstractTest {

	@Autowired
	private ReaderService	readerService;


	/**
	 * Este test hace referencia al registro de un nuevo readeristrador.
	 * Corresponde con el requisito funcional 11.1.
	 * Además de datos válidos, es requisito indispensable que para registrar
	 * un readeristrador nuevo, lo haga un readersitrador ya existente.
	 * 1 test positivo
	 * 4 test negativos
	 * 
	 * @throws ParseException
	 */
	@Test
	public void AdminRegisterDriver() throws ParseException {
		final Object testingData[][] = {
			{
				/*
				 * usuario no logeado
				 * a)11.1.
				 * b)Positivo
				 * c)13%
				 * d)2/4
				 */
				null, true, null
			}, {
				/*
				 * usuario logeado como reader
				 * a)11.1
				 * b)Negativo
				 * c)100%
				 * d)2/4
				 */
				"reader0", true, IllegalArgumentException.class
			}, {
				/*
				 * usuario logeado como reader y datos no validos
				 * a)11.1
				 * b)Negativo
				 * c)39%
				 * d)2/4
				 */
				"reader1", false, IllegalArgumentException.class
			}, {
				/*
				 * usuario logeado como writer
				 * a)11.1
				 * b)Negativo
				 * c)39%
				 * d)2/4
				 */
				"writer0", true, IllegalArgumentException.class
			}, {
				/*
				 * usuario logeado como lector, datos invalidos.(email y photoURL)
				 * a)11.1
				 * b)Negativo
				 * c)100%
				 * d)2/4
				 */
				null, false, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.AdminRegisterTemplate((String) testingData[i][0], (boolean) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Ancillary methods ------------------------------------------------------

	protected void AdminRegisterTemplate(final String username, final boolean validData, final Class<?> expected) throws ParseException {
		Class<?> caught;

		caught = null;
		Reader reader = this.readerService.create();
		try {
			this.authenticate(username);

			if (!validData) {
				reader.setEmail("null");
				reader.setPhotoURL("thisisanonvalidurl");
			} else {
				String uniqueId = "";
				if (username == null)
					uniqueId = "NonAuth";
				else
					uniqueId = "" + (LoginService.getPrincipal().hashCode() * 31);
				reader.setEmail("reader" + uniqueId + "@mailto.com");
				reader.setPhotoURL("https://tiny.url/dummyPhoto");
			}
			reader = ReaderServiceTest.fillData(reader);
			this.readerService.save(reader);
			this.readerService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	private static Reader fillData(final Reader reader) throws NoSuchAlgorithmException {
		final Reader res = reader;

		res.setAddress("Dirección de prueba");
		res.setBanned(false);

		res.setName("Dummy Reader");
		res.setPhoneNumber("+34 555-2342");
		res.setSpammer(false);

		final String dummySurname = "Dummy Wane Dan";
		res.setSurname(dummySurname);

		final UserAccount a = new UserAccount();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.READER);
		a.addAuthority(auth);
		final SecureRandom randomGenerator = SecureRandom.getInstance("SHA1PRNG");
		a.setUsername("DummyTest" + randomGenerator.nextInt(res.getEmail().length()) + randomGenerator.nextInt(res.getEmail().length()));
		a.setPassword("DummyPass");

		res.setUserAccount(a);

		return res;
	}
}
