
package forms;

import org.hibernate.validator.constraints.SafeHtml;

public class SpamWordForm {

	private String	spamWord;


	@SafeHtml
	public String getSpamWord() {
		return this.spamWord;
	}

	public void setSpamWord(final String spamWord) {
		this.spamWord = spamWord;
	}

}
