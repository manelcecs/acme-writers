
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.GenreRepository;
import utiles.AuthorityMethods;
import domain.Book;
import domain.Finder;
import domain.Genre;

@Service
@Transactional
public class GenreService {

	@Autowired
	private GenreRepository	genreRepository;

	@Autowired
	private BookService		bookService;

	@Autowired
	private FinderService	finderService;


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

		Assert.isTrue(genre.getId() != genre.getParent().getId());

		final Collection<Genre> childrens = this.getChildrenOfAGenre(genre);
		Assert.isTrue(!childrens.contains(genre.getParent()));

		return this.genreRepository.save(genre);
	}
	public Collection<String> getAllNameES() {
		return this.genreRepository.getAllNameES();
	}

	public Collection<String> getAllNameEN() {
		return this.genreRepository.getAllNameEN();
	}

	public void delete(final Genre genre) {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("ADMINISTRATOR"));
		final Genre generalGenre = this.genreRepository.getGeneralGenre();
		Assert.isTrue(genre.getId() != generalGenre.getId());
		final Genre genreParent = genre.getParent();

		final Collection<Book> bookWithThisGenre = this.bookService.getBooksByGenre(genre.getId());

		for (final Book book : bookWithThisGenre) {
			book.setGenre(genreParent);
			this.bookService.updateGenre(book);
		}

		final Collection<Finder> finderWithThisGenre = this.finderService.getFindersByGenre(genre.getId());

		for (final Finder finder : finderWithThisGenre) {
			finder.setGenre(genreParent);
			this.finderService.updateGenre(finder);
		}

		final Collection<Genre> subgenres = this.genreRepository.getSubgenres(genre.getId());

		for (final Genre subgenre : subgenres) {
			subgenre.setParent(genreParent);
			this.genreRepository.save(subgenre);
		}

		this.genreRepository.delete(genre);

	}

	public Collection<Genre> getChildren(final int id) {
		return this.genreRepository.getChildren(id);
	}

	public Collection<Genre> getChildrenOfAGenre(final Genre genre) {
		final Collection<Genre> acum = new ArrayList<>();
		return this.getChildrenOfAGenre(genre, acum);
	}

	private Collection<Genre> getChildrenOfAGenre(final Genre genre, final Collection<Genre> acum) {
		final Collection<Genre> childrens = this.getChildren(genre.getId());
		if (childrens.size() == 0)
			acum.add(genre);
		else {
			for (final Genre child : childrens)
				this.getChildrenOfAGenre(child, acum);
			acum.add(genre);
		}
		return acum;
	}

	public void flush() {
		this.genreRepository.flush();
	}
}
