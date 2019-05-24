
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {

	@Query("select a from Actor a where a.userAccount.id = ?1")
	Actor getByUserAccount(int userAccountId);

	@Query("select a from Actor a join a.messageBoxes mb where mb.id = ?1")
	Actor getByMessageBox(int idBox);

	@Query("select a from Actor a where a.userAccount.authorities.size > 0")
	Collection<Actor> findNonEliminatedActors();

	// Workaround for the problem of hibernate with inheritances
	@Query("select a from Actor a where a.id = ?1")
	Actor getActor(int idActor);

	@Query("select a from Actor a where a.spammer = true and a.userAccount.authorities.size > 0")
	Collection<Actor> findSpamActors();
}
