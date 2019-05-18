
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Pattern;

@Entity
@Access(AccessType.PROPERTY)
public class Ticker extends DomainEntity {

	private String	identifier;


	@Pattern(regexp = "^[0-9]{6}-[A-Za-z]{10}$")
	@Column(unique = true)
	public String getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}
}
