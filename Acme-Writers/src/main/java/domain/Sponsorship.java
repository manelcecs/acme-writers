
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import forms.SponsorshipForm;

@Entity
@Access(AccessType.PROPERTY)
public class Sponsorship extends DomainEntity {

	private Boolean				cancelled;
	private String				bannerURL;
	private String				targetPageURL;
	private Integer				views;
	private Double				flatRateApplied;

	private Sponsor				sponsor;
	private Collection<Contest>	contests;


	@NotNull
	public Boolean getCancelled() {
		return this.cancelled;
	}

	public void setCancelled(final Boolean cancelled) {
		this.cancelled = cancelled;
	}

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

	@Min(0)
	@NotNull
	public Double getFlatRateApplied() {
		return this.flatRateApplied;
	}

	public void setFlatRateApplied(final Double flatRateApplied) {
		this.flatRateApplied = flatRateApplied;
	}

	@Valid
	@ManyToOne(optional = false)
	public Sponsor getSponsor() {
		return this.sponsor;
	}

	public void setSponsor(final Sponsor sponsor) {
		this.sponsor = sponsor;
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
	public Integer getViews() {
		return this.views;
	}

	public void setViews(final Integer views) {
		this.views = views;
	}

	public SponsorshipForm castToForm() {
		final SponsorshipForm sponsorshipForm = new SponsorshipForm();

		sponsorshipForm.setId(this.getId());
		sponsorshipForm.setBannerURL(this.getBannerURL());
		sponsorshipForm.setContests(this.getContests());
		sponsorshipForm.setTargetPageURL(this.getTargetPageURL());

		return sponsorshipForm;

	}
}
