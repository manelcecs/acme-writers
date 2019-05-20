
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.MessageBox;

@Repository
public interface MessageBoxRepository extends JpaRepository<MessageBox, Integer> {

	@Query("select mb from Actor a join a.messageBoxes mb where a.id = ?1 and mb.name = ?2")
	MessageBox findOriginalBox(int id, String nameBox);

	@Query("select distinct(1) from Actor a join a.messageBoxes mb where a.id = ?1 and mb.name = ?2")
	Integer findAllNameMessageBoxOfActor(int idActor, String nameBox);

	@Query("select a.messageBoxes from Actor a where a.id = ?1")
	Collection<MessageBox> findAllMessageBoxByActor(int idActor);

	@Query("select mb from Actor a join a.messageBoxes mb join mb.messages m where a.id = ?1 and m.id = ?2")
	Collection<MessageBox> findAllMessageBoxByActorContainsAMessage(int idActor, int idMessage);

	@Query("select mb From Actor a join a.messageBoxes mb where a.id = ?1 and mb.deleteable = true")
	Collection<MessageBox> findPosibleParents(int id);

	@Query("select m From MessageBox m where m.parent.id = ?1")
	Collection<MessageBox> findChildren(int id);

}
