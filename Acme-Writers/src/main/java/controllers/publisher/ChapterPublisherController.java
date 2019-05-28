
package controllers.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ChapterService;
import services.PublisherService;
import controllers.AbstractController;
import domain.Chapter;
import domain.Publisher;

@Controller
@RequestMapping("/chapter/publisher")
public class ChapterPublisherController extends AbstractController {

	@Autowired
	private ChapterService		chapterService;

	@Autowired
	private PublisherService	publisherService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(required = true) final int idChapter) {
		ModelAndView result = new ModelAndView("chapter/display");

		final Chapter chapter = this.chapterService.findOne(idChapter);
		final Publisher publiserLogged = this.publisherService.findByPrincipal(LoginService.getPrincipal());

		if (!chapter.getBook().getPublisher().equals(publiserLogged) || chapter.getBook().getCancelled() || chapter.getBook().getDraft())
			//FIXME: PROBAR ESTE REDIRECT
			result = new ModelAndView("redirect:/book/publisher/list.do");
		else {
			result.addObject("chapter", chapter);
			result.addObject("publisher", true);
		}

		this.configValues(result);

		return result;
	}

}
