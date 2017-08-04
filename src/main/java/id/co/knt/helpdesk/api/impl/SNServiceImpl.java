package id.co.knt.helpdesk.api.impl;

import java.util.*;

import id.co.knt.helpdesk.api.model.SubProduct;
import id.co.knt.helpdesk.api.model.dto.LicenseGeneratorDTO;
import id.co.knt.helpdesk.api.repositories.SubProductRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.co.knt.helpdesk.api.model.License;
import id.co.knt.helpdesk.api.model.Product;
import id.co.knt.helpdesk.api.repositories.ProductRepo;
import id.co.knt.helpdesk.api.model.LicenseHistory;
import id.co.knt.helpdesk.api.repositories.LicenseHistoryRepo;
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

    private LicenseHistory licenseHistory;

    @Autowired
    private LicenseHistoryRepo licenseHistoryRepo;

    @Autowired
    private SubProductRepo subProductRepo;

    private short status = 0;

    @Override
    public License registerSerialNumber(License serialNumber, int state) {
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
                        snNumber.setActivationKey("");
                        snNumber.setNumberOfClient(serialNumber.getNumberOfClient());
                        snNumber.setCreatedDate(new Date().getTime());
                        snNumber.setProduct(product);
                        snNumber.setSchoolName(serialNumber.getSchoolName());
                        snRepo.save(snNumber);
                        snNumber = snRepo.save(snNumber);

                        status = (short) state;
                        setLicenseHistory(snNumber, status);
                    } else {
                        return null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            return sn;
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
        License sn = snRepo.findLicenseById(id);
        return sn;
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
            snNumber = snRepo.saveAndFlush(snNumber);

            status = 2;
            setLicenseHistory(snNumber, status);
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
                                snNumber = generateActivationKey(sn.getId(), serialNumber.getPassKey(),
                                        serialNumber.getXlock());
                                snNumber.setActivationKey(snNumber.getActivationKey());
                                snNumber = snRepo.saveAndFlush(snNumber);
                                status = 2;
                                setLicenseHistory(snNumber, status);
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
    public License findBySerial(String serial) {
        License number = snRepo.findByLicense(serial);
        return number;
    }

    /**
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
                    newLicense.setProduct(product);

                    list.add(newLicense);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            sortedData.put("EL", list);
        } else {
            //int lengthData = licenseGeneratorDTO.getLicenseCount() * licenseGeneratorDTO.getSubProducts().size();
            for (int i = 0; i < licenseGeneratorDTO.getLicenseCount(); i++) {
                try {
                    for (SubProduct sp : licenseGeneratorDTO.getSubProducts()) {
                        generatedSn = gawl.generate(product.getProductCode(), sp.getValue());

                        License newLicense = new License();
                        newLicense.setLicense(generatedSn);
                        newLicense.setNumberOfClient(sp.getValue());
                        newLicense.setCreatedDate(System.currentTimeMillis());
                        newLicense.setProduct(product);
                        list.add(newLicense);
                    }

                    sortedData.put("Paket" + (i + 1), list);
                    list = new ArrayList<>();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return sortedData;
    }

    @Override
    public List<LicenseHistory> findUnreadLicense() {
        List<LicenseHistory> licenseHistories = licenseHistoryRepo.fetchUnreadLicenseGenerator();

        return licenseHistories;
    }

    @Override
    public LicenseHistory findDetailHistory(Long id) {
        LicenseHistory history = licenseHistoryRepo.findOne(id);

        return history;
    }

    /**
     * View detail license
     * @param licenseId
     * @return
     */
    @Override
    public Map<String, Object> viewDetailLicense(Long licenseId) {
        License license = snRepo.findLicenseById(licenseId);
        List<LicenseHistory> histories = licenseHistoryRepo.findAlreadyReadLicence(licenseId);
        List<SubProduct> subProducts = subProductRepo.findAllSubProductByProductId(license.getProduct().getId());
        List<String> listClassName = new ArrayList<>();

        for (SubProduct sp:subProducts) {
            listClassName.add(sp.getLabel());
        }

        Map<String, Object> map = new HashMap<>();
        map.put("licenseKey", license.getLicense());
        map.put("schoolName", license.getSchoolName());
        map.put("productName", license.getProduct().getProductName());
        if(license.getProduct().getSubModuleType().equals("EL")){
            map.put("numberOfClient", license.getNumberOfClient());
        }else{
            map.put("listClassName", listClassName);
        }
        map.put("numberOfActivation", license.getNumberOfActivation());
        map.put("activationKey", license.getActivationKey());
        map.put("createdDate", license.getCreatedDate());
        map.put("licenseHistory", histories);

        return map;
    }


    @Override
    public LicenseHistory updateReadStatus(LicenseHistory generatorHistory) {
        LicenseHistory history = licenseHistoryRepo.saveAndFlush(generatorHistory);

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

    private void setLicenseHistory(License license, short status) {
        if (license != null) {
            licenseHistory = new LicenseHistory();
            licenseHistory.setLicense(license);
            licenseHistory.setLicenseStatus(status);
            licenseHistory
                    .setMessage("One license for " + license.getProduct().getProductName() + " has been ");
            licenseHistory.setIsRead(false);
            licenseHistory.setCreatedDate(new Date().getTime());
            licenseHistoryRepo.save(licenseHistory);
        }
    }
}
