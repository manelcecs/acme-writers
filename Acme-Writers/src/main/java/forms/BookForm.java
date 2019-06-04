
package forms;

import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

import domain.DomainEntity;
import domain.Genre;
import domain.Publisher;

public class BookForm extends DomainEntity {

	private String		title;
	private String		description;
	private String		lang;
	private String		cover;
	private Genre		genre;

	private Publisher	publisher;


	@SafeHtml
	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@SafeHtml
	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@SafeHtml
	@NotBlank
	@Pattern(regexp = "^EN|ES|IT|FR|DE|OTHER$")
	public String getLang() {
		return this.lang;
	}

	public void setLang(final String lang) {
		this.lang = lang;
	}

	@SafeHtml
	@URL
	public String getCover() {
		return this.cover;
	}

	public void setCover(final String cover) {
		this.cover = cover;
	}

	@ManyToOne(optional = true)
	@Valid
	public Genre getGenre() {
		return this.genre;
	}

	public void setGenre(final Genre genre) {
		this.genre = genre;
	}

	@ManyToOne(optional = true)
	@Valid
	public Publisher getPublisher() {
		return this.publisher;
	}

	public void setPublisher(final Publisher publisher) {
		this.publisher = publisher;
	}

}
