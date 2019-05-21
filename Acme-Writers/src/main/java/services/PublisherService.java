
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.PublisherRepository;
import domain.Publisher;

@Service
@Transactional
public class PublisherService {

	@Autowired
	PublisherRepository	publisherRepository;


	public Publisher findByPrincipal(final int idPrincipal) {
		return this.publisherRepository.findByPrincipal(idPrincipal);
	}

	public Publisher findOne(final int idPublisher) {
		return this.publisherRepository.findOne(idPublisher);
	}

	public Collection<Publisher> findAll() {
		return this.publisherRepository.findAll();

	}
}
