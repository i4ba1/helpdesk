package id.co.knt.helpdesk.api.ctrl;

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
public class SNMgmtCtrl{

    @Autowired
    private SNService snService;

    //
    @RequestMapping(value="/register/", method = RequestMethod.POST)
    public ResponseEntity<SerialNumber> register(@RequestBody String serialNumber){
        SerialNumber number = snService.registerSerialNumber(serialNumber);
        if(!serialNumber.equals(null)){
            return new ResponseEntity<SerialNumber>(number, HttpStatus.OK);
        }

        return new ResponseEntity<SerialNumber>(number, HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value="/requestActiovationKey/{id}/{passKey}", method = RequestMethod.GET)
    public ResponseEntity<SerialNumber> getActivationKey(@PathVariable Long id, @PathVariable String passKey){
    	SerialNumber serialNumber = snService.generateActivationKey(id, passKey);
    	
    	return new ResponseEntity<SerialNumber>(serialNumber, HttpStatus.OK);
    }
    
    @RequestMapping(value="/activate/", method = RequestMethod.POST)
    public ResponseEntity<Void> activate(@PathVariable Long id, @PathVariable String activationKey){
    	int result = snService.activateActivationKey(id, activationKey);
    	if (result <= 0) {
    		return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
    	
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
    
}