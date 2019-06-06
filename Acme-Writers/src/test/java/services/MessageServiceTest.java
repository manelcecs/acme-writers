
package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Actor;
import domain.Book;
import domain.Message;
import domain.MessageBox;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class MessageServiceTest extends AbstractTest {

	@Autowired
	private MessageService			messageService;

	@Autowired
	private MessageBoxService		messageBoxService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private BookService				bookService;

	@Autowired
	private SponsorshipService		sponsorshipService;

	private final SimpleDateFormat	FORMAT	= new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");


	/**
	 * #1 Test for Case use: An actor sends message
	 * 1 positive
	 * 7 negative
	 */
	@Test
	public void sendMessageDriver() throws ParseException {
		final Object testingData[][] = {
			{
				/**
				 * a) #1 | An actor sends a message with legacy data
				 * b) Positive
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"reader0", "reader0", "reader1", "body", "subject", "tag", "HIGH", null
			}, {
				/**
				 * a) #1 | An actor sends a message with a blank body
				 * b) Negative
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"reader0", "reader0", "reader1", "", "subject", "tag", "HIGH", ConstraintViolationException.class
			}, {
				/**
				 * a) #1 | An actor sends a message with a blank subject
				 * b) Negative
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"reader0", "reader0", "reader1", "body", "", "tag", "HIGH", ConstraintViolationException.class
			}, {
				/**
				 * a) #1 | An actor sends a message with a blank priority
				 * b) Negative
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"reader0", "reader0", "reader1", "body", "subject", "tag", "", ConstraintViolationException.class
			}, {
				/**
				 * a) #1 | An actor sends a message with a unlegal priority
				 * b) Negative
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"reader0", "reader0", "reader1", "body", "subject", "tag", "No priority", ConstraintViolationException.class
			}, {
				/**
				 * a) #1 | An actor sends a message with legacy data but the sender it not the same as logged actor
				 * b) Negative
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"reader0", "writer0", "reader1", "body", "subject", "tag", "HIGH", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.sendMessageTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}

	protected void sendMessageTemplate(final String userLogged, final String sender, final String recipient, final String body, final String subject, final String tag, final String priority, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(userLogged);

			final int idSender = this.getEntityId(sender);
			final int idRecipient = this.getEntityId(recipient);

			final Message message = this.messageService.create();
			message.setSender(this.actorService.getActor(idSender));
			message.setBody(body);
			message.setSubject(subject);

			final Collection<String> tags = new ArrayList<>();
			tags.add(tag);
			message.setTags(tags);

			message.setMoment(this.dateNow());

			message.setPriority(priority);

			final Collection<Actor> recipients = new ArrayList<>();
			recipients.add(this.actorService.getActor(idRecipient));
			message.setRecipients(recipients);

			this.messageService.save(message);
			this.messageService.flush();

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	/**
	 * #2 Test for Case use: An actor delete a message
	 * 2 positive
	 * 4 negative
	 */
	@Test
	public void deleteMessageDriver() throws ParseException {
		final Object testingData[][] = {
			{
				/**
				 * a) #2 | Sender delete a message
				 * b) Positive
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"reader0", "reader0", "reader1", null
			}, {
				/**
				 * a) #2 | Recipient delete a message
				 * b) Positive
				 * c) Sequence coverage: 100%
				 * d) Data coverage: 100%
				 * 
				 */
				"reader1", "reader0", "reader1", null
			}, {
				/**
				 * a) #2 | An writer delete a message that is not yours
				 * b) Negative
				 * c) Sequence coverage: 7%
				 * d) Data coverage: 100%
				 * 
				 */
				"writer0", "reader0", "reader1", IllegalArgumentException.class
			}, {
				/**
				 * a) #2 | An admin delete a message that is not yours
				 * b) Negative
				 * c) Sequence coverage: 7%
				 * d) Data coverage: 100%
				 * 
				 */
				"admin", "reader0", "reader1", IllegalArgumentException.class
			}, {
				/**
				 * a) #2 | An reader delete a message that is not yours
				 * b) Negative
				 * c) Sequence coverage: 7%
				 * d) Data coverage: 100%
				 * 
				 */
				"reader2", "reader0", "reader1", IllegalArgumentException.class
			}, {
				/**
				 * a) #2 | An actor not logged delete a message that is not yours
				 * b) Negative
				 * c) Sequence coverage: 7%
				 * d) Data coverage: 100%
				 * 
				 */
				null, "reader0", "reader1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteMessageTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	protected void deleteMessageTemplate(final String userLogged, final String sender, final String recipient, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(sender);

			final int idSender = this.getEntityId(sender);
			final int idRecipient = this.getEntityId(recipient);

			final Message message = this.messageService.create();
			message.setSender(this.actorService.getActor(idSender));
			message.setBody("Body");
			message.setSubject("Subject");
			message.setMoment(this.dateNow());
			message.setTags(null);
			message.setPriority("HIGH");
			final Collection<Actor> recipients = new ArrayList<>();
			recipients.add(this.actorService.getActor(idRecipient));
			message.setRecipients(recipients);

			final Message messageBD = this.messageService.save(message);
			this.messageService.flush();
			super.unauthenticate();

			super.authenticate(userLogged);

			this.messageService.delete(messageBD);
			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	/**
	 * a)#3 | Test for Case use: An actor sends a spam message to another actor
	 * b)Positive
	 * c)Sequence coverage: 100%
	 * d)Data coverage: 100%
	 */
	@Test
	public void spamMessage() throws ParseException {
		super.authenticate("reader0");

		final int idSender = this.getEntityId("reader0");
		final int idRecipient = this.getEntityId("reader1");

		final Message message = this.messageService.create();
		message.setSender(this.actorService.getActor(idSender));
		message.setBody("sexo"); //Spam Word
		message.setMoment(this.dateNow());
		message.setPriority("HIGH");
		final Collection<String> tags = new ArrayList<>();
		message.setTags(tags);
		final Collection<Actor> recipients = new ArrayList<>();
		recipients.add(this.actorService.getActor(idRecipient));
		message.setRecipients(recipients);
		message.setSubject("Subject");

		final Message messageSaved = this.messageService.save(message);
		this.messageService.flush();

		final Message messageSpam = this.messageService.spamMessageDetector(messageSaved);
		this.messageService.flush();

		final Collection<MessageBox> boxes = messageSpam.getMessageBoxes();

		final MessageBox spamBoxRecipient = this.messageBoxService.findOriginalBox(idRecipient, "Spam Box");

		Assert.isTrue(boxes.contains(spamBoxRecipient));

		this.messageService.flush();
	}

	/**
	 * a)#3 | Test for Case use: An actor sends a spam message to another actor
	 * b)Negative
	 * c)Sequence coverage: 60%
	 * d)Data coverage: 100%
	 */
	@Test(expected = IllegalArgumentException.class)
	public void spamMessageNegative() throws ParseException {
		super.authenticate(null);

		final int idSender = this.getEntityId("reader0");
		final int idRecipient = this.getEntityId("reader1");

		final Message message = this.messageService.create();
		message.setSender(this.actorService.getActor(idSender));
		message.setBody("sexo"); //Spam Word
		message.setMoment(this.dateNow());
		message.setPriority("HIGH");
		final Collection<Actor> recipients = new ArrayList<>();
		recipients.add(this.actorService.getActor(idRecipient));
		final Collection<String> tags = new ArrayList<>();
		message.setTags(tags);
		message.setRecipients(recipients);
		message.setSubject("Subject");

		final Message messageSaved = this.messageService.save(message);
		this.messageService.flush();

		final Message messageSpam = this.messageService.spamMessageDetector(messageSaved);
		this.messageService.flush();

		final Collection<MessageBox> boxes = messageSpam.getMessageBoxes();

		final MessageBox spamBoxRecipient = this.messageBoxService.findOriginalBox(idRecipient, "Spam Box");

		Assert.isTrue(boxes.contains(spamBoxRecipient));

		this.messageService.flush();
	}

	/**
	 * a)#4 Test for Case use: Administrator send a broadcast message
	 * b)Positive
	 * c)Sequence coverage: 100%
	 * d)Data coverage: 100%
	 */
	@Test
	public void broadCastMessage() throws ParseException {
		super.authenticate("admin");

		final int idSender = this.getEntityId("admin");

		final Message message = this.messageService.create();
		message.setSender(this.actorService.getActor(idSender));
		message.setBody("notification");
		message.setMoment(this.dateNow());
		message.setPriority("HIGH");
		final List<Actor> recipients = (List<Actor>) this.actorService.findNonEliminatedActors();
		recipients.remove(this.actorService.getActor(idSender));
		message.setRecipients(recipients);

		message.setTags(null);
		message.setSubject("Subject");

		final Message messageSaved = this.messageService.save(message);
		this.messageService.flush();

		final Collection<MessageBox> boxes = messageSaved.getMessageBoxes();

		final MessageBox notificationBox = this.messageBoxService.findOriginalBox(recipients.get(0).getId(), "Notification Box");

		Assert.isTrue(boxes.contains(notificationBox));

		this.messageService.flush();
	}

	/**
	 * a)#4 Test for Case use: Administrator send a broadcast message
	 * b)Negative
	 * c)Sequence coverage: 72%
	 * d)Data coverage: 100%
	 */
	@Test(expected = IllegalArgumentException.class)
	public void broadCastMessageFail() throws ParseException {
		super.authenticate("reader0");

		final int idSender = this.getEntityId("reader0");

		final Message message = this.messageService.create();
		message.setSender(this.actorService.getActor(idSender));
		message.setBody("notification");
		message.setMoment(this.dateNow());
		message.setPriority("HIGH");
		final List<Actor> recipients = (List<Actor>) this.actorService.findNonEliminatedActors();
		recipients.remove(this.actorService.getActor(idSender));
		message.setRecipients(recipients);

		message.setTags(null);
		message.setSubject("Subject");

		final Message messageSaved = this.messageService.save(message);
		this.messageService.flush();

		final Collection<MessageBox> boxes = messageSaved.getMessageBoxes();

		final MessageBox notificationBox = this.messageBoxService.findOriginalBox(recipients.get(0).getId(), "Notification Box");

		Assert.isTrue(boxes.contains(notificationBox));

		this.messageService.flush();
	}

	//utiles
	private Date dateNow() throws ParseException {
		final LocalDateTime DateTimeNow = LocalDateTime.now();
		final Date moment = this.FORMAT.parse(DateTimeNow.getYear() + "/" + DateTimeNow.getMonthOfYear() + "/" + DateTimeNow.getDayOfMonth() + " " + DateTimeNow.getHourOfDay() + ":" + DateTimeNow.getMinuteOfHour() + ":" + DateTimeNow.getSecondOfMinute());
		return moment;
	}

	/**
	 * a)#5 Test for Case use: Cuando una editorial cambie el estado de un libro, el escritor debe recibir una notificacion informativa
	 * b)
	 * c)Sequence coverage: 100%
	 * d)Data coverage:
	 * 
	 * @throws ParseException
	 */
	@Test
	public void notificationWhenStatusOfBookIsChanged() throws ParseException {

		super.authenticate("writer0");
		final Book book = this.bookService.changeDraft(this.getEntityId("book1"));
		this.bookService.flush();
		super.unauthenticate();

		super.authenticate("publisher0");
		final Book bookCS = this.bookService.changeStatus(this.getEntityId("book1"), "ACCEPTED");
		this.bookService.flush();
		super.unauthenticate();

		super.authenticate("writer0");
		final MessageBox messageBox = this.messageBoxService.findOne(this.getEntityId("messageBox39"));
		this.messageBoxService.flush();
		Assert.isTrue(messageBox.getMessages().size() != 0);
		super.unauthenticate();

	}

	/**
	 * a)#6 Test for Case use: Los patrocinadores deben recibir una notificación cuando se cancelen sus patrocinios
	 * b)
	 * c)Sequence coverage: 100%
	 * d)Data coverage:
	 * 
	 * @throws ParseException
	 */
	@Test
	public void notificationWhenSponsorpshipIsCancelled() throws ParseException {

		super.authenticate("admin");
		this.sponsorshipService.cancelSponsorshipCaducate();
		this.sponsorshipService.flush();
		super.unauthenticate();

		super.authenticate("sponsor2");
		final MessageBox messageBox = this.messageBoxService.findOne(this.getEntityId("messageBox19"));
		this.messageBoxService.flush();
		Assert.isTrue(messageBox.getMessages().size() != 0);
		super.unauthenticate();
	}
}
