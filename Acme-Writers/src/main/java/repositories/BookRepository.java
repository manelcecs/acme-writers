
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

	// Bug EAGER
	@Query("select b from Book b where b.id = ?1")
	Book findOne(int idBook);

	@Query("select distinct(b) from Book b LEFT JOIN b.writer w LEFT JOIN b.genre g where (b.draft = false AND b.cancelled = false AND (b.status = 'INDEPENDENT' OR b.status = 'ACCEPTED')) AND (w.name LIKE %?1% OR w.surname LIKE %?1% OR b.title LIKE %?1% OR b.description LIKE %?1% OR b.language LIKE %?1% OR g.nameEN LIKE %?1% OR g.nameES LIKE %?1%)")
	Collection<Book> getFilterBooksByKeyword(String keyword);
}
