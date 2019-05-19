
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.ReaderRepository;
import security.UserAccount;
import domain.Reader;

@Service
@Transactional
public class ReaderService {

	@Autowired
	private ReaderRepository	readerRepository;


	public Reader findByPrincipal(final UserAccount principal) {
		return this.readerRepository.findByPrincipal(principal.getId());
	}

}
