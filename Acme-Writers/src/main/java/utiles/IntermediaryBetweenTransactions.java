
package utiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import services.TickerService;
import domain.Ticker;

@Service
public class IntermediaryBetweenTransactions {

	@Autowired
	TickerService	tickerService;


	public Ticker generateTicker() {
		boolean exito = false;
		Ticker result = null;

		int attempts = 20;

		while (!exito || attempts == 0)
			try {
				result = this.tickerService.generateTicker();
				exito = true;
			} catch (final Throwable oops) {
				attempts--;
			}
		return result;
	}

}
