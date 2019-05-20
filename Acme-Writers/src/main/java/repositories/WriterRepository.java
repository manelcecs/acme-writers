
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Writer;

@Repository
public interface WriterRepository extends JpaRepository<Writer, Integer> {

	@Query("select a from Writer a where a.userAccount.id = ?1")
	Writer findByPrincipal(int principalId);
}
