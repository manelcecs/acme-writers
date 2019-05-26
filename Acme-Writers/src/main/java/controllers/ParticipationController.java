
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ParticipationService;
import services.PublisherService;
import services.WriterService;
import domain.Participation;

@Controller
@RequestMapping("/participation/publisher,writer")
public class ParticipationController extends AbstractController {

	@Autowired
	private ParticipationService	participationService;

	@Autowired
	private WriterService			writerService;

	@Autowired
	private PublisherService		publisherService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int idParticipation) {
		ModelAndView result;

		final Participation participation = this.participationService.findOne(idParticipation);

		result = new ModelAndView("participation/display");
		if (utiles.AuthorityMethods.chechAuthorityLogged("WRITER")) {
			if (participation.getBook().getWriter().getId() == this.writerService.findByPrincipal(LoginService.getPrincipal()).getId()) {
				result.addObject("writer", true);
				result.addObject("participation", participation);
			} else
				result = new ModelAndView("redirect:/participation/writer/list.do");
		} else if (participation.getContest().getPublisher().getId() == this.publisherService.findByPrincipal(LoginService.getPrincipal()).getId()) {
			result.addObject("publisher", true);
			result.addObject("participation", participation);
		} else
			result = new ModelAndView("redirect:/participation/publisher/list.do");

		this.configValues(result);
		return result;

	}
}
