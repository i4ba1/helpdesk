package id.co.knt.helpdesk.api.impl;

import java.util.*;

import id.co.knt.helpdesk.api.model.SubProduct;
import id.co.knt.helpdesk.api.model.dto.LicenseGeneratorDTO;
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

    private byte status= 0;

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
                        snNumber.setLicenseStatus(status);
                        snNumber.setProduct(product);
                        snNumber.setSchoolName(serialNumber.getSchoolName());
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
        List<License> serialNumbers = snRepo.findAllLicense();
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
            byte b = 3;
            snNumber.setLicenseStatus(b);
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
                                    snNumber.setLicenseStatus(status);
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
                number.setLicenseStatus(status);
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

    /**
     *
     * @param licenseGeneratorDTO
     * @return
     */
    @Override
    public TreeMap<String, List<License>> serialNumberGenerator(LicenseGeneratorDTO licenseGeneratorDTO) {
        Product product = licenseGeneratorDTO.getProduct();
        List<License> list = new ArrayList<>();
        TreeMap<String, List<License>> sortedData = new TreeMap<>();
        String generatedSn = "";
        byte b = 0;

        /*For Direct Entry*/
        if (product.getSubModuleType().equals("EL")) {
            for (int i = 0; i < licenseGeneratorDTO.getLicenseCount(); i++) {
                try {
                    generatedSn = gawl.generate(product.getProductCode(), licenseGeneratorDTO.getSubProducts().get(0).getValue());

                    License newLicense = new License();
                    newLicense.setLicense(generatedSn);
                    newLicense.setNumberOfClient(licenseGeneratorDTO.getSubProducts().get(0).getValue());
                    newLicense.setCreatedDate(System.currentTimeMillis());
                    newLicense.setLicenseStatus(b);
                    newLicense.setProduct(product);

                    list.add(newLicense);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            sortedData.put("EL", list);
        }else{
            //int lengthData = licenseGeneratorDTO.getLicenseCount() * licenseGeneratorDTO.getSubProducts().size();
            for (int i = 0; i < licenseGeneratorDTO.getLicenseCount(); i++) {
                try{
                    for (SubProduct sp:licenseGeneratorDTO.getSubProducts()) {
                        generatedSn = gawl.generate(product.getProductCode(), sp.getValue());

                        License newLicense = new License();
                        newLicense.setLicense(generatedSn);
                        newLicense.setNumberOfClient(sp.getValue());
                        newLicense.setCreatedDate(System.currentTimeMillis());
                        newLicense.setLicenseStatus(b);
                        newLicense.setProduct(product);
                        list.add(newLicense);
                    }

                    sortedData.put("Paket"+(i+1), list);
                    list = new ArrayList<>();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        return sortedData;
    }

    @Override
    public List<LicenseGeneratorHistory> findUnreadLicense() {
        List<LicenseGeneratorHistory> generatorHistories = historyRepo.fetchUnreadLicenseGenerator();

        return generatorHistories;
    }

    @Override
    public LicenseGeneratorHistory findDetailHistory(Long id) {
        LicenseGeneratorHistory history = historyRepo.findOne(id);

        return history;
    }

    @Override
    public Map<String, Object> viewDetailLicense(Long licenseId) {
        License license = snRepo.findLicenseById(licenseId);
        Map<String, Object> map = new HashMap<>();
        map.put("licenseKey", license.getLicense());
        map.put("activationKey", license.getActivationKey());
        map.put("createdDate", license.getCreatedDate());
        map.put("licenseStatus", license.getLicenseStatus());
        map.put("schoolName", license.getSchoolName());
        map.put("productName", license.getProduct().getProductName());

        return map;
    }

    @Override
    public LicenseGeneratorHistory updateReadStatus(LicenseGeneratorHistory generatorHistory) {
        LicenseGeneratorHistory history = historyRepo.saveAndFlush(generatorHistory);

        return history;
    }

    @Override
    public List<Object> findSnCountByProduct() {
        List<Object> list = snRepo.findSnCountByProduct();
        if (list.size() > 0) {
            return list;
        }
        return null;
    }

}
