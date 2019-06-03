
package controllers.reader;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ActorService;
import services.AnnouncementService;
import services.ReaderService;
import controllers.AbstractController;
import domain.Announcement;
import domain.Writer;

@Controller
@RequestMapping("/announcement/reader")
public class AnnouncementReaderController extends AbstractController {

	@Autowired
	private ActorService		actorService;

	@Autowired
	private ReaderService		readerService;

	@Autowired
	private AnnouncementService	announcementService;


	@RequestMapping("/list")
	public ModelAndView list() {
		final ModelAndView res = new ModelAndView("announcement/list");

		final Collection<Announcement> announcements = this.announcementService.findAllWriter(this.actorService.findByUserAccount(LoginService.getPrincipal()).getId());
		res.addObject("announcements", announcements);

		res.addObject("writer", false);
		res.addObject("requestURI", "/announcement/reader/list.do");
		this.configValues(res);

		return res;
	}

	@RequestMapping("/listAllMyWriters")
	public ModelAndView listMyWriters() {
		final ModelAndView res = new ModelAndView("announcement/list");

		final List<Writer> writers = (List<Writer>) this.readerService.findByPrincipal(LoginService.getPrincipal()).getWriters();
		final Collection<Announcement> announcements = this.announcementService.findAllMyWriters(writers);
		res.addObject("announcements", announcements);

		res.addObject("writer", false);
		res.addObject("requestURI", "/announcement/reader/listAllMyWriters.do");
		this.configValues(res);

		return res;
	}

	@RequestMapping("/display")
	public ModelAndView display(final int announcementId) {
		ModelAndView res;

		res = this.displayModelAndView(announcementId);
		return res;
	}

	private ModelAndView displayModelAndView(final int announcementId) {
		final ModelAndView res = new ModelAndView("announcement/display");

		final Announcement ann = this.announcementService.findOne(announcementId);
		res.addObject("announcement", ann);
		res.addObject("back", false);

		this.configValues(res);

		return res;
	}

}
