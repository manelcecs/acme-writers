
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;

@Entity
@Access(AccessType.PROPERTY)
public class Reader extends Actor {

	private Collection<Writer>	writers;
	private Finder				finder;
	private Collection<Book>	books;


	@Valid
	@ManyToMany
	public Collection<Writer> getWriters() {
		return this.writers;
	}
	public void setWriters(final Collection<Writer> writers) {
		this.writers = writers;
	}

	@Valid
	@OneToOne(optional = false)
	public Finder getFinder() {
		return this.finder;
	}
	public void setFinder(final Finder finder) {
		this.finder = finder;
	}

	@ManyToMany
	@Valid
	public Collection<Book> getBooks() {
		return this.books;
	}
	public void setBooks(final Collection<Book> books) {
		this.books = books;
	}

}
