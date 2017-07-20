package id.co.knt.helpdesk.api.ctrl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import id.co.knt.helpdesk.api.model.License;
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
			return new ResponseEntity<License>(serialNumber, HttpStatus.CONFLICT);
		}

		License number = snService.registerSerialNumber(serialNumber);
		if (!serialNumber.equals(null)) {
			return new ResponseEntity<License>(number, HttpStatus.OK);
		}

		return new ResponseEntity<License>(number, HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/requestActivationKey/{id}/{passKey}/{xlock}", method = RequestMethod.GET)
	public ResponseEntity<License> requestActivationKey(@PathVariable Long id, @PathVariable String passKey,
			@PathVariable String xlock) {
		License serialNumber = snService.generateActivationKey(id, passKey, xlock);
		if (serialNumber.equals(null)) {
			return new ResponseEntity<License>(serialNumber, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<License>(serialNumber, HttpStatus.OK);
	}

	@RequestMapping(value = "/activate/", method = RequestMethod.POST)
	public ResponseEntity<License> activate(@RequestBody License serialNumber) {
		License result = snService.manuallyActivate(serialNumber.getId(), serialNumber.getXlock(),
				serialNumber.getActivationKey());
		if (result == null) {
			return new ResponseEntity<License>(result, HttpStatus.EXPECTATION_FAILED);
		}

		return new ResponseEntity<License>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/activateByInternet/", method = RequestMethod.POST)
	public ResponseEntity<License> activateByInternet(@RequestBody License serialNumber) {
		License result = snService.onlineActivation(serialNumber);
		if (result == null) {
			return new ResponseEntity<License>(result, HttpStatus.EXPECTATION_FAILED);
		}

		return new ResponseEntity<License>(result, HttpStatus.OK);
	}

	@RequestMapping(value = { "" }, method = RequestMethod.GET)
	public ResponseEntity<List<License>> findAllSN() {
		List<License> listSN = snService.findAllSN();
		if (listSN.isEmpty()) {
			return new ResponseEntity<List<License>>(listSN, HttpStatus.EXPECTATION_FAILED);
		}

		return new ResponseEntity<List<License>>(listSN, HttpStatus.OK);
	}

	@RequestMapping(value = "/viewDetailSN/{id}", method = RequestMethod.GET)
	public ResponseEntity<License> viewDetailSN(@PathVariable Long id) {
		License serialNumber = snService.findSN(id);
		if (serialNumber.equals(null)) {
			return new ResponseEntity<License>(serialNumber, HttpStatus.EXPECTATION_FAILED);
		}

		return new ResponseEntity<License>(serialNumber, HttpStatus.OK);
	}

	/**
	 * @author Marlina_Kreatif
	 * 
	 * @param productId
	 * @param licenseCount
	 * @param secondParam
	 * @return list<License>
	 */
	@RequestMapping(value = { "/snGenerator/{productId}/{licenseCount}/{secondParam}" }, method = RequestMethod.GET)
	public ResponseEntity<List<License>> snGenerator(@PathVariable Integer productId,
			@PathVariable Integer licenseCount, @PathVariable Integer secondParam) {

		List<License> list = snService.serialNumberGenerator(productId, licenseCount, secondParam);

		if (list.isEmpty()) {
			return new ResponseEntity<List<License>>(list, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<List<License>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/registerGeneratedSN/", method = RequestMethod.POST)
	public ResponseEntity<String> registerGeneratedSN(@RequestBody List<License> list) {
		int counter = 0;
		for (License license : list) {
			License number = snService.registerSerialNumber(license);
			
		}
		
		return new ResponseEntity<String>(new String(), HttpStatus.OK);
	}
	
	@RequestMapping(value="/findUnreadLicenses/", method=RequestMethod.GET)
	public ResponseEntity<List<License>> findUnreadLicenses(){
		List<License> unreadLicenses = snService.findUnreadLicense();
	
		if(unreadLicenses.isEmpty()) {
			return new ResponseEntity<List<License>>(unreadLicenses, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<List<License>>(unreadLicenses, HttpStatus.OK);
	}
}