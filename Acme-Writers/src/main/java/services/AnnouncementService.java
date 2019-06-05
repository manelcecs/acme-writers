
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AnnouncementRepository;
import security.Authority;
import security.LoginService;
import utiles.AuthorityMethods;
import domain.Announcement;
import domain.Writer;

@Service
@Transactional
public class AnnouncementService {

	@Autowired
	private WriterService			writerService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private AnnouncementRepository	announcementRepository;

	@Autowired
	private Validator				validator;


	public Announcement create() {
		final Announcement res = new Announcement();
		final Writer writer = this.writerService.findByPrincipal(LoginService.getPrincipal());

		res.setMoment(new Date());
		res.setWriter(writer);

		return res;
	}

	public Announcement save(final Announcement announcement) {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged(Authority.WRITER));

		Assert.isTrue(announcement.getWriter().getId() == this.actorService.findByUserAccount(LoginService.getPrincipal()).getId());
		Date date = new Date();

		announcement.setMoment(date);

		final Announcement res = this.announcementRepository.save(announcement);

		return res;
	}

	public Announcement saveAnonymize(final Announcement announcement) {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged(Authority.WRITER));

		Assert.isTrue(announcement.getWriter().getId() == this.actorService.findByUserAccount(LoginService.getPrincipal()).getId());

		final Announcement res = this.announcementRepository.save(announcement);

		return res;
	}

	public Announcement reconstruct(final Announcement ann, final BindingResult binding) {

		final Announcement res = new Announcement();

		res.setWriter(ann.getWriter());
		res.setMoment(ann.getMoment());
		res.setText(ann.getText());

		this.validator.validate(res, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		return res;
	}
	public void delete(final Announcement announcement) {
		Assert.isTrue(announcement.getWriter().getId() == this.actorService.findByUserAccount(LoginService.getPrincipal()).getId());
		this.announcementRepository.delete(announcement);
	}

	public Collection<Announcement> findAll() {
		return this.announcementRepository.findAll();
	}

	public Collection<Announcement> findAllWriter(final int writerId) {
		return this.announcementRepository.findAllWriter(writerId);
	}

	public Announcement findOne(final int announcementId) {
		return this.announcementRepository.findOne(announcementId);
	}

	public Collection<Announcement> findAllMyWriters(final List<Writer> writers) {
		final List<Announcement> res = new ArrayList<>();

		for (final Writer i : writers)
			res.addAll(this.findAllWriter(i.getId()));

		Collections.sort(res, new Comparator<Announcement>() {

			@Override
			public int compare(final Announcement o1, final Announcement o2) {
				return o1.getMoment().compareTo(o2.getMoment());
			}
		});

		return res;
	}

}
