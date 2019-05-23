
package controllers.writer;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.BookService;
import services.ChapterService;
import services.WriterService;
import domain.Book;
import domain.Chapter;
import domain.Writer;

@Controller
@RequestMapping("/chapter/writer")
public class ChapterWriterController {

	@Autowired
	BookService		bookService;

	@Autowired
	ChapterService	chapterService;

	@Autowired
	WriterService	writerService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(required = true) final int idChapter) {
		ModelAndView result = new ModelAndView("chapter/display");

		final Chapter chapter = this.chapterService.findOne(idChapter);
		final Writer writerLogged = this.writerService.findByPrincipal(LoginService.getPrincipal().getId());

		if (!chapter.getBook().getWriter().equals(writerLogged))
			//FIXME: PROBAR ESTE REDIRECT
			result = new ModelAndView("redirect:/book/writer/list.do");
		else {
			result.addObject("chapter", chapter);
			result.addObject("myChapter", true);
		}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam(required = true) final int idBook) {
		ModelAndView result;

		final Book book = this.bookService.findOne(idBook);
		final Writer writerLogged = this.writerService.findByPrincipal(LoginService.getPrincipal().getId());

		if (!book.getWriter().equals(writerLogged) || !book.getDraft())
			//FIXME: PROBAR ESTE REDIRECT
			result = new ModelAndView("redirect:/book/writer/list.do");
		else {
			final Chapter chapter = this.chapterService.create(book);
			result = this.createEditModelAndView(chapter);
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam(required = true) final int idChapter) {
		ModelAndView result;

		final Chapter chapter = this.chapterService.findOne(idChapter);
		final Writer writerLogged = this.writerService.findByPrincipal(LoginService.getPrincipal().getId());

		if (!chapter.getBook().getWriter().equals(writerLogged) || !chapter.getBook().getDraft())
			//FIXME: PROBAR ESTE REDIRECT
			result = new ModelAndView("redirect:/book/writer/list.do");
		else
			result = this.createEditModelAndView(chapter);
		return result;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView save(@Valid final Chapter chapter, final BindingResult bindingResult) {
		ModelAndView result;

		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(chapter);
		else
			try {
				this.chapterService.save(chapter);
				result = new ModelAndView("redirect:/book/writer/display.do?idBook=" + chapter.getBook().getId());
			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = this.createEditModelAndView(chapter, "cannot.save.chapter");
			}
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam(required = true) final int idChapter) {
		ModelAndView result;
		final Chapter chapter = this.chapterService.findOne(idChapter);

		try {
			this.chapterService.delete(idChapter);
			result = new ModelAndView("redirect:/book/writer/display.do?idBook=" + chapter.getBook().getId());
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/book/writer/display.do?idBook=" + chapter.getBook().getId());
		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final Chapter chapter) {
		return this.createEditModelAndView(chapter, null);
	}

	protected ModelAndView createEditModelAndView(final Chapter chapter, final String message) {
		final ModelAndView result = new ModelAndView("chapter/edit");

		result.addObject("chapter", chapter);
		result.addObject("message", message);

		return result;
	}

}
