
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Book;
import domain.Opinion;

@Repository
public interface OpinionRepository extends JpaRepository<Opinion, Integer> {

	@Query("select o from Opinion o where o.reader.id = ?1")
	Collection<Opinion> findOpinionsByReader(int idReader);

	@Query("select count(*) from Opinion o where o.book.id = ?1 and o.positiveOpinion = true")
	Integer getNumLikesOfBook(int idBook);

	@Query("select count(*) from Opinion o where o.book.id = ?1")
	Integer getNumOpinionsOfBook(int idBook);

	@Query("select o from Opinion o where o.book.id = ?1")
	Collection<Book> getOpinionsOfBook(int idBook);

}
