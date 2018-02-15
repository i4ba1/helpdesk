package id.co.knt.helpdesk.api.controller;

import java.util.*;

import id.co.knt.helpdesk.api.model.dto.LicenseGeneratorDTO;
import id.co.knt.helpdesk.api.model.dto.ListLicenseDTO;
import id.co.knt.helpdesk.api.repositories.SNRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import id.co.knt.helpdesk.api.model.License;
import id.co.knt.helpdesk.api.model.LicenseHistory;
import id.co.knt.helpdesk.api.service.SNService;

/**
 * @author kntdev
 */
@RestController
@RequestMapping(value = "/snManagement")
public class SNManagementController {

    @Autowired
    private SNService snService;

    @Autowired
    private SNRepo snRepo;

    /**
     * To handle register license
     *
     * @param license
     * @return ResponseEntity<Integer>
     */
    @RequestMapping(value = "/register/", method = RequestMethod.POST)
    public ResponseEntity<Integer> register(@RequestBody License license) {
        int error = snService.registerSN(license);
        if (error == 1) {
            return new ResponseEntity<>(error, HttpStatus.ACCEPTED);
        } else if (error == 2) {
            return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(error, HttpStatus.OK);
    }

    @RequestMapping(value = "/registerInHelpdesk/", method = RequestMethod.POST)
    public ResponseEntity<License> registerInHelpdesk(@RequestBody License license) {
        Map<String, Object> map = snService.registerInHelpdesk(license);

        /**
         * If the serial number invalid or any error when extracting SN
         */
        if ((int) map.get("error") == 2) {
            return new ResponseEntity<>((License) map.get("license"), HttpStatus.FORBIDDEN);
        } else if ((int) map.get("error") == 3) {// If passKey is not valid to generate activationKey
            return new ResponseEntity<>((License) map.get("license"), HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>((License) map.get("license"), HttpStatus.OK);
    }

    /**
     * To generate activation key based on passkey
     *
     * @param id
     * @param passKey
     * @return ResponseEntity<License>
     */
    @RequestMapping(value = "/requestActivationKey/{id}/{passKey}", method = RequestMethod.GET)
    public ResponseEntity<License> requestActivationKey(@PathVariable Long id, @PathVariable String passKey) {
        License serialNumber = snService.generateActivationKey(id, passKey);
        if (serialNumber.equals(null)) {
            return new ResponseEntity<>(serialNumber, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(serialNumber, HttpStatus.OK);
    }

    /**
     * This method to handle activation by Phone
     *
     * @param licenseId
     * @param passkey
     * @param reason
     * @return ResponseEntity<License>
     */
    @RequestMapping(value = "/activate/", method = RequestMethod.POST)
    public ResponseEntity<License> activateByPhone(@RequestParam("licenseId") Long licenseId,
                                                   @RequestParam("passkey") String passkey, @RequestParam("reason") String reason) {

        License result = snService.findSN(licenseId);

        /*
         * if passkey client is not same on the Helpdesk
         */
        if (!passkey.equals(result.getPassKey())) {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }

        try {
            if (result.getNumberOfActivation() < result.getActivationLimit()) {
                if (result.getActivationKey() == null || result.getActivationKey().length() <= 0) {
                    result = snService.manuallyActivate(licenseId, passkey, reason);
                } else {
                    result = snService.manuallyActivate(licenseId, passkey, reason);
                }
            } else {
                return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result == null) {
            return new ResponseEntity<>(result, HttpStatus.EXPECTATION_FAILED);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * This method to handle activation by internet
     *
     * @param serialNumber
     * @return ResponseEntity<License>
     */
    @RequestMapping(value = "/activateByInternet/", method = RequestMethod.POST)
    public ResponseEntity<License> activateByInternet(@RequestBody License serialNumber) {
        License currentLicense = snService.findBySerial(serialNumber.getLicense());

        if (currentLicense.getPassKey() != null && currentLicense.getPassKey().compareTo(serialNumber.getPassKey()) != 0){
            return new ResponseEntity<>(currentLicense, HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            if (currentLicense.getActivationKey() == null || currentLicense.getActivationKey().length() <= 0) {
                currentLicense = snService.onlineActivation(serialNumber);
            } else {
                if (currentLicense.getNumberOfActivation() < currentLicense.getActivationLimit()) {
                    currentLicense = snService.onlineActivation(serialNumber);
                } else {
                    return new ResponseEntity<>(currentLicense, HttpStatus.FORBIDDEN);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (currentLicense == null) {
            return new ResponseEntity<>(currentLicense, HttpStatus.EXPECTATION_FAILED);
        }

        return new ResponseEntity<>(currentLicense, HttpStatus.OK);
    }

    /**
     * This method to fetch all serial numbers and for search with filter by license, school name, and create date
     *
     * @param category
     * @param page
     * @param searchText
     * @param startDate
     * @param endDate
     * @return ResponseEntity<Map   <   String   ,       Object>>
     */
    @RequestMapping(value = {"/serialNumbers/"}, method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> findAllSN(@RequestParam String category, @RequestParam int page, @RequestParam String searchText, @RequestParam Long startDate, @RequestParam Long endDate) {

        Map<String, Object> result = snService.findAllSN(category, page, searchText, startDate, endDate);
        if (result.isEmpty()) {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * View detail of license table
     *
     * @param id
     * @return ResponseEntity<Map   <   String   ,       Object>>
     */
    @RequestMapping(value = "/viewDetailSN/{id}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> viewDetailSN(@PathVariable Long id) {
        Map<String, Object> objectMap = snService.viewDetailLicense(id);
        if (objectMap == null) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }

        return new ResponseEntity<>(objectMap, HttpStatus.OK);
    }

    /**
     * @param licenseGeneratorDTO
     * @return ResponseEntity<TreeMap   <   String   ,       List   <   License>>>
     */
    @RequestMapping(value = {"/snGenerator/"}, method = RequestMethod.POST)
    public ResponseEntity<TreeMap<String, List<License>>> snGenerator(
            @RequestBody LicenseGeneratorDTO licenseGeneratorDTO) {

        TreeMap<String, List<License>> treeMap = snService.serialNumberGenerator(licenseGeneratorDTO);

        if (treeMap.isEmpty()) {
            return new ResponseEntity<>(treeMap, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(treeMap, HttpStatus.OK);
    }

    /**
     * ResponseEntity<List<Map<String, Object>>>
     *
     * @param list
     * @return generate license
     */
    @RequestMapping(value = "/registerGeneratedSN/", method = RequestMethod.POST)
    public ResponseEntity<List<Map<String, Object>>> registerGeneratedSN(@RequestBody List<License> list) {
        //List<List<ListLicenseDTO>> listList = new ArrayList<>();
        List<ListLicenseDTO> licenseDTOS = new ArrayList<>();
        Map<String, Object> objectMap = null;
        List<Map<String, Object>> result = new ArrayList<>();

        for (License license : list) {
            licenseDTOS.add(snService.saveGeneratedSN(license).get(0));
        }

        for (ListLicenseDTO data : licenseDTOS) {
            objectMap = snService.generateLicenseDTOResult(data);
            result.add(objectMap);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * To find unread license to show on dashboard
     *
     * @return ResponseEntity<List   <   LicenseHistory>>
     */
    @RequestMapping(value = "/findUnreadLicenses/", method = RequestMethod.GET)
    public ResponseEntity<List<LicenseHistory>> findUnreadLicenses() {
        List<LicenseHistory> unreadLicenses = snService.findUnreadLicense();

        if (unreadLicenses.isEmpty()) {
            return new ResponseEntity<>(unreadLicenses, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(unreadLicenses, HttpStatus.OK);

    }

    /**
     *
     */
    @RequestMapping(value = "/licenseCountByProduct/", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, Object>>> findSnCountByProduct() {
        List<Object> list = snService.findSnCountByProduct();
        List<Map<String, Object>> mapReturn = new ArrayList<>();
        if (list.size() > 0) {
            for (Object object : list) {
                Object[] values = (Object[]) object;
                Map<String, Object> newMap = new HashMap<>();

                newMap.put("productName", values[0]);
                newMap.put("licenseCount", values[1]);
                mapReturn.add(newMap);

            }
            return new ResponseEntity<>(mapReturn, HttpStatus.OK);
        }
        return new ResponseEntity<>(mapReturn, HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/viewDetailUnreadLicense/{licenseId}/{historyId}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> viewDetailUnreadLicense(@PathVariable Long licenseId,
                                                                       @PathVariable Long historyId) {
        Map<String, Object> object = snService.viewDetailLicense(licenseId);

        if (object.isEmpty()) {
            return new ResponseEntity<>(object, HttpStatus.NOT_FOUND);
        }

        LicenseHistory generatorHistory = snService.findDetailHistory(historyId);
        generatorHistory.setIsRead(true);
        snService.updateReadStatus(generatorHistory);

        return new ResponseEntity<>(object, HttpStatus.OK);
    }

    @RequestMapping(value = "/updateSchool/", method = RequestMethod.POST)
    public ResponseEntity<License> updateSchool(@RequestParam Long licenseId, @RequestParam String schoolName) {
        License license = snRepo.findOne(licenseId);
        if (license == null) {
            return new ResponseEntity<>(license, HttpStatus.NOT_FOUND);
        }
        license.setSchoolName(schoolName);

        return new ResponseEntity<>(snRepo.saveAndFlush(license), HttpStatus.OK);
    }

    @RequestMapping(value = "/overrideActivationLimit/", method = RequestMethod.POST)
    public ResponseEntity<License> overrideActivationLimit(@RequestParam("licenseId") Long licenseId,
                                                           @RequestParam("message") String message, @RequestParam("file") MultipartFile file) {
        License license = snRepo.findOne(licenseId);
        if (license == null) {
            return new ResponseEntity<>(license, HttpStatus.NOT_FOUND);
        }
        license.setActivationLimit((short) (license.getActivationLimit() + 1));
        snService.setLicenseHistory(license, (short) 3, message, file);

        return new ResponseEntity<>(snRepo.saveAndFlush(license), HttpStatus.OK);

    }

    @RequestMapping(value = "/blocked/", method = RequestMethod.POST)
    public ResponseEntity<License> blocked(@RequestParam Long licenseId, @RequestParam String message) {
        License license = snRepo.findOne(licenseId);
        if (license == null) {
            return new ResponseEntity<>(license, HttpStatus.NOT_FOUND);
        }
        license.setActivationLimit((short) (license.getActivationLimit() + 1));
        snService.setLicenseHistory(license, (short) 4, message, null);

        return new ResponseEntity<>(snRepo.saveAndFlush(license), HttpStatus.OK);
    }

    @RequestMapping(value = "/validateActivationKey", method = RequestMethod.POST)
    public ResponseEntity<Integer> checkActivationCode(@RequestBody License license) {
        License currentLicense = snRepo.findByLicense(license.getLicense());

        if (currentLicense == null) {
            return new ResponseEntity<Integer>(-1, HttpStatus.NOT_FOUND);
        }

        try {
            if (!currentLicense.getActivationKey().equals(license.getActivationKey())) {
                return new ResponseEntity<>(-1, HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(0, HttpStatus.OK);
    }
}