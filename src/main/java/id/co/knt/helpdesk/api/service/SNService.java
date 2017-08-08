package id.co.knt.helpdesk.api.service;

import id.co.knt.helpdesk.api.model.License;
import id.co.knt.helpdesk.api.model.LicenseHistory;
import id.co.knt.helpdesk.api.model.dto.LicenseGeneratorDTO;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public interface SNService{
    License registerSerialNumber(License serialNumber, int state);

    License onlineActivation(License serialNumber);

    List<License> findAllSN();

    License findSN(Long id);
    
    License findBySerial(String serial);

    License generateActivationKey(Long id, String passKey);

    License manuallyActivate(Long licenseId, String passkey);

    TreeMap<String, List<License>> serialNumberGenerator(LicenseGeneratorDTO licenseGeneratorDTO);
    
    List<LicenseHistory> findUnreadLicense();
    
    Map<String, Object> viewDetailLicense(Long licenseId);
    
    LicenseHistory findDetailHistory(Long id);
    
    LicenseHistory updateReadStatus(LicenseHistory generatorHistory);

    List<Object> findSnCountByProduct();

    LicenseHistory fetchLicenseHistory(Long licenseId);

    void setLicenseHistory(License license, short status, String message);
}
