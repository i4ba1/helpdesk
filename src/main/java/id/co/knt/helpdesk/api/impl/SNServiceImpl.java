package id.co.knt.helpdesk.api.impl;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simontuffs.onejar.Boot;

import id.co.knt.helpdesk.api.model.MacAddr;
import id.co.knt.helpdesk.api.model.SerialNumber;
import id.co.knt.helpdesk.api.repositories.MacAddrRepo;
import id.co.knt.helpdesk.api.repositories.SNRepo;
import id.co.knt.helpdesk.api.service.SNService;
import id.web.pos.integra.gawl.Gawl;

@Service("snServieImpl")
public class SNServiceImpl implements SNService {

	private Gawl gawl = new Gawl();

	@Autowired
	private SNRepo snRepo;

	@Autowired
	private MacAddrRepo macAddrRepo;

	@Override
	public String registerSerialNumber(String serialNumber) {
		SerialNumber snNumber = null;
		MacAddr ma = null;
		InetAddress addr;
		byte[] mac = null;
		
		try {
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
		}

		if (gawl.validate(serialNumber)) {
			try {
				Map<String, Byte> extractResult = gawl.extract(serialNumber);
				if (extractResult.containsKey(Gawl.TYPE) && extractResult.containsKey(Gawl.MODULE)) {
					byte Type = extractResult.get(Gawl.TYPE);
					byte seed1 = extractResult.get(Gawl.SEED1);
					byte seed2 = extractResult.get(Gawl.SEED2);
					// Generate passkey
					String passKey = gawl.pass(seed1, seed2);
					
					//Generate activation key
                    Boot.run(new String[]{passKey});

					if (Type == 3) {
						// get passkey and put into textbox
						if (extractResult.get(Gawl.SEED1) == seed1) {
							snNumber = new SerialNumber();
							snNumber.setSerialNumber(serialNumber);
							snNumber.setPassKey(passKey);
							snNumber.setMacAddr(ma);
							snNumber.setRegisterDate(new Date());
							snRepo.save(snNumber);
						} else {
							return "404";
						}
					} else {
						return "404";
					}

				} else {
					return "404";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
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