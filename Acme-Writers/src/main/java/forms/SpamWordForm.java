
package forms;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

public class SpamWordForm {

	private String	spamWord;


	@SafeHtml
	@NotBlank
	public String getSpamWord() {
		return this.spamWord;
	}

	public void setSpamWord(final String spamWord) {
		this.spamWord = spamWord;
	}

}
