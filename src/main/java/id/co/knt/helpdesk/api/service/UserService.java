package id.co.knt.helpdesk.api.service;

import id.co.knt.helpdesk.api.model.User;

public interface UserService {
	User registerUser(User user);

	User updateUser(User user);

	User getDetailUser(Integer id);
}
