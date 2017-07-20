package id.co.knt.helpdesk.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author MNI
 *
 */
@Repository
public interface LicenseGeneratorHistoryRepo extends JpaRepository<LicenseGeneratorHistoryRepo, Long> {

}
