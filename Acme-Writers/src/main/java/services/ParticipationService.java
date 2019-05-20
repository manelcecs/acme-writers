
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ParticipationRepository;
import utiles.AuthorityMethods;
import domain.Contest;
import domain.Participation;

@Service
@Transactional
public class ParticipationService {

	@Autowired
	private ParticipationRepository	participationRepository;

	@Autowired
	private ContestService			contestService;


	public Participation create(final int idContest) {
		Assert.isTrue(AuthorityMethods.chechAuthorityLogged("WRITER"));
		final Participation participation = new Participation();
		final Contest contest = this.contestService.findOne(idContest);
		participation.setContest(contest);
		participation.setStatus("PENDING");
		return participation;
	}

	public Participation findOne(final int idParticipation) {
		return this.participationRepository.findOne(idParticipation);
	}

}
