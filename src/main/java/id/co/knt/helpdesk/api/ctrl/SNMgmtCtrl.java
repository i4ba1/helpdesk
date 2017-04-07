package id.co.knt.helpdesk.api.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.TextNode;

import id.co.knt.helpdesk.api.model.SerialNumber;
import id.co.knt.helpdesk.api.service.SNService;

@RestController
@RequestMapping(value = "/snManagement")
public class SNMgmtCtrl{

    @Autowired
    private SNService snService;

    @RequestMapping(value="/register/", method = RequestMethod.POST)
    public ResponseEntity<SerialNumber> register(@RequestBody TextNode serialNumber){
        SerialNumber number = snService.registerSerialNumber(serialNumber.asText());
        if(!serialNumber.equals(null)){
            return new ResponseEntity<SerialNumber>(number, HttpStatus.OK);
        }

        return new ResponseEntity<SerialNumber>(number, HttpStatus.NOT_FOUND);
    }


}