package id.co.knt.helpdesk.api.service;

import id.co.knt.helpdesk.api.model.Login;
import id.co.knt.helpdesk.api.model.User;


public interface LoginService {
	
	Login saveLogin(Login login);
	
	Boolean validateToken(String token, Long today);
	
	Login findByUser(User u);
	
	Login updateToken(Login login);
	
	void deleteToken(Login login);
	
	Login findByToken(String token);
	
	Login findById(Long id);
}
