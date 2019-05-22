
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer> {

	@Query("select g from Genre g where g.nameEN != 'GENRE' or g.nameES != 'GÉNERO'")
	Collection<Genre> findAllMinusGENRE();

	@Query("select g.nameES from Genre g where g.nameES != 'GÉNERO'")
	Collection<String> getAllNameES();

	@Query("select g.nameEN from Genre g where g.nameEN != 'GENRE'")
	Collection<String> getAllNameEN();

	@Query("select g from Genre g where g.nameEN = 'GENRE' and g.nameES = 'GÉNERO'")
	Genre getGeneralGenre();

	@Query("select g from Genre g where g.parent.id = ?1")
	Collection<Genre> getSubgenres(int idGenre);

}
