
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

	@Query("select distinct(b) from Book b LEFT JOIN b.writer w LEFT JOIN b.publisher p LEFT JOIN b.genre g where (b.draft = false AND b.cancelled = false AND (b.status = 'INDEPENDENT' OR b.status = 'ACCEPTED')) AND (w.name LIKE %?1% OR w.surname LIKE %?1% OR b.title LIKE %?1% OR b.description LIKE %?1% OR p.commercialName LIKE %?1%) AND (b.language LIKE %?4%) AND (b.numWords between ?2 and ?3) AND (g.id between ?5 and ?6)")
	Collection<Book> getFilterBooksByFinder(String keyWord, Integer minNumWords, Integer maxNumWords, String language, int minGenre, int maxGenre);

	@Query("select f.books from Finder f where f.id = ?1")
	Collection<Book> getBooksByFinder(int idFinder);

	@Query("select b from Book b where b.genre.id = ?1")
	Collection<Book> getBooksByGenre(int idGenre);
}
