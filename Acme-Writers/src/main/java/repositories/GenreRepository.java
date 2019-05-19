
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Genre;


@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer> {

}
