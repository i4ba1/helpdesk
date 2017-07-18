/**
 * 
 */
package id.co.knt.helpdesk.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import id.co.knt.helpdesk.api.model.School;

/**
 * @author marlina_kreatif
 */
@Repository
public interface SchoolRepo extends JpaRepository<School, Integer> {
	
	@Query("select s from School s where s.deleted = false")
	List<School> findAllSchool();	

}
