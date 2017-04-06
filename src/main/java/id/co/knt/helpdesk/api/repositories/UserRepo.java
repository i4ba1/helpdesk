package id.co.knt.helpdesk.api.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import id.co.knt.helpdesk.api.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
	
}
