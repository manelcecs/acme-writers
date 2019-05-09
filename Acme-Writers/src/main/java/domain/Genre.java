
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class Genre extends DomainEntity {

	private String	nameEN;
	private String	nameES;

	private Genre	parent;


	@NotBlank
	@SafeHtml
	public String getNameEN() {
		return this.nameEN;
	}

	public void setNameEN(final String nameEN) {
		this.nameEN = nameEN;
	}

	@NotBlank
	@SafeHtml
	public String getNameES() {
		return this.nameES;
	}

	public void setNameES(final String nameES) {
		this.nameES = nameES;
	}

	@Valid
	@ManyToOne(optional = true)
	public Genre getParent() {
		return this.parent;
	}

	public void setParent(final Genre parent) {
		this.parent = parent;
	}

}
