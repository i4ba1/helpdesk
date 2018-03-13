package id.co.knt.helpdesk.api.repositories;


import id.co.knt.helpdesk.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
	@Query("select u from User as u where u.userName= :userName")
	User findUserByUserName(@Param("userName") String userName);

	@Query("select u from User as u where u.userName= :userName and u.password= :pass")
	User validateUser(@Param("userName") String userName, @Param("pass") String pass);
}
