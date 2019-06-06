
package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/misc")
public class MiscController extends AbstractController {

	@RequestMapping(value = "/termsAndConditions")
	public ModelAndView display() {
		final ModelAndView res = new ModelAndView("misc/termsAndConditions");
		this.configValues(res);
		return res;
	}
}
