
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "reader"), @Index(columnList = "book, positiveOpinion"), @Index(columnList = "book")
})
public class Opinion extends DomainEntity {

	private Boolean	positiveOpinion;
	private String	review;
	private Date	moment;

	private Book	book;
	private Reader	reader;


	public Boolean getPositiveOpinion() {
		return this.positiveOpinion;
	}

	public void setPositiveOpinion(final Boolean positiveOpinion) {
		this.positiveOpinion = positiveOpinion;
	}

	@NotBlank
	@SafeHtml
	public String getReview() {
		return this.review;
	}

	public void setReview(final String review) {
		this.review = review;
	}

	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@NotNull
	public Date getMoment() {
		return this.moment;
	}

	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	@Valid
	@ManyToOne(optional = false)
	public Book getBook() {
		return this.book;
	}

	public void setBook(final Book book) {
		this.book = book;
	}

	@ManyToOne(optional = false)
	@Valid
	public Reader getReader() {
		return this.reader;
	}

	public void setReader(final Reader reader) {
		this.reader = reader;
	}

}
