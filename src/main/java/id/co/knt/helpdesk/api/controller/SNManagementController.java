package id.co.knt.helpdesk.api.controller;

import java.util.*;

import id.co.knt.helpdesk.api.model.dto.LicenseGeneratorDTO;
import id.co.knt.helpdesk.api.repositories.SNRepo;
import id.web.pos.integra.gawl.Gawl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/register/", method = RequestMethod.POST)
    public ResponseEntity<License> register(@RequestBody License serialNumber) {
        if (snService.findBySerial(serialNumber.getLicense()) != null) {
            serialNumber = null;
            return new ResponseEntity<>(serialNumber, HttpStatus.CONFLICT);
        }

        License number = snService.registerSerialNumber(serialNumber, 1);
        if (!serialNumber.equals(null)) {
            return new ResponseEntity<>(number, HttpStatus.OK);
        }

        return new ResponseEntity<>(number, HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/requestActivationKey/{id}/{passKey}", method = RequestMethod.GET)
    public ResponseEntity<License> requestActivationKey(@PathVariable Long id, @PathVariable String passKey) {
        License serialNumber = snService.generateActivationKey(id, passKey);
        if (serialNumber.equals(null)) {
            return new ResponseEntity<>(serialNumber, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(serialNumber, HttpStatus.OK);
    }

    @RequestMapping(value = "/activate/{licenseId}/{passkey}/{reason}", method = RequestMethod.POST)
    public ResponseEntity<License> activate(@PathVariable("licenseId") Long licenseId, @PathVariable("passkey") String passkey, @PathVariable("reason") String reason) {
        Gawl gawl = new Gawl();
        License result = snService.findSN(licenseId);

        try {
            if (result.getActivationKey() == null || result.getActivationKey().length() <= 0) {
                result = snService.manuallyActivate(licenseId, passkey, reason);
            } else if (gawl.challenge(passkey, result.getActivationKey())) {
                result = snService.manuallyActivate(licenseId, passkey, reason);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result == null) {
            return new ResponseEntity<>(result, HttpStatus.EXPECTATION_FAILED);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/activateByInternet/", method = RequestMethod.POST)
    public ResponseEntity<License> activateByInternet(@RequestBody License serialNumber) {
        Gawl gawl = new Gawl();
        License currentLicense = snService.findBySerial(serialNumber.getLicense());
        License result = null;

        try {
            if (currentLicense.getActivationKey() == null || currentLicense.getActivationKey().length() <= 0) {
                result = snService.onlineActivation(serialNumber);
            } else if (gawl.challenge(serialNumber.getPassKey(), currentLicense.getActivationKey())) {
                result = snService.onlineActivation(serialNumber);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if (result == null) {
            return new ResponseEntity<>(result, HttpStatus.EXPECTATION_FAILED);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, Object>>> findAllSN() {
        List<License> listSN = snService.findAllSN();
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> objectMap = null;

        for (License license : listSN) {
            objectMap = new TreeMap<>();
            objectMap.put("serialNumber", license);
            objectMap.put("status", (int) snService.fetchLicenseHistory(license.getId()).getLicenseStatus());
            result.add(objectMap);
        }

        if (result.isEmpty()) {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/viewDetailSN/{id}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> viewDetailSN(@PathVariable Long id) {
        Map<String, Object> objectMap = snService.viewDetailLicense(id);
        if (objectMap.equals(null)) {
            return new ResponseEntity<>(objectMap, HttpStatus.EXPECTATION_FAILED);
        }

        return new ResponseEntity<>(objectMap, HttpStatus.OK);
    }

    /**
     * @param licenseGeneratorDTO
     * @return
     */
    @RequestMapping(value = {"/snGenerator/"}, method = RequestMethod.POST)
    public ResponseEntity<TreeMap<String, List<License>>> snGenerator(@RequestBody LicenseGeneratorDTO licenseGeneratorDTO) {

        TreeMap<String, List<License>> treeMap = snService.serialNumberGenerator(licenseGeneratorDTO);

        if (treeMap.isEmpty()) {
            return new ResponseEntity<>(treeMap, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(treeMap, HttpStatus.OK);
    }

    @RequestMapping(value = "/registerGeneratedSN/", method = RequestMethod.POST)
    public ResponseEntity<Void> registerGeneratedSN(@RequestBody List<License> list) {
        for (License license : list) {
            snService.registerSerialNumber(license, 0);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

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
    public ResponseEntity<Map<String, Object>> viewDetailUnreadLicense(@PathVariable Long licenseId, @PathVariable Long historyId) {
        Map<String, Object> object = snService.viewDetailLicense(licenseId);

        if (object.isEmpty()) {
            return new ResponseEntity<>(object, HttpStatus.NOT_FOUND);
        }

        LicenseHistory generatorHistory = snService.findDetailHistory(historyId);
        generatorHistory.setIsRead(true);
        snService.updateReadStatus(generatorHistory);

        return new ResponseEntity<>(object, HttpStatus.OK);
    }

    @RequestMapping(value = "/updateSchool/{licenseId}/{schoolName}", method = RequestMethod.PUT)
    public ResponseEntity<License> updateSchool(@PathVariable Long licenseId, @PathVariable String schoolName) {
        License license = snRepo.findOne(licenseId);
        if (license == null) {
            return new ResponseEntity<>(license, HttpStatus.NOT_FOUND);
        }
        license.setSchoolName(schoolName);

        return new ResponseEntity<>(snRepo.saveAndFlush(license), HttpStatus.OK);
    }

    @RequestMapping(value="/overrideActivationLimit/{licenseId}/{message}", method = RequestMethod.PUT)
    public ResponseEntity<License> overrideActivationLimit(@PathVariable Long licenseId, @PathVariable String message){
        License license = snRepo.findOne(licenseId);
        if (license == null) {
            return new ResponseEntity<>(license, HttpStatus.NOT_FOUND);
        }
        license.setActivationLimit((short)(license.getActivationLimit()+1));
        snService.setLicenseHistory(license, (short)3, message);

        return new ResponseEntity<>(snRepo.saveAndFlush(license), HttpStatus.OK);
    }

    @RequestMapping(value="/blocked/{licenseId}/{message}", method = RequestMethod.PUT)
    public ResponseEntity<License> blocked(@PathVariable Long licenseId, @PathVariable String message){
        License license = snRepo.findOne(licenseId);
        if (license == null) {
            return new ResponseEntity<>(license, HttpStatus.NOT_FOUND);
        }
        license.setActivationLimit((short)(license.getActivationLimit()+1));
        snService.setLicenseHistory(license, (short)4, message);

        return new ResponseEntity<>(snRepo.saveAndFlush(license), HttpStatus.OK);
    }
}