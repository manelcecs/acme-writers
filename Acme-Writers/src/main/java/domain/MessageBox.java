
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "name"), @Index(columnList = "deleteable"), @Index(columnList = "parent")
})
public class MessageBox extends DomainEntity {

	private String				name;
	@JsonIgnore
	private boolean				deleteable;
	@JsonIgnore
	private Collection<Message>	messages;
	private MessageBox			parent;


	@NotBlank
	@SafeHtml
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public boolean getDeleteable() {
		return this.deleteable;
	}

	public void setDeleteable(final boolean deleteable) {
		this.deleteable = deleteable;
	}

	@Valid
	@ManyToMany(mappedBy = "messageBoxes")
	public Collection<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(final Collection<Message> messages) {
		this.messages = messages;
	}

	@Valid
	@ManyToOne(optional = true)
	public MessageBox getParent() {
		return this.parent;
	}

	public void setParent(final MessageBox parent) {
		this.parent = parent;
	}

}
