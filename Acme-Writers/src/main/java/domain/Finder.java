
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Finder extends DomainEntity {

	//Atributes
	private String				keyWord;
	private Integer				minNumWords;
	private Integer				maxNumWords;
	private String				language;
	private Date				lastUpdate;

	//Relationship
	private Collection<Book>	books;
	private Genre				genre;


	//Atributes getters and setters
	@SafeHtml
	public String getKeyWord() {
		return this.keyWord;
	}

	public void setKeyWord(final String keyWord) {
		this.keyWord = keyWord;
	}

	@Min(1)
	public Integer getMinNumWords() {
		return this.minNumWords;
	}

	public void setMinNumWords(final Integer minNumWords) {
		this.minNumWords = minNumWords;
	}

	@Min(1)
	public Integer getMaxNumWords() {
		return this.maxNumWords;
	}

	public void setMaxNumWords(final Integer maxNumWords) {
		this.maxNumWords = maxNumWords;
	}

	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(final String language) {
		this.language = language;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@NotNull
	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(final Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@ManyToMany
	@Valid
	public Collection<Book> getBooks() {
		return this.books;
	}

	public void setBooks(final Collection<Book> books) {
		this.books = books;
	}

	@ManyToOne(optional = true)
	@Valid
	public Genre getGenre() {
		return this.genre;
	}

	public void setGenre(final Genre genre) {
		this.genre = genre;
	}

}
