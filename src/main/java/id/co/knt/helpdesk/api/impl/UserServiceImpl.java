package id.co.knt.helpdesk.api.impl;

import java.util.List;

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
		userRepo.save(user);
		return null;
	}

	@Override
	public User updateUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> findAllUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getDetailUser(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
}
