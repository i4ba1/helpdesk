package id.co.knt.helpdesk.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

	private Gawl gawl = new Gawl();

	@Autowired
	private SNRepo snRepo;

	@Autowired
	private HistoryRepo historyRepo;

	@Override
	public SerialNumber registerSerialNumber(SerialNumber serialNumber) {
		SerialNumber snNumber = null;
		String passKey = "";
		SerialNumber sn = snRepo.findBySerialNumber(serialNumber.getSerialNumber());

		if (sn == null) {
			if (gawl.validate(serialNumber.getSerialNumber())) {
				try {
					Map<String, Byte> extractResult = gawl.extract(serialNumber.getSerialNumber());
					if (extractResult.containsKey(Gawl.TYPE) && extractResult.containsKey(Gawl.MODULE)) {
						byte Type = extractResult.get(Gawl.TYPE);
						byte seed1 = extractResult.get(Gawl.SEED1);
						byte seed2 = extractResult.get(Gawl.SEED2);
						// Generate passkey
						passKey = gawl.pass(seed1, seed2);
						String xlock = gawl.xlock(serialNumber.getSerialNumber());

						if (Type == 3) {
							// save the serial number
							if (extractResult.get(Gawl.SEED1) == seed1) {
								snNumber = new SerialNumber();
								snNumber.setSerialNumber(serialNumber.getSerialNumber());
								snNumber.setPassKey(passKey);
								snNumber.setXlock(xlock);
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
				activationKey = gawl.activate(xlock);
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
	public int activateActivationKey(Long id, String xlock, String activationKey) {
		SerialNumber snNumber = snRepo.findOne(id);
		int result = 0;

		try {
			if (!gawl.challenge(snNumber.getPassKey(), activationKey) && xlock.equals(snNumber.getXlock())) {
				result = -1;
			} else {
				snNumber.setActivationKey(activationKey);
				snNumber.setSerialNumberStatus(true);
				snRepo.save(snNumber);
				result = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public SerialNumber onlineActivation(SerialNumber serialNumber) {

		SerialNumber snNumber = null;
		SerialNumber sn = snRepo.findBySerialNumber(serialNumber.getSerialNumber());
		String passKey = "";

		if (sn != null) {
			if (gawl.validate(serialNumber.getSerialNumber())) {
				try {
					Map<String, Byte> extractResult = gawl.extract(serialNumber.getSerialNumber());
					if (extractResult.containsKey(Gawl.TYPE) && extractResult.containsKey(Gawl.MODULE)) {
						byte Type = extractResult.get(Gawl.TYPE);
						byte seed1 = extractResult.get(Gawl.SEED1);
						byte seed2 = extractResult.get(Gawl.SEED2);

						passKey = gawl.pass(seed1, seed2);
						String xlock = gawl.xlock(serialNumber.getSerialNumber());

						if (Type == 3) {
							if (serialNumber.getPassKey().compareTo(sn.getPassKey()) == 0
									&& serialNumber.getXlock().compareTo(sn.getXlock()) == 0) {
								snNumber = new SerialNumber();

								if (snNumber != null) {
									snNumber = generateActivationKey(sn.getId(), passKey, xlock);
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
}
