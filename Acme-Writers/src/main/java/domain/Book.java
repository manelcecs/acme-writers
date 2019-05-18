
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Book extends DomainEntity {

	private String						title;
	private String						description;
	private String						language;
	private String						cover;
	private boolean						cancelled;
	private String						status;
	private boolean						draft;
	private Double						score;
	private Integer						numWords;

	private Ticker						ticker;
	private Genre						genre;
	private Collection<Participation>	participations;
	private Publisher					publisher;
	private Writer						writer;


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
	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(final String language) {
		this.language = language;
	}

	@Valid
	@OneToOne(optional = false)
	public Ticker getTicker() {
		return this.ticker;
	}

	public void setTicker(final Ticker ticker) {
		this.ticker = ticker;
	}

	@SafeHtml
	@URL
	public String getCover() {
		return this.cover;
	}

	public void setCover(final String cover) {
		this.cover = cover;
	}

	public boolean getCancelled() {
		return this.cancelled;
	}

	public void setCancelled(final boolean cancelled) {
		this.cancelled = cancelled;
	}

	@NotBlank
	@SafeHtml
	@Pattern(regexp = "^INDEPENDENT|PENDING|REJECTED|ACCEPTED$")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public boolean getDraft() {
		return this.draft;
	}

	public void setDraft(final boolean draft) {
		this.draft = draft;
	}

	@Min(0)
	public Double getScore() {
		return this.score;
	}

	public void setScore(final Double score) {
		this.score = score;
	}

	@Min(1)
	@NotNull
	public Integer getNumWords() {
		return this.numWords;
	}

	public void setNumWords(final Integer numWords) {
		this.numWords = numWords;
	}

	@ManyToOne(optional = true)
	@Valid
	public Genre getGenre() {
		return this.genre;
	}

	public void setGenre(final Genre genre) {
		this.genre = genre;
	}

	@ManyToMany
	@Valid
	public Collection<Participation> getParticipations() {
		return this.participations;
	}

	public void setParticipations(final Collection<Participation> participations) {
		this.participations = participations;
	}

	@ManyToOne(optional = true)
	@Valid
	public Publisher getPublisher() {
		return this.publisher;
	}

	public void setPublisher(final Publisher publisher) {
		this.publisher = publisher;
	}

	@ManyToOne(optional = false)
	@Valid
	public Writer getWriter() {
		return this.writer;
	}

	public void setWriter(final Writer writer) {
		this.writer = writer;
	}
}
