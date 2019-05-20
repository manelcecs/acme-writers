
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.WriterRepository;
import security.UserAccount;
import domain.Writer;

@Service
@Transactional
public class WriterService {

	@Autowired
	private WriterRepository	writerRepository;


	public Writer findByPrincipal(final UserAccount principal) {
		return this.writerRepository.findByPrincipal(principal.getId());
	}

}
