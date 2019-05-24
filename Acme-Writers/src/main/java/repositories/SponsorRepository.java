
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Sponsor;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, Integer> {

	@Query("select a from Sponsor a where a.userAccount.id = ?1")
	Sponsor findByPrincipal(int idPrincipal);

	@Query("select s from Sponsor s where (s.creditCard.expirationYear < year(current_date())) or ((s.creditCard.expirationYear = year(current_date())) and s.creditCard.expirationMonth <= month(current_date())))")
	Collection<Actor> findSponsorsWithExpiredCreditCard();

	@Query("select s from Sponsor s where (1*(Select count(sp) from Sponsorship sp where sp.sponsor.id = s.id) = (select max(1*(select count(spo) from Sponsorship spo where spo.sponsor.id = spon.id)) from Sponsor spon))")
	Collection<Sponsor> getSponsorsWithMoreSponsorships();
}
