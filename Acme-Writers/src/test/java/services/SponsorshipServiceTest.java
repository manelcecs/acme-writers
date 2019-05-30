
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Contest;
import domain.Sponsorship;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SponsorshipServiceTest extends AbstractTest {

	@Autowired
	SponsorshipService	sponsorshipService;

	@Autowired
	SponsorService		sponsorService;

	@Autowired
	ContestService		contestService;


	/*
	 * This test reefer to requeriment 24
	 * An actor who is authenticated as a sponsor must be able to:
	 * Manage his or her sponsorships, which includes LISTING, showing,
	 * creating, updating and deleting them.
	 * 1 positive
	 * 1 negative
	 */
	@Test
	public void listingDriver() {
		final Object testingData[][] = {
			{
				/*
				 * a)A sponsor lists his or her sponsorships
				 * b)Positive
				 * c)Sequence coverage: 100%
				 * d)Data coverage: -
				 */
				"sponsor0", "sponsor0", null
			}, {
				/*
				 * a) One sponsor lists the sponsorships of another sponsor
				 * b) Negative
				 * c)Sequence coverage: 50%
				 * d)Data coverage: -
				 */
				"sponsor0", "sponsor1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listingTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Ancillary methods ------------------------------------------------------
	protected void listingTemplate(final String user, final String owner, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(user);

			final int ownerID = super.getEntityId(owner);
			this.sponsorshipService.findAllBySponsor(ownerID);

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	/*
	 * This test reefer to requeriment 24
	 * An actor who is authenticated as a sponsor must be able to:
	 * Manage his or her sponsorships, which includes listing, SHOWING,
	 * creating, updating and deleting them.
	 * 1 positive
	 * 1 negative
	 */
	@Test
	public void showingDriver() {
		final Object testingData[][] = {
			{
				/*
				 * a)A sponsor display his or her sponsorship
				 * b)Positive
				 * c)Sequence coverage: 100%
				 * d)Data coverage: -
				 */
				"sponsor0", super.getEntityId("sponsorship0"), null
			}, {
				/*
				 * a) One sponsor display the sponsorship of another sponsor
				 * b) Negative
				 * c)Sequence coverage: 66%
				 * d)Data coverage: -
				 */
				"sponsor0", super.getEntityId("sponsorship1"), IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.showingTemplate((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	// Ancillary methods ------------------------------------------------------
	protected void showingTemplate(final String user, final int idSponsorship, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(user);

			this.sponsorshipService.findOne(idSponsorship);

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	/*
	 * This test reefer to requeriment 24
	 * An actor who is authenticated as a sponsor must be able to:
	 * Manage his or her sponsorships, which includes listing, showing,
	 * CREATING, updating and deleting them.
	 * 2 positive
	 * 2 negative
	 */
	@Test
	public void creatingDriver() {
		final Object testingData[][] = {
			{
				/*
				 * a)Sponsor create a valid sponsorship (with lower limit data)
				 * b)Positive
				 * c)Sequence coverage: 100%
				 * d)Data coverage: 100%
				 */
				"sponsor0", "http://url.com", "http://url.com", "contest0", null
			}, {
				/*
				 * // * a)sponsor create a valid sponsorship (with upper limit data)
				 * // * b)Positive
				 * //*c)Sequence coverage: 100%
				 * // * d)Data coverage: 100%
				 * //
				 */
				"sponsor0", "http://url.com", "http://url.com", "contest0", null
			}, {
				/*
				 * a)sponsor create a invalid sponsorship (with banner not URL)
				 * b)Negative
				 * // * c)Sequence coverage: 100%
				 * // * d)Data coverage: 100%
				 * //
				 */
				"sponsor0", "Non URL", "http://url.com", "contest0", ConstraintViolationException.class
			}, {
				/*
				 * a)sponsor create a invalid sponsorship (with banner blank)
				 * b)Negative
				 * c)Sequence coverage: 100%
				 * d)Data coverage: 100%
				 */
				"sponsor0", "", "http://url.com", "contest0", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.creatingTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}
	// Ancillary methods ------------------------------------------------------
	protected void creatingTemplate(final String user, final String bannerURL, final String targetPageURL, final String contestName, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(user);

			Sponsorship sponsorship = this.sponsorshipService.create();

			sponsorship.setSponsor(this.sponsorService.findOne(this.getEntityId(user)));

			sponsorship = this.fillData(sponsorship, bannerURL, targetPageURL, contestName);

			this.sponsorshipService.save(sponsorship);
			this.sponsorshipService.flush();

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			oops.printStackTrace();
		}

		this.checkExceptions(expected, caught);
	}
	/*
	 * This test reefer to requeriment 24
	 * An actor who is authenticated as a sponsor must be able to:
	 * Manage his or her sponsorships, which includes listing, showing,
	 * creating, UPDATING and deleting them.
	 * 2 positive
	 * 11 negative
	 */
	@Test
	public void updatingDriver() {
		final Object testingData[][] = {
			{
				/*
				 * a)Sponsor edit a valid sponsorship (with lower limit data)
				 * b)Positive
				 * c)Sequence coverage: 100%
				 * d)Data coverage: 100%
				 */
				"sponsor0", "http://url.com", "http://url.com", "contest0", null
			}, {
				/*
				 * a)Sponsor edit a valid sponsorship (with upper limit data)
				 * b)Positive
				 * c)Sequence coverage: 100%
				 * d)Data coverage: 100%
				 */
				"sponsor0", "http://url.com", "http://url.com", "contest0", null
			}, {
				/*
				 * a)Sponsor edit a invalid sponsorship (with banner not URL)
				 * b)Negative
				 * c)Sequence coverage: 100%
				 * d)Data coverage: 100%
				 */
				"sponsor0", "Non URL", "http://url.com", "contest0", ConstraintViolationException.class
			}, {
				/*
				 * a)Sponsor edit a invalid sponsorship (with banner blank)
				 * b)Negative
				 * c)Sequence coverage: 100%
				 * d)Data coverage: 100%
				 */
				"sponsor0", "", "http://url.com", "contest0", ConstraintViolationException.class
			}, {
				/*
				 * a)Sponsor edit a invalid sponsorship (with target not URL)
				 * b)Negative
				 * c)Sequence coverage: 100%
				 * d)Data coverage: 100%
				 */
				"sponsor0", "http://url.com", "Non URL", "contest0", ConstraintViolationException.class
			}, {
				/*
				 * a)Sponsor edit a invalid sponsorship (with target not blank)
				 * b)Negative
				 * c)Sequence coverage: 100%
				 * d)Data coverage: 100%
				 */
				"sponsor0", "http://url.com", "", "contest0", ConstraintViolationException.class

			}, {
				/*
				 * a)Sponsor edits the sponsorship of another sponsor
				 * b)Negative
				 * c)Sequence coverage: 12'5%
				 * d)Data coverage: 100%
				 */
				"sponsor1", "http://url.com", "http://url.com", "contest0", IllegalArgumentException.class
			}, {
				/*
				 * a)Admin edits the sponsorship of another sponsor
				 * b)Negative
				 * c)Sequence coverage: 12'5%
				 * d)Data coverage: 100%
				 */
				"admin", "http://url.com", "http://url.com", "contest0", IllegalArgumentException.class
			}, {
				/*
				 * a)Reader edits the sponsorship of another sponsor
				 * b)Negative
				 * c)Sequence coverage: 12'5%
				 * d)Data coverage: 100%
				 */
				"reader0", "http://url.com", "http://url.com", "contest0", IllegalArgumentException.class
			}, {
				/*
				 * a)Writer edits the sponsorship of another sponsor
				 * b)Negative
				 * c)Sequence coverage: 12'5%
				 * d)Data coverage: 100%
				 */
				"writer0", "http://url.com", "http://url.com", "contest0", IllegalArgumentException.class
			}, {
				/*
				 * a)Publisher edits the sponsorship of another sponsor
				 * b)Negative
				 * c)Sequence coverage: 12'5%
				 * d)Data coverage: 100%
				 */
				"publisher0", "http://url.com", "http://url.com", "contest0", IllegalArgumentException.class
			}, {
				/*
				 * a)Anonymous actor edits the sponsorship of another sponsor
				 * b)Negative
				 * c)Sequence coverage: 12'5%
				 * d)Data coverage: 100%
				 */
				null, "http://url.com", "http://url.com", "contest0", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.updatingTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}
	// Ancillary methods ------------------------------------------------------
	protected void updatingTemplate(final String user, final String bannerURL, final String targetPageURL, final String contestName, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(user);

			Sponsorship sponsorship = this.sponsorshipService.findOne(this.getEntityId("sponsorship0"));

			sponsorship = this.fillData(sponsorship, bannerURL, targetPageURL, contestName);

			this.sponsorshipService.save(sponsorship);
			this.sponsorshipService.flush();

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			oops.printStackTrace();
		}

		this.checkExceptions(expected, caught);
	}

	/*
	 * This test reefer to requeriment 24
	 * An actor who is authenticated as a sponsor must be able to:
	 * Manage his or her sponsorships, which includes listing, showing,
	 * creating, updating and DELETING them.
	 * 1 positive
	 * 1 negative
	 */
	@Test
	public void deletingDriver() {
		final Object testingData[][] = {
			{
				/*
				 * a)A sponsor delete his or her sponsorship
				 * b)Positive
				 * c)Sequence coverage: 100%
				 * d)Data coverage: -
				 */
				"sponsor0", "sponsorship0", null
			}, {
				/*
				 * a) One sponsor delete the sponsorship of another sponsor
				 * b) Negative
				 * c)Sequence coverage: 50%
				 * d)Data coverage: -
				 */
				"sponsor0", "sponsorship1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deletingTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	// Ancillary methods ------------------------------------------------------
	protected void deletingTemplate(final String user, final String idSponsorship, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(user);

			final Sponsorship sponsorship = this.sponsorshipService.findOne(this.getEntityId(idSponsorship));

			this.sponsorshipService.delete(sponsorship);

			super.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			oops.printStackTrace();
		}

		this.checkExceptions(expected, caught);
	}

	private Sponsorship fillData(final Sponsorship sponsorship, final String bannerURL, final String targetPageURL, final String contestName) {
		sponsorship.setBannerURL(bannerURL);
		sponsorship.setTargetPageURL(targetPageURL);
		sponsorship.setFlatRateApplied(0.0);
		sponsorship.setViews(0);
		sponsorship.setCancelled(false);
		final Collection<Contest> contests = new ArrayList<>();
		final Contest contest = this.contestService.findOne(this.getEntityId(contestName));
		contests.add(contest);
		sponsorship.setContests(contests);
		return sponsorship;
	}
}
