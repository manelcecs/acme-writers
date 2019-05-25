
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Contest extends DomainEntity {

	//private String				title;
	private String				description;
	private String				prize;
	private Collection<String>	rules;
	private Date				deadline;

	private Publisher			publisher;


	//	@SafeHtml
	//	@NotBlank
	//	public String getTitle() {
	//		return this.title;
	//	}
	//
	//	public void setTitle(final String title) {
	//		this.title = title;
	//	}

	@SafeHtml
	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@NotBlank
	@SafeHtml
	public String getPrize() {
		return this.prize;
	}

	public void setPrize(final String prize) {
		this.prize = prize;
	}

	@ElementCollection
	public Collection<String> getRules() {
		return this.rules;
	}

	public void setRules(final Collection<String> rules) {
		this.rules = rules;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@NotNull
	public Date getDeadline() {
		return this.deadline;
	}

	public void setDeadline(final Date deadline) {
		this.deadline = deadline;
	}

	@Valid
	@ManyToOne(optional = false)
	public Publisher getPublisher() {
		return this.publisher;
	}

	public void setPublisher(final Publisher publisher) {
		this.publisher = publisher;
	}

}
