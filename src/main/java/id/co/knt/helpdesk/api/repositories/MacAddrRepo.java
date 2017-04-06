package id.co.knt.helpdesk.api.repositories;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import id.co.knt.helpdesk.api.model.MacAddr;

@Repository
public interface MacAddrRepo extends JpaRepository<MacAddr, Long> {
    @Query("select ma from MacAddr ma where ma.macAdress= :mac")
	MacAddr findByMacAddress(@Param("mac")byte[] mac);
}
