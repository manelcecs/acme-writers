
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.WriterRepository;
import domain.Writer;

@Service
@Transactional
public class WriterService {

	@Autowired
	WriterRepository	writerRepository;


	public Writer findByPrincipal(final int idPrincipal) {
		return this.writerRepository.findByPrincipal(idPrincipal);
	}

}
