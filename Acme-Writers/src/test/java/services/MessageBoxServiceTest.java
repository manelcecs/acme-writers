
package services;

import java.util.ArrayList;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Message;
import domain.MessageBox;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class MessageBoxServiceTest extends AbstractTest {

	@Autowired
	private MessageBoxService	messageBoxService;


	/**
	 * #1 Test for Case use: An actor can create message boxes
	 * 1 positive
	 * 6 negative
	 */
	@Test
	public void newBoxesDriver() {
		final Object testingData[][] = {
			{
				/**
				 * a) #1 | An actor create a message box with legacy data
				 * b) Positive
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"writer0", "nombre valido", null
			}, {
				/**
				 * a) #1 | An actor create a message box with unlegal name
				 * b) Negative
				 * c) Sequence coverage: 36'8%
				 * d) Data coverage: 100%
				 * 
				 */
				"writer0", "In Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #1 | An actor create a message box with unlegal name
				 * b) Negative
				 * c) Sequence coverage: 36'8%
				 * d) Data coverage: 100%
				 * 
				 */
				"writer0", "Out Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #1 | An actor create a message box with unlegal name
				 * b) Negative
				 * c) Sequence coverage: 36'8%
				 * d) Data coverage: 100%
				 * 
				 */
				"writer0", "Notification Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #1 | An actor create a message box with unlegal name
				 * b) Negative
				 * c) Sequence coverage: 36'8%
				 * d) Data coverage: 100%
				 * 
				 */
				"writer0", "Spam Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #1 | An actor create a message box with unlegal name
				 * b) Negative
				 * c) Sequence coverage: 36'8%
				 * d) Data coverage: 100%
				 * 
				 */
				"writer0", "Trash Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #1 | An actor create a message box with unlegal name
				 * b) Negative
				 * c) Sequence coverage: 47'3%
				 * d) Data coverage: 100%
				 * 
				 */
				"writer0", "", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.newBoxesTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void newBoxesTemplate(final String user, final String nameBox, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(user);

			final MessageBox messageBox = this.messageBoxService.create();

			messageBox.setDeleteable(true);
			messageBox.setName(nameBox);

			this.messageBoxService.save(messageBox);

			this.messageBoxService.flush();

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	/**
	 * #2 Test for Case use: An actor can edit message boxes
	 * 0 positive
	 * 15 negative
	 */
	@Test
	public void editOriginalBoxesDriver() {
		final Object testingData[][] = {
			{
				/**
				 * a) #2 | An writer edit a original message box
				 * b) Negative
				 * c) Sequence coverage: 84'2%
				 * d) Data coverage: -
				 * 
				 */
				"writer0", "In Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #2 | An writer edit a original message box
				 * b) Negative
				 * c) Sequence coverage: 84'2%
				 * d) Data coverage: -
				 * 
				 */
				"writer0", "Out Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #2 | An writer edit a original message box
				 * b) Negative
				 * c) Sequence coverage: 84'2%
				 * d) Data coverage: -
				 * 
				 */
				"writer0", "Trash Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #2 | An rookie edit a original message box
				 * b) Negative
				 * c) Sequence coverage: 84'2%
				 * d) Data coverage: -
				 * 
				 */
				"writer0", "Notification Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #2 | An writer edit a original message box
				 * b) Negative
				 * c) Sequence coverage: 84'2%
				 * d) Data coverage: -
				 * 
				 */
				"writer0", "Spam Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #2 | An reader edit a original message box
				 * b) Negative
				 * c) Sequence coverage: 84'2%
				 * d) Data coverage: -
				 * 
				 */
				"reader0", "In Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #2 | An reader edit a original message box
				 * b) Negative
				 * c) Sequence coverage: 84'2%
				 * d) Data coverage: -
				 * 
				 */
				"reader0", "Out Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #2 | An reader edit a original message box
				 * b) Negative
				 * c) Sequence coverage: 84'2%
				 * d) Data coverage: -
				 * 
				 */
				"reader0", "Trash Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #2 | An reader edit a original message box
				 * b) Negative
				 * c) Sequence coverage: 84'2%
				 * d) Data coverage: -
				 * 
				 */
				"reader0", "Notification Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #2 | An reader edit a original message box
				 * b) Negative
				 * c) Sequence coverage: 84'2%
				 * d) Data coverage: -
				 * 
				 */
				"reader0", "Spam Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #2 | An admin edit a original message box
				 * b) Negative
				 * c) Sequence coverage: 84'2%
				 * d) Data coverage: -
				 * 
				 */
				"admin", "In Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #2 | An admin edit a original message box
				 * b) Negative
				 * c) Sequence coverage: 84'2%
				 * d) Data coverage: -
				 * 
				 */
				"admin", "Out Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #2 | An admin edit a original message box
				 * b) Negative
				 * c) Sequence coverage: 84'2%
				 * d) Data coverage: -
				 * 
				 */
				"admin", "Trash Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #2 | An admin edit a original message box
				 * b) Negative
				 * c) Sequence coverage: 84'2%
				 * d) Data coverage: -
				 * 
				 */
				"admin", "Notification Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #2 | An admin edit a original message box
				 * b) Negative
				 * c) Sequence coverage: 84'2%
				 * d) Data coverage: -
				 * 
				 */
				"admin", "Spam Box", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editOriginalBoxesTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	protected void editOriginalBoxesTemplate(final String user, final String nameBox, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(user);

			final int idActor = this.getEntityId(user);

			final MessageBox originalBox = this.messageBoxService.findOriginalBox(idActor, nameBox);

			originalBox.setName("Hola");

			this.messageBoxService.save(originalBox);
			this.messageBoxService.flush();

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	/**
	 * #3 Test for Case use: An actor can delete message boxes
	 * 0 positive
	 * 7 negative
	 */
	@Test
	public void deleteOriginalBoxesDriver() {
		final Object testingData[][] = {
			{
				/**
				 * a) #3 | An Actor delete his box
				 * b) Negative
				 * c) Sequence coverage: 2%
				 * d) Data coverage: -
				 * 
				 */
				"reader0", "reader0", "In Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #3 | An Actor delete his box
				 * b) Negative
				 * c) Sequence coverage: 2%
				 * d) Data coverage: -
				 * 
				 */
				"reader0", "reader0", "Out Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #3 | An Actor delete his box
				 * b) Negative
				 * c) Sequence coverage: 2%
				 * d) Data coverage: -
				 * 
				 */
				"reader0", "reader0", "Trash Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #3 | An Actor delete his box
				 * b) Negative
				 * c) Sequence coverage: 2%
				 * d) Data coverage: -
				 * 
				 */
				"reader0", "reader0", "Notification Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #3 | An Actor delete his In box
				 * b) Negative
				 * c) Sequence coverage: 2%
				 * d) Data coverage: -
				 * 
				 */
				"reader0", "reader0", "Spam Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #3 | An Actor delete someone else's box
				 * b) Negative
				 * c) Sequence coverage: 2%
				 * d) Data coverage: -
				 * 
				 */
				"reader0", "admin", "In Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #3 | An Actor delete someone else's box
				 * b) Negative
				 * c) Sequence coverage: 2%
				 * d) Data coverage: -
				 * 
				 */
				"reader0", "admin", "Out Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #3 | An Actor delete someone else's box
				 * b) Negative
				 * c) Sequence coverage: 2%
				 * d) Data coverage: -
				 * 
				 */
				"reader0", "admin", "Trash Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #3 | An Actor delete his In box
				 * b) Negative
				 * c) Sequence coverage: 2%
				 * d) Data coverage: -
				 * 
				 */
				"reader0", "admin", "Notification Box", IllegalArgumentException.class
			}, {
				/**
				 * a) #3 | An Actor delete someone else's box
				 * b) Negative
				 * c) Sequence coverage: 2%
				 * d) Data coverage: -
				 * 
				 */
				"reader0", "admin", "Spam Box", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteOriginalBoxesTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	protected void deleteOriginalBoxesTemplate(final String user, final String owner, final String nameBox, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(user);

			final int idActor = this.getEntityId(user);

			final MessageBox originalBox = this.messageBoxService.findOriginalBox(idActor, nameBox);
			this.messageBoxService.delete(originalBox);

			this.messageBoxService.flush();

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	/**
	 * #3 Test for Case use: An actor can delete message boxes
	 * Positive
	 * c) Sequence coverage: 100%
	 * d) Data coverage: -
	 */
	@Test
	public void deleteMessageBox() {
		super.authenticate("reader0");
		final int idActor = this.getEntityId("reader0");

		final MessageBox messageBox = this.messageBoxService.create();

		messageBox.setDeleteable(true);
		messageBox.setName("Nombre valido");
		messageBox.setMessages(new ArrayList<Message>());

		final MessageBox boxSaved = this.messageBoxService.save(messageBox);

		this.messageBoxService.flush();

		Assert.isTrue(this.messageBoxService.findAllMessageBoxByActor(idActor).size() == 6);

		this.messageBoxService.delete(boxSaved);

		Assert.isTrue(this.messageBoxService.findAllMessageBoxByActor(idActor).size() == 5);

		super.unauthenticate();
	}

	/**
	 * #2 Test for Case use: An actor can edit message boxes
	 * Positive
	 * c) Sequence coverage: 100%
	 * d) Data coverage: -
	 */
	@Test
	public void editMessageBox() {
		super.authenticate("reader0");

		final MessageBox messageBox = this.messageBoxService.create();

		messageBox.setDeleteable(true);
		messageBox.setName("Prueba");
		messageBox.setMessages(new ArrayList<Message>());

		final MessageBox boxSaved = this.messageBoxService.save(messageBox);

		this.messageBoxService.flush();

		boxSaved.setName("editado");

		final MessageBox boxEdited = this.messageBoxService.save(boxSaved);

		this.messageBoxService.flush();

		Assert.isTrue(boxEdited.getName().equals("editado"), boxEdited.getName());

		super.unauthenticate();
	}
}
