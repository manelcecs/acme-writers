
package controllers.reader;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ReaderService;
import controllers.AbstractController;
import domain.Reader;
import forms.ReaderForm;

@Controller
@RequestMapping("/reader")
public class ReaderReaderControler extends AbstractController {

	@Autowired
	private ReaderService	readerService;


	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register() {
		ModelAndView res;

		final ReaderForm readerForm = new ReaderForm();

		res = this.createEditModelAndView(readerForm);

		return res;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView save(final ReaderForm readerForm, final BindingResult binding) {

		ModelAndView res;

		try {
			final Reader readerRect = this.readerService.reconstruct(readerForm, binding);
			this.readerService.save(readerRect);
			res = new ModelAndView("redirect:/welcome/index.do");
		} catch (final ValidationException oops) {
			res = this.createEditModelAndView(readerForm);
		} catch (final Throwable oops) {
			res = this.createEditModelAndView(readerForm, "reader.edit.commit.error");

		}

		return res;

	}

	protected ModelAndView createEditModelAndView(final ReaderForm readerForm, final String... messages) {

		final ModelAndView result;

		result = new ModelAndView("reader/edit");
		result.addObject("readerForm", readerForm);
		result.addObject("edit", false);

		final List<String> messageCodes = new ArrayList<>();
		for (final String s : messages)
			messageCodes.add(s);
		result.addObject("messages", messageCodes);

		this.configValues(result);

		return result;

	}

	protected ModelAndView createEditModelAndView(final Reader reader, final String... messages) {

		final ModelAndView result;

		result = new ModelAndView("reader/edit");
		result.addObject("reader", reader);
		result.addObject("edit", true);
		final List<String> messageCodes = new ArrayList<>();
		for (final String s : messages)
			messageCodes.add(s);
		result.addObject("messages", messageCodes);

		this.configValues(result);

		return result;

	}

}
