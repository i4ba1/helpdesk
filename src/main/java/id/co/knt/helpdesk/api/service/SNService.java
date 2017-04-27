package id.co.knt.helpdesk.api.service;

import id.co.knt.helpdesk.api.model.SerialNumber;
import java.util.List;

public interface SNService{
    SerialNumber registerSerialNumber(String serialNumber);

    SerialNumber onlineActivation(String serialNumber, String passKey, String xlock);
    
    List<SerialNumber> findSNNeedActivated(List<SerialNumber> serialNumbers);

    List<SerialNumber> findAllSN();

    SerialNumber findSN(Long id);

    void deleteSN(Long id);

    SerialNumber generateActivationKey(Long id, String passKey);

    int activateActivationKey(Long id, String activationKey);
}
