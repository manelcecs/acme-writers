
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.GenreRepository;
import utiles.AuthorityMethods;
import domain.Book;
import domain.Genre;

@Service
@Transactional
public class GenreService {

	@Autowired
	private GenreRepository	genreRepository;

	@Autowired
	private BookService		bookService;


	public Genre create() {
		return new Genre();
	}

	public Collection<Genre> findAll() {
		return this.genreRepository.findAll();
	}

	public Genre findOne(final int idGenre) {
		return this.genreRepository.findOne(idGenre);
	}

	public Collection<Genre> findAllMinusGENRE() {
		return this.genreRepository.findAllMinusGENRE();
	}

	public Genre save(final Genre genre) {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		Assert.isTrue(!genre.getNameES().equals("GÉNERO") && !genre.getNameEN().equals("GENRE"));
		final Genre generalGenre = this.genreRepository.getGeneralGenre();
		Assert.isTrue(genre.getId() != generalGenre.getId());

		if (genre.getParent() == null)
			genre.setParent(generalGenre);

		return this.genreRepository.save(genre);
	}
	public Collection<String> getAllNameES() {
		return this.genreRepository.getAllNameES();
	}

	public Collection<String> getAllNameEN() {
		return this.genreRepository.getAllNameEN();
	}

	//TODO Falta lo del tema del finde
	public void delete(final Genre genre) {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		final Genre generalGenre = this.genreRepository.getGeneralGenre();
		Assert.isTrue(genre.getId() != generalGenre.getId());

		final Collection<Book> bookWithThisGenre = this.bookService.getBooksByGenre(genre.getId());

		for (final Book book : bookWithThisGenre) {
			book.setGenre(genre.getParent());
			this.bookService.updateGenre(book);
		}

		final Collection<Genre> subgenres = this.genreRepository.getSubgenres(genre.getId());
		final Genre genreParent = genre.getParent();

		for (final Genre subgenre : subgenres) {
			subgenre.setParent(genreParent);
			this.genreRepository.save(subgenre);
		}

		this.genreRepository.delete(genre);

	}
}
