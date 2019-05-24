
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Participation;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Integer> {

	//TODO por david
	@Query("select a from Actor a")
	Collection<Participation> getParticipationsOfContest(int idContest);

	//TODO por david
	@Query("select a from Actor a")
	Collection<Participation> getParticipationsOfWriter(int idWriter);

}
