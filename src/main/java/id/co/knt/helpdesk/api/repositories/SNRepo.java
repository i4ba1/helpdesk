package id.co.knt.helpdesk.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import id.co.knt.helpdesk.api.model.SerialNumber;

@Repository
public interface SNRepo extends JpaRepository<SerialNumber, Long> {
	
    @Query("select sn from SerialNumber sn where sn.serialNumber= :serialNumber")
    SerialNumber findBySerialNumber(@Param("serialNumber") String serialNumber);
}