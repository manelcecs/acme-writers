
package services;

import java.text.ParseException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Reader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ActorServiceTest extends AbstractTest {

	@Autowired
	ActorService	actorService;

	@Autowired
	ReaderService	readerService;


	/**
	 * a)#1 Test for Case use: Un administrador puede lanzar un proceso para calcular a los usuarios spammers
	 * b)
	 * c)Sequence coverage: 100%
	 * d)Data coverage:
	 */
	@Test
	public void calculateSpammers() throws ParseException {
		super.authenticate("admin");

		this.actorService.updateSpam();
		final Reader reader = this.readerService.findOne(this.getEntityId("reader0"));
		this.actorService.flush();
		Assert.isTrue(reader.getSpammer());

		super.unauthenticate();
	}

}
