package id.co.knt.helpdesk.api.impl;

import java.util.*;

import id.co.knt.helpdesk.api.model.SubProduct;
import id.co.knt.helpdesk.api.model.dto.LicenseGeneratorDTO;
import id.co.knt.helpdesk.api.model.dto.ListLicenseDTO;
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
    private SubProductRepo subProductRepo;

    @Autowired
    private LicenseHistoryRepo licenseHistoryRepo;

    private short status = 0;
    private String message = "";

    private enum productTypeChoice {
        EL,
        EP
    }

    @Override
    public int registerSerialNumber(License serialNumber, int state) {
        License snNumber = null;
        License sn = snRepo.findByLicense(serialNumber.getLicense());
        Product product = null;

        if (gawl.validate(serialNumber.getLicense())) {
            try {
                Map<String, Byte> extractResult = gawl.extract(serialNumber.getLicense());
                byte Type = extractResult.get(Gawl.TYPE);

                product = productRepo.findByProductCode(new Integer(Type));
                if (state == 0)
                    message = "One license: " + serialNumber.getLicense() + "for " + product.getProductName() + " has been generated";
                else
                    message = "One license: " + serialNumber.getLicense() + "for " + product.getProductName() + " has been registered";
                status = (short) state;

                if (sn == null) {
                    snNumber = new License();
                    snNumber.setLicense(serialNumber.getLicense());
                    snNumber.setPassKey(serialNumber.getPassKey());
                    snNumber.setActivationKey("");
                    snNumber.setNumberOfClient(serialNumber.getNumberOfClient());
                    snNumber.setProduct(product);
                    snNumber.setSchoolName(serialNumber.getSchoolName());
                    snRepo.save(snNumber);
                    snNumber = snRepo.save(snNumber);
                    setLicenseHistory(snNumber, status, message);
                } else {
                    sn.setPassKey(serialNumber.getPassKey());
                    sn.setCreatedDate(new Date().getTime());
                    sn.setPassKey(serialNumber.getPassKey());
                    snRepo.saveAndFlush(sn);

                    setLicenseHistory(sn, status, message);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return 2;
            }

        } else {
            return 2;
        }

        return 0;
    }

    @Override
    public List<ListLicenseDTO> findAllSN() {
        List<License> serialNumbers = snRepo.findAllLicense();
        List<ListLicenseDTO> dtoList = new ArrayList<>();
        ListLicenseDTO listLicenseDTO;

        for (License sn : serialNumbers) {
            listLicenseDTO = new ListLicenseDTO();
            listLicenseDTO.setId(sn.getId());
            listLicenseDTO.setLicense(sn.getLicense());
            if (sn.getProduct().getSubModuleType().equals(productTypeChoice.EP.name())) {
                try {
                    Map<String, Byte> extractResult = gawl.extract(sn.getLicense());
                    byte module = extractResult.get(Gawl.MODULE);
                    listLicenseDTO.setProductName(sn.getProduct().getProductName() + "- kelas" + new Integer(module));
                    ;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                listLicenseDTO.setProductName(sn.getProduct().getProductName());
            }

            listLicenseDTO.setCreatedDate(sn.getCreatedDate());
            listLicenseDTO.setNumberOfClient(sn.getNumberOfClient());
            listLicenseDTO.setSchoolName(sn.getSchoolName());
            dtoList.add(listLicenseDTO);
        }

        return dtoList;
    }

    @Override
    public License findSN(Long id) {
        License sn = snRepo.findLicenseById(id);
        return sn;
    }

    @Override
    public License generateActivationKey(Long id, String passKey) {
        String activationKey = "";
        License snNumber = snRepo.findOne(id);
        try {
            activationKey = gawl.activate(passKey);
            snNumber.setActivationKey(activationKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return snNumber;
    }

    @Override
    public License manuallyActivate(Long licenseId, String passkey, String reason) {
        License snNumber = snRepo.findOne(licenseId);

        try {
            String activationKey = generateActivationKey(licenseId, passkey).getActivationKey();
            snNumber.setPassKey(passkey);
            snNumber.setActivationKey(activationKey);
            snNumber.setNumberOfActivation((short) (snNumber.getNumberOfActivation() + 1));
            snNumber = snRepo.saveAndFlush(snNumber);

            status = 2;
            setLicenseHistory(snNumber, status, reason);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return snNumber;
    }

    @Override
    public License onlineActivation(License serialNumber) {
        License sn = snRepo.findByLicense(serialNumber.getLicense());

        if (sn != null) {
            if (gawl.validate(serialNumber.getLicense())) {
                try {
                    Map<String, Byte> extractResult = gawl.extract(serialNumber.getLicense());
                    if (extractResult.containsKey(Gawl.TYPE) && extractResult.containsKey(Gawl.MODULE)) {
                        String activationKey = gawl.activate(serialNumber.getPassKey());
                        sn.setPassKey(serialNumber.getPassKey());
                        sn.setActivationKey(activationKey);
                        sn.setNumberOfActivation((short) (sn.getNumberOfActivation() + 1));
                        sn = snRepo.saveAndFlush(sn);
                        String message = "The license " + sn.getLicense() + " has been activated";
                        status = 2;
                        setLicenseHistory(sn, status, message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return sn;
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

		/* For Direct Entry */
        if (product.getSubModuleType().equals("EL")) {
            for (int i = 0; i < licenseGeneratorDTO.getLicenseCount(); i++) {
                try {
                    generatedSn = gawl.generate(product.getProductCode(),
                            licenseGeneratorDTO.getSubProducts().get(0).getValue());

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
            // int lengthData = licenseGeneratorDTO.getLicenseCount() *
            // licenseGeneratorDTO.getSubProducts().size();
            for (int i = 0; i < licenseGeneratorDTO.getLicenseCount(); i++) {
                try {
                    for (SubProduct sp : licenseGeneratorDTO.getSubProducts()) {
                        generatedSn = gawl.generate(product.getProductCode(), sp.getValue());

                        License newLicense = new License();
                        newLicense.setLicense(generatedSn);
                        newLicense.setNumberOfClient(null);
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
     *
     * @param licenseId
     * @return
     */
    @Override
    public Map<String, Object> viewDetailLicense(Long licenseId) {
        License license = snRepo.findLicenseById(licenseId);
        List<LicenseHistory> histories = licenseHistoryRepo.findLicenseHistory(licenseId);
        Object obj = licenseHistoryRepo.findLicenseHistoryByLicenseStatus(licenseId);

        Map<String, Object> map = new HashMap<>();
        map.put("licenseKey", license.getLicense());
        map.put("schoolName", license.getSchoolName());
        map.put("productName", license.getProduct().getProductName());
        map.put("numberOfClient", license.getNumberOfClient());
        map.put("numberOfActivation", license.getNumberOfActivation());
        map.put("activationLimit", license.getActivationLimit());
        map.put("activationKey", license.getActivationKey());
        map.put("createdDate", license.getCreatedDate());
        map.put("licenseHistory", histories);
        map.put("licenseStatus", obj);

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

    @Override
    public void setLicenseHistory(License license, short status, String message) {
        if (license != null) {
            licenseHistory = new LicenseHistory();
            licenseHistory.setLicense(license);
            licenseHistory.setLicenseStatus(status);
            licenseHistory.setMessage(message);
            licenseHistory.setIsRead(false);
            licenseHistory.setCreatedDate(new Date().getTime());
            licenseHistoryRepo.save(licenseHistory);
        }
    }

    @Override
    public LicenseHistory fetchLicenseHistory(Long licenseId) {
        List<LicenseHistory> licenseHistories = licenseHistoryRepo.findLicenseHistory(licenseId);

        return licenseHistories.get(0);
    }
}
