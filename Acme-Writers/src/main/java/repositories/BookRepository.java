
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

	@Query("select b from Book b where b.writer.id = ?1")
	Collection<Book> getAllBooksOfAWriter(int idWriter);

	@Query("select b from Book b where b.draft = false and (b.status = 'INDEPENDENT' or b.status = 'ACCEPTED') and b.cancelled = false")
	Collection<Book> getAllVisibleBooks();

	@Query("select b from Book b where b.writer.id = ?1 and b.draft = false and (b.status = 'INDEPENDENT' or b.status = 'ACCEPTED') and b.cancelled = false")
	Collection<Book> getAllVisibleBooksOfWriter(int idWriter);

	@Query("select distinct(c.book) from Chapter c where c.book.draft = true and c.book.writer.id = ?1")
	Collection<Book> getBooksCanChangeDraft(int idWriter);

	@Query("select b from Book b where b.publisher.id = ?1 and b.draft = false and b.cancelled = false")
	Collection<Book> getBooksOfPublisher(int idPublisher);

}
