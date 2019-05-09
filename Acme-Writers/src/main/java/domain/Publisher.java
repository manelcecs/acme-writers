
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class Publisher extends Actor {

	private String		VAT;
	private String		commercialName;
	private CreditCard	creditCard;


	@NotBlank
	@SafeHtml
	public String getVAT() {
		return this.VAT;
	}

	public void setVAT(final String vAT) {
		this.VAT = vAT;
	}

	@NotBlank
	@SafeHtml
	public String getCommercialName() {
		return this.commercialName;
	}

	public void setCommercialName(final String commercialName) {
		this.commercialName = commercialName;
	}

	@Valid
	@NotNull
	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}

}
