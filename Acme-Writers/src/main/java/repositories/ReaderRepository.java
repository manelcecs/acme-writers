
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Reader;

@Repository
public interface ReaderRepository extends JpaRepository<Reader, Integer> {

	@Query("select r from Reader r where r.userAccount.id = ?1")
	Reader findByPrincipal(int principalId);

}
