package id.co.knt.helpdesk.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import id.co.knt.helpdesk.api.model.LicenseHistory;

/**
 * 
 * @author MNI
 *
 */
@Repository
public interface LicenseHistoryRepo extends JpaRepository<LicenseHistory, Long> {

	@Query("select lh from LicenseHistory lh inner join fetch lh.license")
	List<LicenseHistory> fetchUnreadLicenseGenerator();

	@Query("select lh from LicenseHistory lh where lh.license.id= :licenseId order by createdDate desc")
	List<LicenseHistory> findLicenseHistory(@Param("licenseId") Long licenseId);
}
