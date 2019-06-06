
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sponsorship;

@Repository
public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer> {

	@Query("select sp from Sponsorship sp where sp.sponsor.id = ?1")
	Collection<Sponsorship> findAllBySponsor(int idSponsor);

	@Query("select sp from Sponsorship sp join sp.contests p where p.id = ?1 and sp.cancelled = false")
	List<Sponsorship> findAllByContest(int idContest);

	@Query("select distinct(sp) from Sponsorship sp join sp.contests c where c.id = ?1")
	Collection<Sponsorship> getSponsorshipsOfContest(int idContest);

}
