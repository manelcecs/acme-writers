
package forms;

import java.util.Collection;

import javax.persistence.ManyToMany;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import domain.Contest;

public class SponsorshipForm {

	private int					id;
	private String				bannerURL;
	private String				targetPageURL;

	private Collection<Contest>	contests;


	@URL
	@NotBlank
	public String getBannerURL() {
		return this.bannerURL;
	}

	public void setBannerURL(final String bannerURL) {
		this.bannerURL = bannerURL;
	}

	@URL
	@NotBlank
	public String getTargetPageURL() {
		return this.targetPageURL;
	}

	public void setTargetPageURL(final String targetPageURL) {
		this.targetPageURL = targetPageURL;
	}

	@Valid
	@ManyToMany
	@NotNull
	public Collection<Contest> getContests() {
		return this.contests;
	}

	public void setContests(final Collection<Contest> contests) {
		this.contests = contests;
	}

	@NotNull
	@Min(0)
	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

}
