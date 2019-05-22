
package forms;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

public class AdminConfigForm {

	private String	systemName;
	private String	bannerURL;
	private String	welcomeMessageEN;
	private String	welcomeMessageES;
	private String	countryCode;
	private Integer	finderResults;
	private Integer	finderCacheTime;
	private Double	spammerPercentage;
	private Double	VAT;
	private Double	flatRate;


	@NotBlank
	@SafeHtml
	public String getSystemName() {
		return this.systemName;
	}

	public void setSystemName(final String systemName) {
		this.systemName = systemName;
	}

	@NotBlank
	@URL
	@SafeHtml
	public String getBannerURL() {
		return this.bannerURL;
	}

	public void setBannerURL(final String bannerURL) {
		this.bannerURL = bannerURL;
	}

	@NotBlank
	@SafeHtml
	public String getWelcomeMessageEN() {
		return this.welcomeMessageEN;
	}

	public void setWelcomeMessageEN(final String welcomeMessageEN) {
		this.welcomeMessageEN = welcomeMessageEN;
	}

	@NotBlank
	@SafeHtml
	public String getWelcomeMessageES() {
		return this.welcomeMessageES;
	}

	public void setWelcomeMessageES(final String welcomeMessageES) {
		this.welcomeMessageES = welcomeMessageES;
	}

	@NotBlank
	@SafeHtml
	@Pattern(regexp = "^(\\+[1-9]|\\+[1-9][0-9]|\\+[1-9][0-9][0-9])$")
	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(final String countryCode) {
		this.countryCode = countryCode;
	}

	@Range(min = 1, max = 100)
	@NotNull
	public Integer getFinderResults() {
		return this.finderResults;
	}

	public void setFinderResults(final Integer finderResults) {
		this.finderResults = finderResults;
	}

	@Range(min = 1, max = 24)
	@NotNull
	public Integer getFinderCacheTime() {
		return this.finderCacheTime;
	}

	public void setFinderCacheTime(final Integer finderCacheTime) {
		this.finderCacheTime = finderCacheTime;
	}

	@Range(min = 0, max = 100)
	@NotNull
	public Double getSpammerPercentage() {
		return this.spammerPercentage;
	}

	public void setSpammerPercentage(final Double spammerPercentage) {
		this.spammerPercentage = spammerPercentage;
	}

	@Range(min = 0, max = 100)
	@NotNull
	public Double getVAT() {
		return this.VAT;
	}

	public void setVAT(final Double VAT) {
		this.VAT = VAT;
	}

	@Min(0)
	@NotNull
	public Double getFlatRate() {
		return this.flatRate;
	}

	public void setFlatRate(final Double flatRate) {
		this.flatRate = flatRate;
	}

}
