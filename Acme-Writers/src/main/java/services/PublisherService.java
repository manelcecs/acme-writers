
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.PublisherRepository;
import security.UserAccount;
import domain.Publisher;

@Service
@Transactional
public class PublisherService {

	@Autowired
	private PublisherRepository	publisherRepository;


	public Publisher findByPrincipal(final UserAccount principal) {
		return this.publisherRepository.findByPrincipal(principal.getId());
	}

}
