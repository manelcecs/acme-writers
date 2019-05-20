
package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MessageRepository;
import security.Authority;
import security.LoginService;
import utiles.AuthorityMethods;
import domain.Actor;
import domain.Message;
import domain.MessageBox;

@Service
@Transactional
public class MessageService {

	@Autowired
	private MessageRepository	messageRepository;

	@Autowired
	private MessageBoxService	messageBoxService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private AdminConfigService	adminConfigService;

	@Autowired
	private Validator			validator;


	public Message create() {
		final Message message = new Message();
		return message;
	}

	public Message save(final Message message) throws ParseException {
		Assert.isTrue(AuthorityMethods.checkIsSomeoneLogged());
		if (message.getId() == 0) {
			Assert.isTrue(this.actorService.findByUserAccount(LoginService.getPrincipal()).equals(message.getSender()));
			this.messageToBoxByDefault(message);
		}
		return this.messageRepository.save(message);
	}

	public Message findOne(final int id) {
		Assert.isTrue(AuthorityMethods.checkIsSomeoneLogged());
		return this.messageRepository.findOne(id);
	}

	private void messageToBoxByDefault(final Message message) {
		final Collection<MessageBox> boxes = new ArrayList<>();

		boxes.add(this.messageBoxService.findOriginalBox(message.getSender().getId(), "Out Box"));

		String boxName = "In Box";
		if (this.isABroadCastMessage(message))
			boxName = "Notification Box";

		final String text = this.allTextFromAMessage(message);

		for (final Actor recipient : message.getRecipients())
			if (this.adminConfigService.existSpamWord(text))
				boxes.add(this.messageBoxService.findOriginalBox(recipient.getId(), "Spam Box"));
			else
				boxes.add(this.messageBoxService.findOriginalBox(recipient.getId(), boxName));

		message.setMessageBoxes(boxes);
	}

	public void delete(final Message message) throws ParseException {
		Assert.isTrue(this.checkMessagePermissions(message));

		final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());

		final MessageBox trashBox = this.messageBoxService.findOriginalBox(actor.getId(), "Trash Box");
		final Collection<MessageBox> allBoxes = message.getMessageBoxes();
		final Collection<MessageBox> allBoxesByPrincipal = actor.getMessageBoxes();

		if (allBoxes.contains(trashBox))
			allBoxes.removeAll(allBoxesByPrincipal);
		else {
			allBoxes.removeAll(allBoxesByPrincipal);
			allBoxes.add(trashBox);
		}
		message.setMessageBoxes(allBoxes);
		this.save(message);

		if (message.getMessageBoxes().size() == 0) {
			message.setRecipients(null);
			this.messageRepository.delete(message);
		}
	}

	public Message reconstruct(final Message message, final BindingResult binding) throws ParseException {
		Message result;
		if (message.getId() == 0) {
			message.setSender(this.actorService.findByUserAccount(LoginService.getPrincipal()));
			message.setMoment(this.getDateNow());
			result = message;
		} else {
			result = this.messageRepository.findOne(message.getId());

			final Collection<MessageBox> boxesBD = result.getMessageBoxes();
			if (message.getMessageBoxes() != null)
				boxesBD.addAll(message.getMessageBoxes());
			result.setMessageBoxes(boxesBD);
		}

		this.validator.validate(result, binding);

		return result;
	}

	public Message removeFrom(Message message, final MessageBox messageBox) {
		Assert.isTrue(this.checkMessagePermissions(message));
		final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());

		final MessageBox trashBox = this.messageBoxService.findOriginalBox(actor.getId(), "Trash Box");

		final Collection<MessageBox> messageBoxesByPrincipal = this.messageBoxService.findAllMessageBoxByActorContainsAMessage(actor.getId(), message.getId());
		final Collection<MessageBox> messageBoxesByMessage = message.getMessageBoxes();

		if (messageBoxesByPrincipal.size() > 1)
			messageBoxesByMessage.remove(messageBox);
		else if (messageBox.equals(trashBox))
			messageBoxesByMessage.remove(messageBox);
		else if (!messageBox.equals(trashBox)) {
			messageBoxesByMessage.remove(messageBox);
			messageBoxesByMessage.add(trashBox);
		}

		if (messageBoxesByMessage.size() == 0)
			this.messageRepository.delete(message);
		else {
			message.setMessageBoxes(messageBoxesByMessage);
			message = this.messageRepository.save(message);
		}

		return message;
	}

	public Collection<Message> findAllByActor(final Integer actorId) {
		return this.messageRepository.findAllByActor(actorId);
	}

	public Collection<Actor> getRecipients(final int idMessage) {
		return this.messageRepository.getRecipients(idMessage);
	}

	public Integer getSpamMessages(final Collection<Message> allMessages) {
		Integer s = 0;
		String text = "";
		for (final Message m : allMessages) {
			text = m.getBody() + " " + m.getSubject();
			for (final String tag : m.getTags())
				text = text + " " + tag;
			if (this.adminConfigService.existSpamWord(text))
				s++;
		}
		return s;
	}

	//Utilities
	private Message initializeNotification(final Actor sender, final Collection<Actor> recipients, final String body) throws ParseException {
		final Message notification = this.create();

		notification.setSubject("System Notification | Notificacion del sistema");
		final Collection<String> tags = new ArrayList<>();
		tags.add("SYSTEM");
		notification.setTags(tags);
		notification.setPriority("HIGH");
		notification.setBody(body);
		notification.setRecipients(recipients);
		notification.setSender(sender);
		notification.setMoment(this.getDateNow());

		final Collection<MessageBox> messageBoxes = new ArrayList<>();
		for (final Actor recipient : recipients)
			messageBoxes.add(this.messageBoxService.findOriginalBox(recipient.getId(), "Notification Box"));

		notification.setMessageBoxes(messageBoxes);

		return notification;
	}

	private String allTextFromAMessage(final Message message) {
		String text = message.getBody() + " " + message.getSubject();
		if (message.getTags() != null)
			for (final String tag : message.getTags())
				text = text + " " + tag;
		return text;
	}

	private boolean isABroadCastMessage(final Message message) {
		boolean res = false;

		final Authority administrator = new Authority();
		administrator.setAuthority("ADMINISTRATOR");

		if ((message.getRecipients().size() == this.actorService.findAll().size() - 1) && (LoginService.getPrincipal().getAuthorities().contains(administrator)))
			res = true;

		return res;
	}

	private Date getDateNow() throws ParseException {
		final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		final LocalDateTime datetimenow = LocalDateTime.now();
		final String month = (datetimenow.getMonthOfYear() <= 9) ? "0" + datetimenow.getMonthOfYear() : datetimenow.getMonthOfYear() + "";
		final String day = (datetimenow.getDayOfMonth() <= 9) ? "0" + datetimenow.getDayOfMonth() : datetimenow.getDayOfMonth() + "";
		final String hour = (datetimenow.getHourOfDay() <= 9) ? "0" + datetimenow.getHourOfDay() : datetimenow.getHourOfDay() + "";
		final String minute = (datetimenow.getMinuteOfHour() <= 9) ? "0" + datetimenow.getMinuteOfHour() : datetimenow.getMinuteOfHour() + "";
		final String second = (datetimenow.getSecondOfMinute() <= 9) ? "0" + datetimenow.getSecondOfMinute() : datetimenow.getSecondOfMinute() + "";

		final Date actual = format.parse(datetimenow.getYear() + "/" + month + "/" + day + " " + hour + ":" + minute + ":" + second);

		return actual;
	}

	public boolean checkMessagePermissions(final Message message) {
		final Actor actorPrincipal = this.actorService.findByUserAccount(LoginService.getPrincipal());
		final Collection<Actor> recipients = this.getRecipients(message.getId());
		return message.getSender().equals(actorPrincipal) || recipients.contains(actorPrincipal);
	}

	public void flush() {
		this.messageRepository.flush();
	}

}
