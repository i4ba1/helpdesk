package id.co.knt.helpdesk.api.controller;

import java.util.*;

import id.co.knt.helpdesk.api.model.dto.LicenseGeneratorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import id.co.knt.helpdesk.api.model.License;
import id.co.knt.helpdesk.api.model.LicenseGeneratorHistory;
import id.co.knt.helpdesk.api.service.SNService;

/**
 * @author kntdev
 *
 */
@RestController
@RequestMapping(value = "/snManagement")
public class SNManagementController {

	@Autowired
	private SNService snService;

	@RequestMapping(value = "/register/", method = RequestMethod.POST)
	public ResponseEntity<License> register(@RequestBody License serialNumber) {
		if (snService.findBySerial(serialNumber.getLicense()) != null) {
			serialNumber = null;
			return new ResponseEntity<>(serialNumber, HttpStatus.CONFLICT);
		}

		License number = snService.registerSerialNumber(serialNumber);
		if (!serialNumber.equals(null)) {
			return new ResponseEntity<>(number, HttpStatus.OK);
		}

		return new ResponseEntity<>(number, HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/requestActivationKey/{id}/{passKey}/{xlock}", method = RequestMethod.GET)
	public ResponseEntity<License> requestActivationKey(@PathVariable Long id, @PathVariable String passKey,
			@PathVariable String xlock) {
		License serialNumber = snService.generateActivationKey(id, passKey, xlock);
		if (serialNumber.equals(null)) {
			return new ResponseEntity<>(serialNumber, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(serialNumber, HttpStatus.OK);
	}

	@RequestMapping(value = "/activate/", method = RequestMethod.POST)
	public ResponseEntity<License> activate(@RequestBody License serialNumber) {
		License result = snService.manuallyActivate(serialNumber.getId(), serialNumber.getXlock(),
				serialNumber.getActivationKey());
		if (result == null) {
			return new ResponseEntity<>(result, HttpStatus.EXPECTATION_FAILED);
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/activateByInternet/", method = RequestMethod.POST)
	public ResponseEntity<License> activateByInternet(@RequestBody License serialNumber) {
		License result = snService.onlineActivation(serialNumber);
		if (result == null) {
			return new ResponseEntity<>(result, HttpStatus.EXPECTATION_FAILED);
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@RequestMapping(value = { "" }, method = RequestMethod.GET)
	public ResponseEntity<List<License>> findAllSN() {
		List<License> listSN = snService.findAllSN();
		if (listSN.isEmpty()) {
			return new ResponseEntity<>(listSN, HttpStatus.EXPECTATION_FAILED);
		}

		return new ResponseEntity<>(listSN, HttpStatus.OK);
	}

	@RequestMapping(value = "/viewDetailSN/{id}", method = RequestMethod.GET)
	public ResponseEntity<License> viewDetailSN(@PathVariable Long id) {
		License serialNumber = snService.findSN(id);
		if (serialNumber.equals(null)) {
			return new ResponseEntity<>(serialNumber, HttpStatus.EXPECTATION_FAILED);
		}

		return new ResponseEntity<>(serialNumber, HttpStatus.OK);
	}

	/**
	 *
	 * @param licenseGeneratorDTO
	 * @return
	 */
	@RequestMapping(value = { "/snGenerator/" }, method = RequestMethod.POST)
	public ResponseEntity<List<TreeMap<String, List<License>>>> snGenerator(@RequestBody LicenseGeneratorDTO licenseGeneratorDTO) {

		List<TreeMap<String, List<License>>> list = snService.serialNumberGenerator(licenseGeneratorDTO);

		if (list.isEmpty()) {
			return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/registerGeneratedSN/", method = RequestMethod.POST)
	public ResponseEntity<String> registerGeneratedSN(@RequestBody List<License> list) {
		for (License license : list) {
			snService.registerSerialNumber(license);
		}

		return new ResponseEntity<>(new String(), HttpStatus.OK);
	}

	@RequestMapping(value = "/findUnreadLicenses/", method = RequestMethod.GET)
	public ResponseEntity<List<LicenseGeneratorHistory>> findUnreadLicenses() {
		List<LicenseGeneratorHistory> unreadLicenses = snService.findUnreadLicense();

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

	@RequestMapping(value="/viewDetailUnreadLicense/{licenseId}/{historyId}", method=RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> viewDetailUnreadLicense(@PathVariable Long licenseId, @PathVariable Long historyId){
		Map<String, Object> object = snService.videDetailLicense(licenseId);
	
		if(object.isEmpty()) {
			return new ResponseEntity<>(object, HttpStatus.NOT_FOUND);
		}
		
		LicenseGeneratorHistory generatorHistory = snService.findDetailHistory(historyId);
		generatorHistory.setIsRead(true);
		snService.updateReadStatus(generatorHistory);
		
		return new ResponseEntity<>(object, HttpStatus.OK);
	}
}