
package services;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.TickerRepository;
import domain.Ticker;

@Service
@Transactional(value = TxType.REQUIRES_NEW)
public class TickerService {

	@Autowired
	private TickerRepository	tickerRepository;


	public Ticker generateTicker() throws NoSuchAlgorithmException {
		final Ticker ticker = new Ticker();
		Ticker result;

		//Fecha de crecion
		String identifier;
		final Date dateNow = DateTime.now().toDate();
		final DateFormat dateFormat = new SimpleDateFormat("ddMMyy");
		identifier = dateFormat.format(dateNow) + "-";

		//Cadena alfanumerica
		final SecureRandom randomGenerator = SecureRandom.getInstance("SHA1PRNG");
		final String alfanumerica = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		for (int i = 0; i < 10; i++) {
			final Integer randomNumber = randomGenerator.nextInt(alfanumerica.length());
			identifier = identifier + alfanumerica.charAt(randomNumber);
		}

		ticker.setIdentifier(identifier);
		result = this.tickerRepository.saveAndFlush(ticker);

		return result;
	}

	public void delete(final Ticker ticker) {
		this.tickerRepository.delete(ticker);

	}

}
