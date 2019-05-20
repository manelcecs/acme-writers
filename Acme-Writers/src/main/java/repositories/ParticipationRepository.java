
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Participation;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Integer> {

}
