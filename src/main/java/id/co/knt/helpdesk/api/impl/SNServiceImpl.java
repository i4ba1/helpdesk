package id.co.knt.helpdesk.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.co.knt.helpdesk.api.model.ActivationHistory;
import id.co.knt.helpdesk.api.model.SerialNumber;
import id.co.knt.helpdesk.api.repositories.HistoryRepo;
import id.co.knt.helpdesk.api.repositories.SNRepo;
import id.co.knt.helpdesk.api.service.SNService;
import id.web.pos.integra.gawl.Gawl;

@Service("snServiceImpl")
public class SNServiceImpl implements SNService {

	Logger LOG = LoggerFactory.getLogger(SNServiceImpl.class);

	private Gawl gawl = new Gawl();

	@Autowired
	private SNRepo snRepo;

	@Autowired
	private HistoryRepo historyRepo;

	@Override
	public SerialNumber registerSerialNumber(SerialNumber serialNumber) {
		SerialNumber snNumber = null;
		SerialNumber sn = snRepo.findBySerialNumber(serialNumber.getSerialNumber());

		if (sn == null) {
			if (gawl.validate(serialNumber.getSerialNumber())) {
				try {
					Map<String, Byte> extractResult = gawl.extract(serialNumber.getSerialNumber());
					if (extractResult.containsKey(Gawl.TYPE) && extractResult.containsKey(Gawl.MODULE)) {
						byte Type = extractResult.get(Gawl.TYPE);
						byte seed1 = extractResult.get(Gawl.SEED1);

						if (Type == 3) {
							// save the serial number
							if (extractResult.get(Gawl.SEED1) == seed1) {
								snNumber = new SerialNumber();
								snNumber.setSerialNumber(serialNumber.getSerialNumber());
								snNumber.setPassKey(serialNumber.getPassKey());
								snNumber.setXlock(serialNumber.getXlock());
								snNumber.setMacAddr(serialNumber.getMacAddr());
								snNumber.setActivationKey("");
								snNumber.setRegisterDate(new Date());
								snNumber.setSerialNumberStatus(false);
								snRepo.save(snNumber);
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
		} else {
			return null;
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
	public SerialNumber generateActivationKey(Long id, String passKey, String xlock) {
		String activationKey = "";
		SerialNumber snNumber = snRepo.findOne(id);
		try {
			if (passKey.equals(snNumber.getPassKey()) && snNumber.getXlock().equals(xlock)) {
				activationKey = gawl.activate(snNumber.getPassKey());
				snNumber.setActivationKey(activationKey);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return snNumber;
	}

	@Override
	public SerialNumber manuallyActivate(Long id, String xlock, String activationKey) {
		SerialNumber snNumber = snRepo.findOne(id);

		try {
			//Map<String, Byte> info = gawl.extract(snNumber.getSerialNumber());
			//String passKey = gawl.pass(((Byte) info.get("seed1")).byteValue(), ((Byte) info.get("seed2")).byteValue());
			activationKey = generateActivationKey(id, snNumber.getPassKey(), 
					snNumber.getXlock()).getActivationKey();
			snNumber.setActivationKey(activationKey);
			snNumber.setSerialNumberStatus(true);
			snNumber = snRepo.saveAndFlush(snNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return snNumber;
	}

	@Override
	public SerialNumber onlineActivation(SerialNumber serialNumber) {

		SerialNumber snNumber = null;
		SerialNumber sn = snRepo.findBySerialNumber(serialNumber.getSerialNumber());

		if (sn != null) {
			if (gawl.validate(serialNumber.getSerialNumber())) {
				try {
					Map<String, Byte> extractResult = gawl.extract(serialNumber.getSerialNumber());
					if (extractResult.containsKey(Gawl.TYPE) && extractResult.containsKey(Gawl.MODULE)) {
						byte Type = extractResult.get(Gawl.TYPE);

						if (Type == 3) {
							if (serialNumber.getPassKey().compareTo(sn.getPassKey()) == 0
									&& serialNumber.getXlock().compareTo(sn.getXlock()) == 0) {
								snNumber = new SerialNumber();

								if (snNumber != null) {
									snNumber = generateActivationKey(sn.getId(), serialNumber.getPassKey(),
											serialNumber.getXlock());
									snNumber.setActivationKey(snNumber.getActivationKey());
									snNumber.setSerialNumberStatus(true);
									snNumber = snRepo.saveAndFlush(snNumber);

									if (snNumber != null) {
										ActivationHistory history = new ActivationHistory(new Date(), snNumber);
										historyRepo.save(history);
									}
								}
							}
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return snNumber;
	}

	@Override
	public List<SerialNumber> findSNNeedActivated(List<SerialNumber> serialNumbers) {
		List<SerialNumber> list = new ArrayList<>();
		serialNumbers.forEach((sn) -> {
			SerialNumber number = snRepo.findOne(sn.getId());
			if (!sn.getMacAddr().equals(number.getMacAddr())) {
				number.setActivationKey("");
				number.setSerialNumberStatus(false);
				number = snRepo.saveAndFlush(number);
				list.add(number);
			}
		});

		return list;
	}

	@Override
	public SerialNumber findBySerial(String serial) {
		SerialNumber number = snRepo.findBySerialNumber(serial);
		return number;
	}
}
