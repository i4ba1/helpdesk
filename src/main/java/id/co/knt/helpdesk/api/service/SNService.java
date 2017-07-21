package id.co.knt.helpdesk.api.service;

import id.co.knt.helpdesk.api.model.License;
import id.co.knt.helpdesk.api.model.LicenseGeneratorHistory;

import java.util.List;

public interface SNService{
    License registerSerialNumber(License serialNumber);

    License onlineActivation(License serialNumber);
    
    List<License> findSNNeedActivated(List<License> serialNumbers);

    List<License> findAllSN();

    License findSN(Long id);
    
    License findBySerial(String serial);

    void deleteSN(Long id);

    License generateActivationKey(Long id, String passKey, String xlock);

    License manuallyActivate(Long id, String xlock, String activationKey);
    
    List<License> serialNumberGenerator(Integer productId,Integer licenseCount,Integer secondParam);
    
    List<LicenseGeneratorHistory> findUnreadLicense();
}
