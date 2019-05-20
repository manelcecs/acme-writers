
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ContestService;
import domain.Contest;

@Controller
@RequestMapping("/contest")
public class ContestController extends AbstractController {

	@Autowired
	private ContestService	contestService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result = new ModelAndView("contest/list");
		final Collection<Contest> contests = this.contestService.findAll();

		result.addObject("contests", contests);
		result.addObject("viewAll", true);
		result.addObject("requestURI", "contest/list.do");

		//		this.configValues(result);
		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int idContest) {
		ModelAndView result;

		final Contest contest = this.contestService.findOne(idContest);

		result = new ModelAndView("contest/display");
		result.addObject("contest", contest);
		result.addObject("requestURI", "contest/display.do?idContest=" + idContest);

		//		this.configValues(result);
		return result;
	}
}
