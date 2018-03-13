package id.co.knt.helpdesk.api.impl;

import id.co.knt.helpdesk.api.model.Login;
import id.co.knt.helpdesk.api.model.User;
import id.co.knt.helpdesk.api.repositories.LoginRepository;
import id.co.knt.helpdesk.api.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("loginService")
public class LoginServiceImpl implements LoginService {
	
	@Autowired
	private LoginRepository loginRepo;

	@Override
	public Login saveLogin(Login login) {
		Login newLogin = loginRepo.save(login);
		
		return newLogin;
	}

	@Override
	public Boolean validateToken(String token, Long today) {
		Login l = loginRepo.validateToken(token, today);
		return l == null?false:true;
	}

	@Override
	public Login findByUser(User u) {
		Login l =loginRepo.findByUser(u);
		return l;
	}

	@Override
	public Login updateToken(Login login) {
		return loginRepo.saveAndFlush(login);
	}

	@Override
	public void deleteToken(Login login) {
		loginRepo.delete(login.getId());
	}

	@Override
	public Login findByToken(String token) {
		Login l = loginRepo.findByToken(token);
		
		return l;
	}

	@Override
	public Login findById(Long id) {
		Login l = loginRepo.findOne(id);
		return l;
	}
}
