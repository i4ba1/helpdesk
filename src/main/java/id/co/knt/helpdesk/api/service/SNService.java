package id.co.knt.helpdesk.api.service;

import id.co.knt.helpdesk.api.model.License;
import id.co.knt.helpdesk.api.model.LicenseHistory;
import id.co.knt.helpdesk.api.model.dto.LicenseGeneratorDTO;
import id.co.knt.helpdesk.api.model.dto.ListLicenseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public interface SNService{
    List<ListLicenseDTO> saveGeneratedSN(List<License> serialNumber);

    int registerSN(License serialNumber);

    Map<String, Object> registerInHelpdesk(License serialNumber);

    License onlineActivation(License serialNumber);

    Map<String, Object> findAllSN(String category, int page, String searchText, Long startDate, Long endDate);

    License findSN(Long id);
    
    License findBySerial(String serial);

    License generateActivationKey(Long id, String passKey);

    License manuallyActivate(Long licenseId, String passkey, String reason);

    TreeMap<String, Object> serialNumberGenerator(LicenseGeneratorDTO licenseGeneratorDTO);
    
    List<LicenseHistory> findUnreadLicense();
    
    Map<String, Object> viewDetailLicense(Long licenseId);
    
    LicenseHistory findDetailHistory(Long id);
    
    LicenseHistory updateReadStatus(LicenseHistory generatorHistory);

    List<Object> findSnCountByProduct();

    LicenseHistory fetchLicenseHistory(Long licenseId);

    void setLicenseHistory(License license, short status, String message, MultipartFile file);

    Map<String, Object> generateLicenseDTOResult(ListLicenseDTO data);

    List<ListLicenseDTO> saveLicenseEntities(Set<License> licenses);

    void emptyListOfLicense();
}
