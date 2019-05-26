
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Contest;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Integer> {

	@Query("select c from Contest c where c.publisher.id = ?1")
	Collection<Contest> getContestsOfPublisher(int idPublisher);

	//---------------------------------------------------------------------------------

	@Query("select c from Contest c where (1*(Select count(p) from Participation p where p.contest.id = c.id) = (select max(1*(select count(p) from Participation p where p.contest.id = c.id)) from Contest c))")
	Collection<Contest> getContestsWithMoreParticipations();

	//--------------------------------------------------------------------------------

	@Query("select c from Contest c where (1*(Select count(sp) from Sponsorship sp join sp.contests c where c.id = c.id) = (select max(1*(select count(spo) from Sponsorship spo join spo.contests co where co.id = co.id)) from Contest co))")
	Collection<Contest> getContestsWithMoreSponsorships();

	@Query("select c from Contest c where (select count(*) from Participation p where p.contest.id = c and p.book.writer.id = ?1) != (select count(*) from Book b where b.draft = false and (b.status = 'INDEPENDENT' or b.status = 'ACCEPTED') and b.cancelled = false and b.writer.id = ?1)")
	Collection<Contest> getContestCanParticipate(int id);

}
