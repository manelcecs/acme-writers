
package controllers;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ContestService;
import services.SponsorshipService;
import utiles.AuthorityMethods;
import domain.Contest;
import domain.Sponsorship;

@Controller
@RequestMapping("/contest")
public class ContestController extends AbstractController {

	@Autowired
	private ContestService		contestService;

	@Autowired
	private SponsorshipService	sponsorshipService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() throws ParseException {

		final ModelAndView result = new ModelAndView("contest/list");
		final Collection<Contest> contests = this.contestService.findAll();
		final Date actual = new Date();
		result.addObject("contests", contests);
		result.addObject("viewAll", true);
		result.addObject("requestURI", "contest/list.do");
		result.addObject("actual", actual);
		result.addObject("targetURL", "/contest/list.do");
		if (AuthorityMethods.checkIsSomeoneLogged())
			if (utiles.AuthorityMethods.chechAuthorityLogged("WRITER"))
				result.addObject("canParticipate", this.contestService.getContestCanParticipate());
		this.configValues(result);
		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int idContest, @RequestParam(required = false, defaultValue = "contest/list.do") final String urlBack) throws ParseException {

		ModelAndView result;

		final Contest contest = this.contestService.findOne(idContest);

		result = new ModelAndView("contest/display");
		result.addObject("contest", contest);
		final Sponsorship sponsorshipRandom = this.sponsorshipService.getRandomOfAContest(idContest);
		result.addObject("sponsorshipRandom", sponsorshipRandom);
		result.addObject("requestURI", "contest/display.do?idContest=" + idContest);
		result.addObject("urlBack", urlBack);

		this.configValues(result);
		return result;
	}
}
