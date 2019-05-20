
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ActorRepository;
import repositories.MessageBoxRepository;
import repositories.MessageRepository;
import security.LoginService;
import domain.Actor;
import domain.Message;
import domain.MessageBox;

@Service
@Transactional
public class MessageBoxService {

	@Autowired
	private MessageBoxRepository	messageBoxRepository;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ActorRepository			actorRepository;

	@Autowired
	private MessageRepository		messageRepository;

	@Autowired
	private Validator				validator;


	public MessageBox create() {
		return new MessageBox();
	}

	public MessageBox save(final MessageBox messageBox) {
		Assert.isTrue(this.notLikeOriginalName(messageBox.getName()));

		final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
		final MessageBox boxSave = this.messageBoxRepository.save(messageBox);

		if (messageBox.getId() == 0) {
			final Collection<MessageBox> messageBoxes = actor.getMessageBoxes();
			messageBoxes.add(boxSave);
			actor.setMessageBoxes(messageBoxes);
			this.actorService.save(actor);
		} else {
			final MessageBox boxBD = this.messageBoxRepository.findOne(messageBox.getId());
			Assert.isTrue(boxBD.getDeleteable() == messageBox.getDeleteable());
			Assert.isTrue(messageBox.getDeleteable());
		}

		return boxSave;
	}

	public void delete(final MessageBox messageBox) {
		Assert.isTrue(messageBox.getDeleteable());
		final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
		Assert.isTrue(this.actorService.getByMessageBox(messageBox.getId()).equals(actor));

		final Collection<MessageBox> childrens = this.allChildren(messageBox);
		final Collection<Message> messagesToTrashBox = new ArrayList<>();
		final MessageBox trashBox = this.findOriginalBox(actor.getId(), "Trash Box");

		for (final MessageBox childrensAndFather : childrens)
			if (childrensAndFather.getMessages().size() > 0) {
				messagesToTrashBox.addAll(childrensAndFather.getMessages());
				for (final Message messageInBox : childrensAndFather.getMessages()) {
					final Collection<MessageBox> boxes = messageInBox.getMessageBoxes();
					boxes.remove(childrensAndFather);
					if (boxes.size() == 0)
						boxes.add(trashBox);
					messageInBox.setMessageBoxes(boxes);
					this.messageRepository.save(messageInBox);
				}
				childrensAndFather.setMessages(null);
				this.messageBoxRepository.save(childrensAndFather);
			}

		final Collection<Message> trashBoxMessages = trashBox.getMessages();
		trashBoxMessages.addAll(messagesToTrashBox);
		trashBox.setMessages(trashBoxMessages);
		this.messageBoxRepository.save(trashBox);

		final Collection<MessageBox> boxesActor = actor.getMessageBoxes();
		boxesActor.removeAll(childrens);
		actor.setMessageBoxes(boxesActor);
		this.actorRepository.save(actor);

		this.messageBoxRepository.delete(childrens);
	}

	public Collection<MessageBox> findBoxToMove(final Message message) {
		final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
		final Collection<MessageBox> messageBoxesToMove = this.findAllMessageBoxByActor(actor.getId());

		final MessageBox trashBox = this.messageBoxRepository.findOriginalBox(actor.getId(), "Trash Box");
		final MessageBox inBox = this.messageBoxRepository.findOriginalBox(actor.getId(), "In Box");
		final MessageBox outBox = this.messageBoxRepository.findOriginalBox(actor.getId(), "Out Box");
		final MessageBox notificationBox = this.messageBoxRepository.findOriginalBox(actor.getId(), "Notification Box");

		messageBoxesToMove.removeAll(message.getMessageBoxes());
		messageBoxesToMove.remove(trashBox);
		messageBoxesToMove.remove(inBox);
		messageBoxesToMove.remove(outBox);
		messageBoxesToMove.remove(notificationBox);

		return messageBoxesToMove;
	}

	public Collection<MessageBox> initializeNewUserBoxes() {
		final List<MessageBox> initialBoxes = new ArrayList<>();

		MessageBox inBox = this.create();
		inBox.setDeleteable(false);
		inBox.setName("In Box");
		inBox = this.messageBoxRepository.save(inBox);

		MessageBox outBox = this.create();
		outBox.setDeleteable(false);
		outBox.setName("Out Box");
		outBox = this.messageBoxRepository.save(outBox);

		MessageBox spamBox = this.create();
		spamBox.setDeleteable(false);
		spamBox.setName("Spam Box");
		spamBox = this.messageBoxRepository.save(spamBox);

		MessageBox trashBox = this.create();
		trashBox.setDeleteable(false);
		trashBox.setName("Trash Box");
		trashBox = this.messageBoxRepository.save(trashBox);

		MessageBox notificationBox = this.create();
		notificationBox.setName("Notification Box");
		notificationBox.setDeleteable(false);
		notificationBox = this.messageBoxRepository.save(notificationBox);

		initialBoxes.add(inBox);
		initialBoxes.add(outBox);
		initialBoxes.add(trashBox);
		initialBoxes.add(spamBox);
		initialBoxes.add(notificationBox);

		return initialBoxes;
	}

	public MessageBox reconstruct(final MessageBox messageBox, final BindingResult binding) {
		MessageBox result;

		if (messageBox.getId() == 0) {
			result = this.create();
			result.setMessages(null);
			result.setDeleteable(true);
			if (this.likeOriginalName(messageBox.getName()))
				binding.rejectValue("name", "messageBox.error.OriginalName");
			else if (this.likeOtherBox(messageBox.getName()))
				binding.rejectValue("name", "messageBox.error.likeOtherBox");
		} else {
			result = this.findOne(messageBox.getId());
			if (this.likeOriginalName(messageBox.getName()))
				binding.rejectValue("name", "messageBox.error.OriginalName");
			else if (!result.getName().equals(messageBox.getName()))
				if (this.likeOtherBox(messageBox.getName()))
					binding.rejectValue("name", "messageBox.error.likeOtherBox");
		}

		result.setParent(messageBox.getParent());
		result.setName(messageBox.getName());

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}
	public MessageBox findOne(final int id) {
		return this.messageBoxRepository.findOne(id);
	}

	public MessageBox findOriginalBox(final int id, final String string) {
		return this.messageBoxRepository.findOriginalBox(id, string);
	}

	public Collection<MessageBox> findAllMessageBoxByActor(final int id) {
		return this.messageBoxRepository.findAllMessageBoxByActor(id);
	}

	public Collection<MessageBox> findPosibleParents(final int id) {
		return this.messageBoxRepository.findPosibleParents(id);
	}

	public Collection<MessageBox> findChildren(final int id) {
		return this.messageBoxRepository.findChildren(id);
	}

	public Collection<MessageBox> findAllMessageBoxByActorContainsAMessage(final int idActor, final int idMessage) {
		return this.messageBoxRepository.findAllMessageBoxByActorContainsAMessage(idActor, idMessage);
	}

	//Utilities
	private boolean likeOriginalName(final String name) {
		return !this.notLikeOriginalName(name);
	}

	private boolean notLikeOriginalName(final String name) {
		boolean res = false;

		final String nameTrim = name.toUpperCase().replace(" ", "");

		if (!(nameTrim.equals("INBOX") || nameTrim.equals("TRASHBOX") || nameTrim.equals("OUTBOX") || nameTrim.equals("SPAMBOX") || nameTrim.equals("NOTIFICATIONBOX")))
			res = true;

		return res;
	}

	private boolean likeOtherBox(final String nameBox) {
		Boolean res = true;
		final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());

		if (this.messageBoxRepository.findAllNameMessageBoxOfActor(actor.getId(), nameBox.trim()) == null)
			res = false;

		return res;
	}

	private Collection<MessageBox> allChildren(final MessageBox box) {
		final Collection<MessageBox> acum = new ArrayList<>();
		return this.allChildren(box, acum);
	}

	private Collection<MessageBox> allChildren(final MessageBox box, final Collection<MessageBox> acum) {
		final Collection<MessageBox> childrens = this.findChildren(box.getId());
		if (childrens.size() == 0)
			acum.add(box);
		else {
			for (final MessageBox child : childrens)
				this.allChildren(child, acum);
			acum.add(box);
		}
		return acum;
	}

	public void flush() {
		this.messageBoxRepository.flush();
	}

}
