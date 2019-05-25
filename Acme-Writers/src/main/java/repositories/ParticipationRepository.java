
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Participation;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Integer> {

	@Query("select p from Participation p join p.book b where b.writer.id = ?1")
	Collection<Participation> getParticipationsOfWriter(int idWriter);

	@Query("select p from Participation p join p.contest c where c.id = ?1")
	Collection<Participation> getParticipationsOfContest(int idContest);

	@Query("select p from Participation p join p.contest c join c.publisher pu where pu.id = ?1")
	Collection<Participation> getParticipationsOfPublisher(int idPublisher);

}
