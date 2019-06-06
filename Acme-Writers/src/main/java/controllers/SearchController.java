
package controllers;

import java.util.Collection;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.BookService;
import domain.Book;
import forms.SearchForm;

@Controller
@RequestMapping("/search")
public class SearchController extends AbstractController {

	@Autowired
	private BookService	bookService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView search(@CookieValue(value = "keyword", required = false) final String keyword) {
		final ModelAndView result;

		final SearchForm searchForm = new SearchForm();
		searchForm.setKeyword(keyword);

		result = this.createEditModelAndView(searchForm);
		return result;
	}
	@RequestMapping(value = "/display", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final HttpServletResponse response, @Valid final SearchForm searchForm, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(searchForm);
		else
			try {
				result = new ModelAndView("redirect:display.do");
				response.addCookie(new Cookie("keyword", searchForm.getKeyword()));

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(searchForm, "search.commit.error");
			}

		return result;
	}

	protected ModelAndView createEditModelAndView(final SearchForm searchForm) {

		final ModelAndView result;

		result = this.createEditModelAndView(searchForm, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final SearchForm searchForm, final String messageCode) {
		final ModelAndView result;

		result = new ModelAndView("search/display");

		result.addObject("searchForm", searchForm);

		final Collection<Book> books = this.bookService.getFilterBooksByKeyword(searchForm.getKeyword());
		result.addObject("books", books);
		result.addObject("requestURI", "search/display.do");
		result.addObject("message", messageCode);

		this.configValues(result);
		return result;
	}
}
