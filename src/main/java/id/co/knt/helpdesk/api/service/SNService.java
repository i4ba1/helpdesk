package id.co.knt.helpdesk.api.service;

import id.co.knt.helpdesk.api.model.SerialNumber;
import java.util.List;

public interface SNService{
    SerialNumber registerSerialNumber(String serialNumber);
    List<SerialNumber> findAllSN();

    SerialNumber findSN(Long id);

    void deleteSN(Long id);
}