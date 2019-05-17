
package utiles;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {

	private static String	genericEmail	= "^([0-9a-zA-Z ]{1,}[ ]{1}[<]{1}[0-9a-zA-Z ]{1,}[@]{1}[0-9a-zA-Z.]{1,}[>]{1}|[0-9a-zA-Z ]{1,}[@]{1}[0-9a-zA-Z.]{1,})$";
	private static String	adminEmail		= "^([0-9a-zA-Z ]{1,}[ ]{1}[<]{1}[A-Za-z0-9]{1,}[@]{1}[>]{1}|[A-Za-z0-9]{1,}[@]{1})$";


	public static Boolean validateEmail(final String email, final String authority) {

		Boolean valid = false;
		Pattern emailPattern = null;

		switch (authority) {
		case "ADMINISTRATOR":
			emailPattern = Pattern.compile(EmailValidator.adminEmail);
			break;
		default:
			emailPattern = Pattern.compile(EmailValidator.genericEmail);
			break;

		}

		final Matcher mEmail = emailPattern.matcher(email.toLowerCase());
		if (mEmail.matches())
			valid = true;
		return valid;
	}

}
