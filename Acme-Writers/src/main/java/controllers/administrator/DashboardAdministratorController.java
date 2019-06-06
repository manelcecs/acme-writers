
package controllers.administrator;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;
import services.ContestService;
import services.SponsorService;
import services.WriterService;
import controllers.AbstractController;
import domain.Contest;
import domain.Sponsor;
import domain.Writer;

@Controller
@RequestMapping("/dashboard")
public class DashboardAdministratorController extends AbstractController {

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private WriterService			writerService;

	@Autowired
	private ContestService			contestService;

	@Autowired
	private SponsorService			sponsorService;


	@RequestMapping(value = "/administrator/display", method = RequestMethod.GET)
	public ModelAndView display() {
		final ModelAndView result = new ModelAndView("administrator/dashboard");

		//----------------------------------------------------------------

		final Double avgOfBooksPerWriter = this.administratorService.getAvgOfBooksPerWriter();
		if (avgOfBooksPerWriter != null)
			result.addObject("avgOfBooksPerWriter", avgOfBooksPerWriter);
		else
			result.addObject("avgOfBooksPerWriter", 0.0);

		final Integer minimumOfBooksPerWriter = this.administratorService.getMinimumOfBooksPerWriter();
		if (minimumOfBooksPerWriter != null)
			result.addObject("minimumOfBooksPerWriter", minimumOfBooksPerWriter);
		else
			result.addObject("minimumOfBooksPerWriter", 0);

		final Integer maximumOfBooksPerWriter = this.administratorService.getMaximumOfBooksPerWriter();
		if (maximumOfBooksPerWriter != null)
			result.addObject("maximumOfBooksPerWriter", maximumOfBooksPerWriter);
		else
			result.addObject("maximumOfBooksPerWriter", 0);

		final Double sDOfBooksPerWriter = this.administratorService.getSDOfBooksPerWriter();
		if (sDOfBooksPerWriter != null)
			result.addObject("sDOfBooksPerWriter", sDOfBooksPerWriter);
		else
			result.addObject("sDOfBooksPerWriter", 0.0);

		//----------------------------------------------------------------

		final Double avgOfContestPerPublisher = this.administratorService.getAvgOfContestPerPublisher();
		if (avgOfContestPerPublisher != null)
			result.addObject("avgOfContestPerPublisher", avgOfContestPerPublisher);
		else
			result.addObject("avgOfContestPerPublisher", 0.0);

		final Integer minimumOfContestPerPublisher = this.administratorService.getMinimumOfContestPerPublisher();
		if (minimumOfContestPerPublisher != null)
			result.addObject("minimumOfContestPerPublisher", minimumOfContestPerPublisher);
		else
			result.addObject("minimumOfContestPerPublisher", 0);

		final Integer maximumOfContestPerPublisher = this.administratorService.getMaximumOfContestPerPublisher();
		if (maximumOfContestPerPublisher != null)
			result.addObject("maximumOfContestPerPublisher", maximumOfContestPerPublisher);
		else
			result.addObject("maximumOfContestPerPublisher", 0);

		final Double sDOfContestPerPublisher = this.administratorService.getSDOfContestPerPublisher();
		if (sDOfContestPerPublisher != null)
			result.addObject("sDOfContestPerPublisher", sDOfContestPerPublisher);
		else
			result.addObject("sDOfContestPerPublisher", 0.0);

		//----------------------------------------------------------------

		final Double ratioOfBooksWithPublisherVsBooksIndependients = this.administratorService.getRatioOfBooksWithPublisherVsBooksIndependients();
		if (ratioOfBooksWithPublisherVsBooksIndependients != null)
			result.addObject("ratioOfBooksWithPublisherVsBooksIndependients", ratioOfBooksWithPublisherVsBooksIndependients);
		else
			result.addObject("ratioOfBooksWithPublisherVsBooksIndependients", 0.0);

		//-----------------------------------------------------------------x
		final Double ratioOfBooksAcceptedVsBooksRejected = this.administratorService.getRatioOfBooksAcceptedVsBooksRejected();
		if (ratioOfBooksAcceptedVsBooksRejected != null)
			result.addObject("ratioOfBooksAcceptedVsBooksRejected", ratioOfBooksAcceptedVsBooksRejected);
		else
			result.addObject("ratioOfBooksAcceptedVsBooksRejected", 0.0);

		//-----------------------------------------------------------------

		final Double avgOfChaptersPerBook = this.administratorService.getAvgOfChaptersPerBook();
		if (avgOfChaptersPerBook != null)
			result.addObject("avgOfChaptersPerBook", avgOfChaptersPerBook);
		else
			result.addObject("avgOfChaptersPerBook", 0.0);

		final Integer minimumOfChaptersPerBook = this.administratorService.getMinimumOfChaptersPerBook();
		if (minimumOfChaptersPerBook != null)
			result.addObject("minimumOfChaptersPerBook", minimumOfChaptersPerBook);
		else
			result.addObject("minimumOfChaptersPerBook", 0);

		final Integer maximumOfChaptersPerBook = this.administratorService.getMaximumOfChaptersPerBook();
		if (maximumOfChaptersPerBook != null)
			result.addObject("maximumOfChaptersPerBook", maximumOfChaptersPerBook);
		else
			result.addObject("maximumOfChaptersPerBook", 0);

		final Double sDOfChaptersPerBook = this.administratorService.getSDOfChaptersPerBook();
		if (sDOfChaptersPerBook != null)
			result.addObject("sDOfChaptersPerBook", sDOfChaptersPerBook);
		else
			result.addObject("sDOfChaptersPerBook", 0.0);

		//------------------------------------------------------------------------- 
		final List<Object[]> histogramData = this.administratorService.getHistogramData();
		result.addObject("histogramData", histogramData);

		//-----------------------------------------------------------------

		final Double avgOfSponsorshipsPerSponsor = this.administratorService.getAvgOfSponsorshipsPerSponsor();
		if (avgOfSponsorshipsPerSponsor != null)
			result.addObject("avgOfSponsorshipsPerSponsor", avgOfSponsorshipsPerSponsor);
		else
			result.addObject("avgOfSponsorshipsPerSponsor", 0.0);

		final Integer minimumOfSponsorshipsPerSponsor = this.administratorService.getMinimumOfSponsorshipsPerSponsor();
		if (minimumOfSponsorshipsPerSponsor != null)
			result.addObject("minimumOfSponsorshipsPerSponsor", minimumOfSponsorshipsPerSponsor);
		else
			result.addObject("minimumOfSponsorshipsPerSponsor", 0);

		final Integer maximumOfSponsorshipsPerSponsor = this.administratorService.getMaximumOfSponsorshipsPerSponsor();
		if (maximumOfSponsorshipsPerSponsor != null)
			result.addObject("maximumOfSponsorshipsPerSponsor", maximumOfSponsorshipsPerSponsor);
		else
			result.addObject("maximumOfSponsorshipsPerSponsor", 0);

		final Double sDOfSponsorshipsPerSponsor = this.administratorService.getSDOfSponsorshipsPerSponsor();
		if (sDOfSponsorshipsPerSponsor != null)
			result.addObject("sDOfSponsorshipsPerSponsor", sDOfSponsorshipsPerSponsor);
		else
			result.addObject("sDOfSponsorshipsPerSponsor", 0.0);

		//-----------------------------------------------------------------

		final Double ratioOfSponsorshipsCancelledVsSponsorshipsNotCancelled = this.administratorService.getRatioOfSponsorshipsCancelledVsSponsorshipsNotCancelled();
		if (ratioOfSponsorshipsCancelledVsSponsorshipsNotCancelled != null)
			result.addObject("ratioOfSponsorshipsCancelledVsSponsorshipsNotCancelled", ratioOfSponsorshipsCancelledVsSponsorshipsNotCancelled);
		else
			result.addObject("ratioOfSponsorshipsCancelledVsSponsorshipsNotCancelled", 0.0);

		//-----------------------------------------------------------------

		final Double avgOfViewsPerSponsorship = this.administratorService.getAvgOfViewsPerSponsorship();
		if (avgOfViewsPerSponsorship != null)
			result.addObject("avgOfViewsPerSponsorship", avgOfViewsPerSponsorship);
		else
			result.addObject("avgOfViewsPerSponsorship", 0.0);

		final Integer minimumOfViewsPerSponsorship = this.administratorService.getMinimumOfViewsPerSponsorship();
		if (minimumOfViewsPerSponsorship != null)
			result.addObject("minimumOfViewsPerSponsorship", minimumOfViewsPerSponsorship);
		else
			result.addObject("minimumOfViewsPerSponsorship", 0);

		final Integer maximumOfViewsPerSponsorship = this.administratorService.getMaximumOfViewsPerSponsorship();
		if (maximumOfViewsPerSponsorship != null)
			result.addObject("maximumOfViewsPerSponsorship", maximumOfViewsPerSponsorship);
		else
			result.addObject("maximumOfViewsPerSponsorship", 0);

		final Double sDOfViewsPerSponsorship = this.administratorService.getSDOfViewsPerSponsorship();
		if (sDOfViewsPerSponsorship != null)
			result.addObject("sDOfViewsPerSponsorship", sDOfViewsPerSponsorship);
		else
			result.addObject("sDOfViewsPerSponsorship", 0.0);

		//----------------------------------------------------------------------

		final Collection<Writer> writersWithMoreBooks = this.writerService.getWritersWithMoreBooks();
		result.addObject("writersWithMoreBooks", writersWithMoreBooks);

		//----------------------------------------------------------------------

		final Collection<Writer> writersWithLessBooks = this.writerService.getWritersWithLessBooks();
		result.addObject("writersWithLessBooks", writersWithLessBooks);

		//----------------------------------------------------------------------

		final Collection<Contest> contestsWithMoreParticipations = this.contestService.getContestsWithMoreParticipations();
		result.addObject("contestsWithMoreParticipations", contestsWithMoreParticipations);

		final Integer maximumOfParticipationsContest = this.administratorService.getMaximumOfParticipationsContest();
		if (maximumOfParticipationsContest != null)
			result.addObject("maximumOfParticipationsContest", maximumOfParticipationsContest);
		else
			result.addObject("maximumOfParticipationsContest", 0);
		//----------------------------------------------------------------------

		final Collection<Contest> contestsWithMoreSponsorships = this.contestService.getContestsWithMoreSponsorships();
		result.addObject("contestsWithMoreSponsorships", contestsWithMoreSponsorships);

		final Integer maximumOfSponsorshipsContest = this.administratorService.getMaximumOfSponsorshipsContest();
		if (maximumOfSponsorshipsContest != null)
			result.addObject("maximumOfSponsorshipsContest", maximumOfSponsorshipsContest);
		else
			result.addObject("maximumOfSponsorshipsContest", 0);
		//----------------------------------------------------------------------

		final Collection<Sponsor> sponsorsWithMoreSponsorships = this.sponsorService.getSponsorsWithMoreSponsorships();
		result.addObject("sponsorsWithMoreSponsorships", sponsorsWithMoreSponsorships);

		result.addObject("requestURI", "dashboard/administrator/display.do");

		this.configValues(result);
		return result;
	}
}
