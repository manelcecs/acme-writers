
package controllers.writer;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.WriterService;
import controllers.AbstractController;
import domain.Writer;
import forms.WriterForm;

@Controller
@RequestMapping("/writer")
public class WriterWriterController extends AbstractController {

	@Autowired
	private WriterService	writerService;


	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register() {
		ModelAndView res;

		final WriterForm writerForm = new WriterForm();

		res = this.createEditModelAndView(writerForm);

		return res;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView save(final WriterForm writerForm, final BindingResult binding) {

		ModelAndView res;

		try {
			final Writer writerRect = this.writerService.reconstruct(writerForm, binding);
			this.writerService.save(writerRect);
			res = new ModelAndView("redirect:/welcome/index.do");
		} catch (final ValidationException oops) {
			res = this.createEditModelAndView(writerForm);
		} catch (final Throwable oops) {
			res = this.createEditModelAndView(writerForm, "writer.edit.commit.error");

		}

		return res;

	}

	protected ModelAndView createEditModelAndView(final WriterForm writerForm, final String... messages) {

		final ModelAndView result;

		result = new ModelAndView("writer/edit");
		result.addObject("writerForm", writerForm);
		result.addObject("edit", false);

		final List<String> messageCodes = new ArrayList<>();
		for (final String s : messages)
			messageCodes.add(s);
		result.addObject("messages", messageCodes);

		this.configValues(result);

		return result;

	}

	protected ModelAndView createEditModelAndView(final Writer writer, final String... messages) {

		final ModelAndView result;

		result = new ModelAndView("writer/edit");
		result.addObject("writer", writer);
		result.addObject("edit", true);
		final List<String> messageCodes = new ArrayList<>();
		for (final String s : messages)
			messageCodes.add(s);
		result.addObject("messages", messageCodes);

		this.configValues(result);

		return result;

	}
}
