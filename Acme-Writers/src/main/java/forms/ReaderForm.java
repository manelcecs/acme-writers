
package forms;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

public class ReaderForm extends ActorForm {

	private String	email;


	@NotBlank
	@Pattern(regexp = "^([0-9a-zA-Z ]{1,}[ ]{1}[<]{1}[0-9a-zA-Z ]{1,}[@]{1}[0-9a-zA-Z.]{1,}[>]{1}|[0-9a-zA-Z ]{1,}[@]{1}[0-9a-zA-Z.]{1,})$")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

}
