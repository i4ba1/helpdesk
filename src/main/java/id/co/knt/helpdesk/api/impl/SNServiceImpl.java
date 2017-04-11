package id.co.knt.helpdesk.api.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import id.co.knt.helpdesk.api.model.SerialNumber;
import id.co.knt.helpdesk.api.repositories.SNRepo;
import id.co.knt.helpdesk.api.service.SNService;
import id.web.pos.integra.gawl.Gawl;

@Service("snServieImpl")
public class SNServiceImpl implements SNService {

	private Gawl gawl = new Gawl();

	@Autowired
	private SNRepo snRepo;

	@Override
	public SerialNumber registerSerialNumber(String serialNumber) {
		SerialNumber snNumber = null;
		String passKey = "";
		
		if (gawl.validate(serialNumber)) {
			try {
				Map<String, Byte> extractResult = gawl.extract(serialNumber);
				if (extractResult.containsKey(Gawl.TYPE) && extractResult.containsKey(Gawl.MODULE)) {
					byte Type = extractResult.get(Gawl.TYPE);
					byte seed1 = extractResult.get(Gawl.SEED1);
					byte seed2 = extractResult.get(Gawl.SEED2);
					// Generate passkey
					passKey = gawl.pass(seed1, seed2);

					if (Type == 3) {
						//save the serial number
						if (extractResult.get(Gawl.SEED1) == seed1) {
							snNumber = new SerialNumber();
							snNumber.setSerialNumber(serialNumber);
							snNumber.setPassKey(passKey);
							snNumber.setRegisterDate(new Date());
							snNumber = snRepo.save(snNumber);
						} else {
							return null;
						}
					} else {
						return null;
					}

				} else {
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return snNumber;
	}

	@Override
	public List<SerialNumber> findAllSN() {
		List<SerialNumber> serialNumbers = snRepo.findAll();
		return serialNumbers;
	}

	@Override
	public SerialNumber findSN(Long id) {
		SerialNumber sn = snRepo.findOne(id);
		return sn;
	}

	@Override
	public void deleteSN(Long id) {
		snRepo.delete(id);
	}

	@Override
	public SerialNumber generateActivationKey(Long id, String passKey){
		String activationKey = "";
		SerialNumber snNumber = snRepo.findOne(id);
		try{
			activationKey = gawl.activate(passKey);
			snNumber.setActivationKey(activationKey);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return snNumber;
	}

	@Override
	public int activateActivationKey(Long id, String activationKey){
		SerialNumber snNumber = snRepo.findOne(id);
		Map<String, Byte> info = new HashMap<>();
		int result = 0;

		try{
			info = gawl.extract(snNumber.getSerialNumber());
			String passKey = gawl.pass(((Byte)info.get("seed1")).byteValue(), ((Byte)info.get("seed2")).byteValue());
			if (!gawl.challenge(passKey, activationKey)) {
				result = -1;
			} else {
				snNumber.setActivationKey(activationKey);
				snRepo.save(snNumber);
				result = 1;
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return result;
	}
}