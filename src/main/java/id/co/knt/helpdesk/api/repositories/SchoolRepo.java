/**
 * 
 */
package id.co.knt.helpdesk.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import id.co.knt.helpdesk.api.model.School;

/**
 * @author marlina_kreatif
 */
@Repository
public interface SchoolRepo extends JpaRepository<School, Integer> {

}
