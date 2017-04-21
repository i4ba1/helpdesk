package id.co.knt.helpdesk.api.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping(value="/register/{serialNumber}", method = RequestMethod.POST)
    public ResponseEntity<SerialNumber> register(@PathVariable String serialNumber){
        SerialNumber number = snService.registerSerialNumber(serialNumber);
        if(!serialNumber.equals(null)){
            return new ResponseEntity<SerialNumber>(number, HttpStatus.OK);
        }

        return new ResponseEntity<SerialNumber>(number, HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value="/requestActivationKey/{id}/{passKey}", method = RequestMethod.GET)
    public ResponseEntity<SerialNumber> getActivationKey(@PathVariable Long id, @PathVariable String passKey){
    	SerialNumber serialNumber = snService.generateActivationKey(id, passKey);
    	if(serialNumber.equals(null)){
    		return new ResponseEntity<SerialNumber>(serialNumber, HttpStatus.NOT_FOUND);
    	}
    	
    	return new ResponseEntity<SerialNumber>(serialNumber, HttpStatus.OK);
    }
    
    @RequestMapping(value="/activate/{id}/{activationKey}", method = RequestMethod.POST)
    public ResponseEntity<Void> activate(@PathVariable Long id, @PathVariable String activationKey){
    	int result = snService.activateActivationKey(id, activationKey);
    	if (result <= 0) {
    		return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
    	
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @RequestMapping(value="/registerAndActivated/{serialNumber}", method = RequestMethod.POST)
    public ResponseEntity<Integer> activate(@PathVariable String serialNumber){
    	int result = snService.registerAndActivate(serialNumber);
    	if (result > 0) {
    		return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
    	
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @RequestMapping(value={ "" }, method = RequestMethod.GET)
    public ResponseEntity<List<SerialNumber>> findAllSN(){
    	List<SerialNumber> listSN = snService.findAllSN();
    	if(listSN.isEmpty()){
    		return new ResponseEntity<List<SerialNumber>>(listSN, HttpStatus.EXPECTATION_FAILED);
    	}
    	
    	return new ResponseEntity<List<SerialNumber>>(listSN, HttpStatus.OK);
    }
    
    @RequestMapping(value="/viewDetailSN/{id}", method = RequestMethod.GET)
    public ResponseEntity<SerialNumber> viewDetailSN (@PathVariable Long id){
    	SerialNumber serialNumber = snService.findSN(id);
    	if(serialNumber.equals(null)){
    		return new ResponseEntity<SerialNumber>(serialNumber, HttpStatus.EXPECTATION_FAILED);
    	}
    	
    	return new ResponseEntity<SerialNumber>(serialNumber, HttpStatus.OK);
    }
}