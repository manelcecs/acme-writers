
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

	@Query("select m from Message m where m.sender.id = ?1")
	Collection<Message> findAllByActor(int actorId);

	@Query("select m.recipients from Message m where m.id = ?1")
	Collection<Actor> getRecipients(int idMessage);

	@Query("select m.id from Message m join m.tags t where (m.subject LIKE %?1% or m.body LIKE %?1% or t LIKE %?1%) and m.id = ?2")
	Integer existsSpamWordInMessage(String spamWord, int idMessage);

	@Query("select m.id from Message m where (m.subject LIKE %?1% or m.body LIKE %?1%) and m.id = ?2")
	Integer existsSpamWordInMessageWithoutTag(String spamWord, int idMessage);

}
