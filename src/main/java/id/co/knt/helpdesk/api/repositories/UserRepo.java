package id.co.knt.helpdesk.api.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import id.co.knt.helpdesk.api.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
	@Query("select u from User as u where u.userName= :userName")
	User findUserByUserName(@Param("userName") String userName);
}
