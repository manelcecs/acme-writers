
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Writer;

@Repository
public interface WriterRepository extends JpaRepository<Writer, Integer> {

	@Query("select a from Writer a where a.userAccount.id = ?1")
	Writer findByPrincipal(int principalId);

	//---------------------------------------------------------------------------------

	@Query("select w from Writer w where (1*(Select count(b) from Book b where b.writer.id = w.id and b.draft = false) = (select max(1*(select count(b) from Book b where b.draft = false and b.writer.id = w.id)) from Writer w))")
	Collection<Writer> getWritersWithMoreBooks();

	//---------------------------------------------------------------------------------

	@Query("select w from Writer w where (1*(Select count(b) from Book b where b.writer.id = w.id and b.draft = false) = (select min(1*(select count(b) from Book b where b.draft = false and b.writer.id = w.id)) from Writer w))")
	Collection<Writer> getWritersWithLessBooks();

}
