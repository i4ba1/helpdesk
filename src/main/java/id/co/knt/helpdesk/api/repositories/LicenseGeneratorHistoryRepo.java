package id.co.knt.helpdesk.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import id.co.knt.helpdesk.api.model.LicenseGeneratorHistory;

/**
 * 
 * @author MNI
 *
 */
@Repository
public interface LicenseGeneratorHistoryRepo extends JpaRepository<LicenseGeneratorHistory, Long> {

	@Query("select generator from LicenseGeneratorHistory generator inner join fetch generator.license where generator.isRead=false")
	List<LicenseGeneratorHistory> fetchUnreadLicenseGenerator();
}
