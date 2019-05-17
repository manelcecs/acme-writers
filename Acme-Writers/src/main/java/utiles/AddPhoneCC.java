
package utiles;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddPhoneCC {

	public static String addPhoneCC(final String CC, String phone) {

		final Pattern phonePattern = Pattern.compile("^([0-9]{4,})$");

		final Matcher matcherPhone = phonePattern.matcher(phone.toLowerCase());
		if (matcherPhone.matches())
			phone = CC + " " + phone;
		return phone;
	}
}
