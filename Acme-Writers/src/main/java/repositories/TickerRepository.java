
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Ticker;

@Repository
public interface TickerRepository extends JpaRepository<Ticker, Integer> {

}
