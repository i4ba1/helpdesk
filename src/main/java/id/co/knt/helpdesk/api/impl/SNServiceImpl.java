package id.co.knt.helpdesk.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.co.knt.helpdesk.api.model.License;
import id.co.knt.helpdesk.api.model.Product;
import id.co.knt.helpdesk.api.repositories.ProductRepo;
import id.co.knt.helpdesk.api.model.LicenseGeneratorHistory;
import id.co.knt.helpdesk.api.repositories.LicenseGeneratorHistoryRepo;
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
	private ProductRepo productRepo;

	private LicenseGeneratorHistory generatorHistory;

	@Autowired
	private LicenseGeneratorHistoryRepo historyRepo;

	@Override
	public License registerSerialNumber(License serialNumber) {
		License snNumber = null;
		License sn = snRepo.findByLicense(serialNumber.getLicense());
		Product product = null;
		
		if (sn == null) {
			if (gawl.validate(serialNumber.getLicense())) {
				try {
					Map<String, Byte> extractResult = gawl.extract(serialNumber.getLicense());
					if (extractResult.containsKey(Gawl.TYPE) && extractResult.containsKey(Gawl.MODULE)) {
						byte Type = extractResult.get(Gawl.TYPE);

						product = productRepo.findByProductCode(new Integer(Type));

						snNumber = new License();
						snNumber.setLicense(serialNumber.getLicense());
						snNumber.setPassKey(serialNumber.getPassKey());
						snNumber.setXlock(serialNumber.getXlock());
						snNumber.setMacAddr(serialNumber.getMacAddr());
						snNumber.setActivationKey("");
						snNumber.setCreatedDate(new Date().getTime());
						snNumber.setLicenseStatus(false);
						snNumber.setProduct(product);
						snNumber.setSchool(serialNumber.getSchool());
						snRepo.save(snNumber);
						snNumber = snRepo.save(snNumber);
					} else {
						return snNumber;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			return snNumber;
		}

		if (!snNumber.equals(null)) {
			generatorHistory = new LicenseGeneratorHistory();
			generatorHistory.setLicense(snNumber);
			generatorHistory
					.setMessage("One license for " + product.getProductName() + " has been created");
			generatorHistory.setIsRead(false);
			generatorHistory.setCreatedDate(new Date().getTime());
			historyRepo.save(generatorHistory);
		}

		return snNumber;
	}

	@Override
	public List<License> findAllSN() {
		List<License> serialNumbers = snRepo.findAll();
		return serialNumbers;
	}

	@Override
	public License findSN(Long id) {
		License sn = snRepo.findOne(id);
		return sn;
	}

	@Override
	public void deleteSN(Long id) {
		snRepo.delete(id);
	}

	@Override
	public License generateActivationKey(Long id, String passKey, String xlock) {
		String activationKey = "";
		License snNumber = snRepo.findOne(id);
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
	public License manuallyActivate(Long id, String xlock, String activationKey) {
		License snNumber = snRepo.findOne(id);

		try {
			activationKey = generateActivationKey(id, snNumber.getPassKey(), snNumber.getXlock()).getActivationKey();
			snNumber.setActivationKey(activationKey);
			snNumber.setLicenseStatus(true);
			snNumber = snRepo.saveAndFlush(snNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return snNumber;
	}

	@Override
	public License onlineActivation(License serialNumber) {

		License snNumber = null;
		License sn = snRepo.findByLicense(serialNumber.getLicense());

		if (sn != null) {
			if (gawl.validate(serialNumber.getLicense())) {
				try {
					Map<String, Byte> extractResult = gawl.extract(serialNumber.getLicense());
					if (extractResult.containsKey(Gawl.TYPE) && extractResult.containsKey(Gawl.MODULE)) {
						byte Type = extractResult.get(Gawl.TYPE);

						if (Type == 3) {
							if (serialNumber.getPassKey().compareTo(sn.getPassKey()) == 0
									&& serialNumber.getXlock().compareTo(sn.getXlock()) == 0) {
								snNumber = new License();

								if (snNumber != null) {
									snNumber = generateActivationKey(sn.getId(), serialNumber.getPassKey(),
											serialNumber.getXlock());
									snNumber.setActivationKey(snNumber.getActivationKey());
									snNumber.setLicenseStatus(true);
									snNumber = snRepo.saveAndFlush(snNumber);

									if (snNumber != null) {
										// ActivationHistory history = new ActivationHistory(new Date(), snNumber);
										// historyRepo.save(history);
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
	public List<License> findSNNeedActivated(List<License> serialNumbers) {
		List<License> list = new ArrayList<>();
		serialNumbers.forEach((sn) -> {
			License number = snRepo.findOne(sn.getId());
			if (!sn.getMacAddr().equals(number.getMacAddr())) {
				number.setActivationKey("");
				number.setLicenseStatus(false);
				number = snRepo.saveAndFlush(number);
				list.add(number);
			}
		});

		return list;
	}

	@Override
	public License findBySerial(String serial) {
		License number = snRepo.findByLicense(serial);
		return number;
	}

	@Override
	public List<License> serialNumberGenerator(Integer productId, Integer licenseCount, Integer secondParam) {
		Product product = productRepo.findOne(productId);
		List<License> list = new ArrayList<>();
		for (int i = 0; i < licenseCount; i++) {
			try {
				String generatedSn = gawl.generate(product.getProductCode(), secondParam);
				Map<String, Byte> extractResult = gawl.extract(generatedSn);

				byte seed1 = extractResult.get(Gawl.SEED1);
				byte seed2 = extractResult.get(Gawl.SEED2);

				License newLicense = new License();
				newLicense.setLicense(generatedSn);
				newLicense.setCreatedDate(System.currentTimeMillis());
				newLicense.setProduct(product);
				newLicense.setPassKey(gawl.pass(seed1, seed2));
				newLicense.setXlock(gawl.xlock(generatedSn));

				list.add(newLicense);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return list;
	}

	@Override
	public List<LicenseGeneratorHistory> findUnreadLicense() {
		List<LicenseGeneratorHistory> generatorHistories = historyRepo.fetchUnreadLicenseGenerator();
		
		return generatorHistories;
	}

	@Override
	public List<Object> findSnCountByProduct() {
		List<Object> list=snRepo.findSnCountByProduct();
		if(list.size()>0){
			return list;
		}
		return null;
	}

}
