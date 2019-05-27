
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Chapter;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Integer> {

	@Query("select c from Chapter c where c.book.id = ?1 order by c.number asc")
	Collection<Chapter> getChaptersOfABook(int idBook);

	@Query("select c.number from Chapter c where c.book.id = ?1 order by c.number asc")
	Collection<Integer> getNumbersOfChaptersOfABook(int idBook);
}
