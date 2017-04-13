package id.co.knt.helpdesk.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.co.knt.helpdesk.api.model.User;
import id.co.knt.helpdesk.api.repositories.UserRepo;
import id.co.knt.helpdesk.api.service.UserService;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;
	
	@Override
	public User registerUser(User user) {
		User u = userRepo.save(user);
		
		return u;
	}

	@Override
	public User updateUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getDetailUser(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User validateUser(String userName, String pass) {
		return userRepo.validateUser(userName, pass);
	}

	@Override
	public User findUserByUsername(String userName) {
		User currentUser = userRepo.findUserByUserName(userName);
		
		return currentUser;
	}
}
