
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ParticipationService;
import domain.Participation;

@Controller
@RequestMapping("/participation/publisher,writer")
public class ParticipationController extends AbstractController {

	@Autowired
	private ParticipationService	participationService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int idParticipation) {
		final ModelAndView result;

		final Participation participation = this.participationService.findOne(idParticipation);

		result = new ModelAndView("participation/display");
		if (utiles.AuthorityMethods.chechAuthorityLogged("WRITER"))
			result.addObject("writer", true);
		else
			result.addObject("publisher", true);
		result.addObject("participation", participation);

		this.configValues(result);
		return result;

	}
}
