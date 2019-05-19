
package services;

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

}
