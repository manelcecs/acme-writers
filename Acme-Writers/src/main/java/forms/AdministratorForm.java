
package forms;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

public class AdministratorForm extends ActorForm {

	private String	email;


	@NotBlank
	@Pattern(regexp = "^([0-9a-zA-Z ]{1,}[ ]{1}[<]{1}[A-Za-z0-9]{1,}[@]{1}[>]{1}|[A-Za-z0-9]{1,}[@]{1})$")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

}
