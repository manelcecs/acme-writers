
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ChapterService;
import domain.Chapter;

@Controller
@RequestMapping("/chapter")
public class ChapterController extends AbstractController {

	@Autowired
	private ChapterService	chapterService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(required = true) final int idChapter) {
		ModelAndView result = new ModelAndView("chapter/display");

		final Chapter chapter = this.chapterService.findOne(idChapter);

		if (!chapter.getBook().getDraft() && !chapter.getBook().getCancelled() && (chapter.getBook().getStatus().equals("INDEPENDENT") || chapter.getBook().getStatus().equals("ACCEPTED")))
			result.addObject("chapter", chapter);
		else
			//FIXME: PROBAR ESTE REDIRECT
			result = new ModelAndView("redirect:/book/listAll.do");

		this.configValues(result);
		return result;
	}
}
