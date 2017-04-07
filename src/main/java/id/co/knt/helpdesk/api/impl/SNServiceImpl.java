package id.co.knt.helpdesk.api.impl;

import java.util.Date;
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
		
		/*try {
			addr = InetAddress.getLocalHost();
			NetworkInterface ni = NetworkInterface.getByInetAddress(addr);
			mac = ni.getHardwareAddress();
			ma = macAddrRepo.findByMacAddress(mac);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (ma == null) {
			ma = new MacAddr();
			ma.setMacAddress(mac);
			ma.setCreatedDate(new Date());
			macAddrRepo.save(ma);
		}*/

		//gawl.generate(arg0, arg1)
		//gawl.generate(type, module)
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
							//snRepo.save(snNumber);
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

}