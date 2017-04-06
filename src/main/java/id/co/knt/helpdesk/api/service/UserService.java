package id.co.knt.helpdesk.api.service;

import id.co.knt.helpdesk.api.model.User;
import java.util.List;

public interface UserService {
	User registerUser(User user);

	User updateUser(User user);

	List<User> findAllUser();

	User getDetailUser(Integer id);
}
