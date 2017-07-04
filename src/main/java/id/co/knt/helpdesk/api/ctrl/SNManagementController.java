package id.co.knt.helpdesk.api.ctrl;

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

@RestController
@RequestMapping(value = "/snManagement")
public class SNManagementController {

	@Autowired
	private SNService snService;

	@RequestMapping(value = "/register/", method = RequestMethod.POST)
	public ResponseEntity<License> register(@RequestBody License serialNumber) {
		if(snService.findBySerial(serialNumber.getLicense()) != null){
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
}