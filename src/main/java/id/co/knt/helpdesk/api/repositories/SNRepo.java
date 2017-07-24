package id.co.knt.helpdesk.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import id.co.knt.helpdesk.api.model.License;

@Repository
public interface SNRepo extends JpaRepository<License, Long> {

	@Query("select sn from License sn where sn.license= :serialNumber")
	License findByLicense(@Param("serialNumber") String serialNumber);

	@Query("select sn from License sn where sn.activationKey= :activationKey")
	License findByActivation(@Param("activationKey") String activationKey);

	@Query("select sn from License sn join fetch sn.product p join fetch sn.school s where sn.id= :licenseId")
	License findLicenseById(@Param("licenseId") Long licenseId);
}