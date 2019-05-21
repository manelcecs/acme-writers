
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Writer;

@Repository
public interface WriterRepository extends JpaRepository<Writer, Integer> {

	@Query("select w from Writer w where w.userAcount.id = ?1")
	Writer findByPrincipal(Integer idPrincipal);

}
