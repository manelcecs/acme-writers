
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Publisher;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Integer> {

	@Query("select a from Publisher a where a.userAccount.id = ?1")
	Publisher findByPrincipal(int principalId);

}
