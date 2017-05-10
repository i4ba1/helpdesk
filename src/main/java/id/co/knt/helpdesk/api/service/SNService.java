package id.co.knt.helpdesk.api.service;

import id.co.knt.helpdesk.api.model.SerialNumber;
import java.util.List;

public interface SNService{
    SerialNumber registerSerialNumber(SerialNumber serialNumber);

    SerialNumber onlineActivation(SerialNumber serialNumber);
    
    List<SerialNumber> findSNNeedActivated(List<SerialNumber> serialNumbers);

    List<SerialNumber> findAllSN();

    SerialNumber findSN(Long id);
    
    SerialNumber findBySerial(String serial);

    void deleteSN(Long id);

    SerialNumber generateActivationKey(Long id, String passKey, String xlock);

    SerialNumber manuallyActivate(Long id, String xlock, String activationKey);
}
