
package forms;

import org.hibernate.validator.constraints.SafeHtml;

import domain.DomainEntity;

public class SearchForm extends DomainEntity {

	//Atributes
	private String	keyword;


	@SafeHtml
	public String getKeyword() {
		return this.keyword;
	}

	public void setKeyword(final String keyword) {
		this.keyword = keyword;
	}
}
