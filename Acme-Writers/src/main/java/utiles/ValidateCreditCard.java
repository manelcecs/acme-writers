
package utiles;

import java.util.GregorianCalendar;

import org.joda.time.LocalDate;
import org.springframework.validation.BindingResult;

import domain.CreditCard;

public class ValidateCreditCard {

	@Deprecated
	public static void checkFecha(final CreditCard card, final BindingResult binding) {
		final int annoActual = new LocalDate().getYear();
		final int mesActual = new LocalDate().getMonthOfYear();

		if (annoActual > card.getExpirationYear())
			binding.rejectValue("creditCard.expirationYear", "creditCard.exprirationYear.error");
		if (annoActual <= card.getExpirationYear())
			if (mesActual >= card.getExpirationYear())
				binding.rejectValue("creditCard.expirationMonth", "creditCard.expirationMonth.error");
	}

	public static void checkGregorianDate(final CreditCard card, final BindingResult binding) {
		int cardYear = 0;
		if (card.getExpirationYear() < 100)
			cardYear = card.getExpirationYear() + 2000;
		else
			cardYear = card.getExpirationYear();

		final GregorianCalendar cardDate = new GregorianCalendar(cardYear, card.getExpirationMonth(), 1);
		final LocalDate date = LocalDate.now();
		final GregorianCalendar actualDate = new GregorianCalendar(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());

		final int compare = actualDate.compareTo(cardDate);
		if (compare >= 0)
			binding.rejectValue("creditCard.expirationYear", "creditCard.expriration.error");

	}

	public static Boolean isCaducate(final CreditCard card) {
		Boolean res = false;
		int cardYear = 0;
		if (card.getExpirationYear() < 100)
			cardYear = card.getExpirationYear() + 2000;
		else
			cardYear = card.getExpirationYear();

		final GregorianCalendar cardDate = new GregorianCalendar(cardYear, card.getExpirationMonth(), 1);
		final LocalDate date = LocalDate.now();
		final GregorianCalendar actualDate = new GregorianCalendar(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());

		final int compare = actualDate.compareTo(cardDate);
		if (compare >= 0)
			res = true;

		return res;
	}

	public static CreditCard checkNumeroAnno(final CreditCard card) {
		int res = card.getExpirationYear();
		if (res < 100)
			res = res + 2000;
		final CreditCard result = card;
		result.setExpirationYear(res);
		return result;
	}

}
