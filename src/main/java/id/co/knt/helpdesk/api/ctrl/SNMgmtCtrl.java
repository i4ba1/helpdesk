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

import id.co.knt.helpdesk.api.model.SerialNumber;
import id.co.knt.helpdesk.api.service.SNService;

@RestController
@RequestMapping(value = "/snManagement")
public class SNMgmtCtrl {

	@Autowired
	private SNService snService;

	@RequestMapping(value = "/register/", method = RequestMethod.POST)
	public ResponseEntity<SerialNumber> register(@RequestBody SerialNumber serialNumber) {
		if(snService.findBySerial(serialNumber.getSerialNumber()) != null){
			serialNumber = null;
			return new ResponseEntity<SerialNumber>(serialNumber, HttpStatus.CONFLICT);
		}
		
		SerialNumber number = snService.registerSerialNumber(serialNumber);
		if (!serialNumber.equals(null)) {
			return new ResponseEntity<SerialNumber>(number, HttpStatus.OK);
		}

		return new ResponseEntity<SerialNumber>(number, HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/requestActivationKey/{id}/{passKey}/{xlock}", method = RequestMethod.GET)
	public ResponseEntity<SerialNumber> requestActivationKey(@PathVariable Long id, @PathVariable String passKey,
			@PathVariable String xlock) {
		SerialNumber serialNumber = snService.generateActivationKey(id, passKey, xlock);
		if (serialNumber.equals(null)) {
			return new ResponseEntity<SerialNumber>(serialNumber, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<SerialNumber>(serialNumber, HttpStatus.OK);
	}

	@RequestMapping(value = "/activate/", method = RequestMethod.POST)
	public ResponseEntity<SerialNumber> activate(@RequestBody SerialNumber serialNumber) {
		SerialNumber result = snService.manuallyActivate(serialNumber.getId(), serialNumber.getXlock(),
				serialNumber.getActivationKey());
		if (result == null) {
			return new ResponseEntity<SerialNumber>(result, HttpStatus.EXPECTATION_FAILED);
		}

		return new ResponseEntity<SerialNumber>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/activateByInternet/", method = RequestMethod.POST)
	public ResponseEntity<SerialNumber> activateByInternet(@RequestBody SerialNumber serialNumber) {
		SerialNumber result = snService.onlineActivation(serialNumber);
		if (result == null) {
			return new ResponseEntity<SerialNumber>(result, HttpStatus.EXPECTATION_FAILED);
		}

		return new ResponseEntity<SerialNumber>(result, HttpStatus.OK);
	}

	@RequestMapping(value = { "" }, method = RequestMethod.GET)
	public ResponseEntity<List<SerialNumber>> findAllSN() {
		List<SerialNumber> listSN = snService.findAllSN();
		if (listSN.isEmpty()) {
			return new ResponseEntity<List<SerialNumber>>(listSN, HttpStatus.EXPECTATION_FAILED);
		}

		return new ResponseEntity<List<SerialNumber>>(listSN, HttpStatus.OK);
	}

	@RequestMapping(value = "/viewDetailSN/{id}", method = RequestMethod.GET)
	public ResponseEntity<SerialNumber> viewDetailSN(@PathVariable Long id) {
		SerialNumber serialNumber = snService.findSN(id);
		if (serialNumber.equals(null)) {
			return new ResponseEntity<SerialNumber>(serialNumber, HttpStatus.EXPECTATION_FAILED);
		}

		return new ResponseEntity<SerialNumber>(serialNumber, HttpStatus.OK);
	}
}