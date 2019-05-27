
package controllers.writer;

import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ActorService;
import services.AnnouncementService;
import controllers.AbstractController;
import domain.Announcement;

@Controller
@RequestMapping("/announcement/writer")
public class AnnouncementWriterController extends AbstractController {

	@Autowired
	private ActorService		actorService;

	@Autowired
	private AnnouncementService	announcementService;


	@RequestMapping("/create")
	public ModelAndView create() {
		final Announcement ann = this.announcementService.create();
		final ModelAndView res = this.createModelAndView(ann);

		this.configValues(res);

		return res;
	}
	private ModelAndView createModelAndView(final Announcement announcement, final String... args) {
		final ModelAndView res = new ModelAndView("announcement/edit");

		res.addObject("announcement", announcement);
		for (final String s : args)
			res.addObject("message", s);

		this.configValues(res);

		return res;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView save(final Announcement announcement, final BindingResult binding) {
		ModelAndView res;

		try {
			final Announcement ann = this.announcementService.reconstruct(announcement, binding);
			final Announcement annF = this.announcementService.save(ann);
			res = this.displayModelAndView(annF.getId());
		} catch (final ValidationException oops) {
			res = this.createModelAndView(announcement);
		} catch (final Exception oops) {
			res = this.createModelAndView(announcement, "announcement.commit.error");
		}

		this.configValues(res);
		return res;
	}

	@RequestMapping("/list")
	public ModelAndView list() {
		final ModelAndView res = new ModelAndView("announcement/list");

		final Collection<Announcement> announcements = this.announcementService.findAllWriter(this.actorService.findByUserAccount(LoginService.getPrincipal()).getId());
		res.addObject("announcements", announcements);
		res.addObject("writer", true);
		this.configValues(res);

		return res;
	}

	@RequestMapping("/display")
	public ModelAndView display(final int announcementId) {
		ModelAndView res;

		res = this.displayModelAndView(announcementId);

		this.configValues(res);
		return res;
	}

	private ModelAndView displayModelAndView(final int announcementId) {
		final ModelAndView res = new ModelAndView("announcement/display");

		final Announcement ann = this.announcementService.findOne(announcementId);
		res.addObject("announcement", ann);
		res.addObject("back", true);

		this.configValues(res);

		return res;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int announcementId) {
		ModelAndView res;
		try {
			final Announcement ann = this.announcementService.findOne(announcementId);
			this.announcementService.delete(ann);

			res = this.listModelAndView();
		} catch (final Throwable oops) {
			res = this.listModelAndView("announcement.delete.error");
		}

		return res;
	}

	protected ModelAndView listModelAndView(final String... args) {
		final ModelAndView res = new ModelAndView("announcement/list");

		final Collection<Announcement> announcements = this.announcementService.findAllWriter(this.actorService.findByUserAccount(LoginService.getPrincipal()).getId());
		res.addObject("announcements", announcements);

		res.addObject("writer", true);

		for (final String s : args)
			res.addObject("message", s);

		this.configValues(res);

		return res;
	}

}
