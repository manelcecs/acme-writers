
package controllers;

import java.util.Collection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import java.util.Date;

import org.joda.time.LocalDateTime;

import services.ContestService;
import services.SponsorshipService;
import domain.Contest;
import domain.Sponsorship;

@Controller
@RequestMapping("/contest")
public class ContestController extends AbstractController {

	@Autowired
	private ContestService		contestService;

	@Autowired
	private SponsorshipService	sponsorshipService;

	private final SimpleDateFormat	FORMAT	= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


	@RequestMapping(value = "/list", method = RequestMethod.GET)
		public ModelAndView list() throws ParseException {

		final ModelAndView result = new ModelAndView("contest/list");
		final Collection<Contest> contests = this.contestService.findAll();
		final LocalDateTime DATETIMENOW = LocalDateTime.now();
		final Date actual = this.FORMAT.parse(DATETIMENOW.getYear() + "/" + 		DATETIMENOW.getMonthOfYear() + "/" + DATETIMENOW.getDayOfMonth() + " " + 		DATETIMENOW.getHourOfDay() + ":" + LocalDateTime.now().getMinuteOfHour() + ":"
			+ DATETIMENOW.getSecondOfMinute());
		result.addObject("contests", contests);
		result.addObject("viewAll", true);
		result.addObject("requestURI", "contest/list.do");
		result.addObject("actual", actual);
		this.configValues(result);
		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
		public ModelAndView display(@RequestParam final int idContest) throws ParseException {

		ModelAndView result;

		final Contest contest = this.contestService.findOne(idContest);

		result = new ModelAndView("contest/display");
		result.addObject("contest", contest);
		final Sponsorship sponsorshipRandom = this.sponsorshipService.getRandomOfAContest(idContest);
		result.addObject("sponsorshipRandom", sponsorshipRandom);
		result.addObject("requestURI", "contest/display.do?idContest=" + idContest);

		this.configValues(result);
		return result;
	}
}
