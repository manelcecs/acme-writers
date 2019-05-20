
package services;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repositories.AdminConfigRepository;
import domain.AdminConfig;

@Service
@Transactional
public class AdminConfigService {

	@Autowired
	private AdminConfigRepository	adminConfigRepository;


	public AdminConfig getAdminConfig() {
		return this.adminConfigRepository.findAll().get(0);
	}

	public boolean existSpamWord(final String s) {
		final String palabras[] = s.split("[.,:;()¿?" + " " + "\t!¡]");
		final List<String> listaPalabras = Arrays.asList(palabras);
		boolean exist = false;
		final AdminConfig administratorConfig = this.getAdminConfig();
		final Collection<String> spamWord = administratorConfig.getSpamWords();
		for (final String palabraLista : listaPalabras)
			if (spamWord.contains(palabraLista.toLowerCase().trim())) {
				exist = true;
				break;
			}
		return exist;
	}

}
