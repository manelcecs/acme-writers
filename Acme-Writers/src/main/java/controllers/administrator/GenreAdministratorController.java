
package controllers.administrator;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.GenreService;
import controllers.AbstractController;
import domain.Genre;

@Controller
@RequestMapping("/genre/administrator")
public class GenreAdministratorController extends AbstractController {

	@Autowired
	private GenreService	genreService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		result = this.listModelAndView();
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		result = this.createModelAndView(this.genreService.create());
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(final int idGenre) {
		ModelAndView result;
		final Genre genre = this.genreService.findOne(idGenre);
		result = this.createModelAndView(genre);
		return result;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Genre genre, final BindingResult binding) {
		ModelAndView result;
		result = new ModelAndView("genre/edit");

		this.validateGenreName(genre, binding);

		if (binding.hasErrors())
			result = this.createModelAndView(genre);
		else
			try {
				this.genreService.save(genre);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createModelAndView(genre, "genre.save.error");
			}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int idGenre) {
		ModelAndView result;
		final Genre genre = this.genreService.findOne(idGenre);

		try {
			this.genreService.delete(genre);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.listModelAndView("genre.save.error");
			oops.printStackTrace();
		}

		return result;
	}

	protected ModelAndView listModelAndView() {
		return this.listModelAndView(null);
	}

	protected ModelAndView listModelAndView(final String message) {
		final ModelAndView result = new ModelAndView("genre/list");

		final Collection<Genre> genres = this.genreService.findAllMinusGENRE();
		result.addObject("genres", genres);
		result.addObject("message", message);

		this.configValues(result);
		return result;
	}

	protected ModelAndView createModelAndView(final Genre genre) {
		return this.createModelAndView(genre, null);
	}

	protected ModelAndView createModelAndView(final Genre genre, final String message) {
		final ModelAndView result = new ModelAndView("genre/edit");

		final Collection<Genre> genres = this.genreService.findAllMinusGENRE();
		genres.remove(genre);
		final Collection<Genre> allSubgenres = this.genreService.getChildrenOfAGenre(genre);
		genres.removeAll(allSubgenres);

		result.addObject("genre", genre);
		result.addObject("genres", genres);
		result.addObject("message", message);

		this.configValues(result);
		return result;
	}

	private void validateGenreName(final Genre genre, final BindingResult binding) {
		final Collection<String> namesEN = this.genreService.getAllNameEN();
		final Collection<String> namesES = this.genreService.getAllNameES();

		if (genre.getId() != 0) {
			namesEN.remove(this.genreService.findOne(genre.getId()).getNameEN());
			namesES.remove(this.genreService.findOne(genre.getId()).getNameES());
		}

		if (genre.getNameEN().toUpperCase().trim().equals("GENRE") || genre.getNameEN().toUpperCase().trim().equals("G�NERO") || genre.getNameES().toUpperCase().trim().equals("GENERO"))
			binding.rejectValue("nameEN", "genre.error.namelikeGenre");

		if (genre.getNameES().toUpperCase().trim().equals("G�NERO") || genre.getNameES().toUpperCase().trim().equals("GENERO") || genre.getNameES().toUpperCase().trim().equals("GENRE"))
			binding.rejectValue("nameES", "genre.error.namelikeGenre");

		if (namesEN.contains(genre.getNameEN().trim().toLowerCase()))
			binding.rejectValue("nameEN", "genre.error.namelikeOther");

		if (namesES.contains(genre.getNameES().trim().toLowerCase()))
			binding.rejectValue("nameES", "genre.error.namelikeOther");
	}
}
