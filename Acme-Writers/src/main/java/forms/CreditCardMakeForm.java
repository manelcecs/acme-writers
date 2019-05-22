
package forms;

import org.hibernate.validator.constraints.SafeHtml;

public class CreditCardMakeForm {

	private String	creditCardMake;


	@SafeHtml
	public String getCreditCardMake() {
		return this.creditCardMake;
	}

	public void setCreditCardMake(final String creditCardMake) {
		this.creditCardMake = creditCardMake;
	}
}
