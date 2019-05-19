
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.GenreRepository;
import domain.Genre;

@Service
@Transactional
public class GenreService {

	@Autowired
	private GenreRepository	genreRepository;


	public Collection<Genre> findAll() {
		return this.genreRepository.findAll();
	}

	public Genre findOne(final int idGenre) {
		return this.genreRepository.findOne(idGenre);
	}

}
