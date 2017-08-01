package id.co.knt.helpdesk.api.service;

import id.co.knt.helpdesk.api.model.License;
import id.co.knt.helpdesk.api.model.LicenseGeneratorHistory;
import id.co.knt.helpdesk.api.model.dto.LicenseGeneratorDTO;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

    List<TreeMap<String, List<License>>> serialNumberGenerator(LicenseGeneratorDTO licenseGeneratorDTO);
    
    List<LicenseGeneratorHistory> findUnreadLicense();
    
    Map<String, Object> videDetailLicense(Long licenseId);
    
    LicenseGeneratorHistory findDetailHistory(Long id);
    
    LicenseGeneratorHistory updateReadStatus(LicenseGeneratorHistory generatorHistory);

    List<Object> findSnCountByProduct();
}
