package id.co.knt.helpdesk.api.impl;

import id.co.knt.helpdesk.api.model.License;
import id.co.knt.helpdesk.api.model.LicenseHistory;
import id.co.knt.helpdesk.api.model.Product;
import id.co.knt.helpdesk.api.model.SubProduct;
import id.co.knt.helpdesk.api.model.dto.LicenseGeneratorDTO;
import id.co.knt.helpdesk.api.model.dto.ListLicenseDTO;
import id.co.knt.helpdesk.api.repositories.LicenseHistoryRepo;
import id.co.knt.helpdesk.api.repositories.ProductRepo;
import id.co.knt.helpdesk.api.repositories.SNRepo;
import id.co.knt.helpdesk.api.service.SNService;
import id.co.knt.helpdesk.api.utilities.LoggingError;
import id.web.pos.integra.gawl.Gawl;
import id.web.pos.integra.gawl.Gawl.UnknownCharacterException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.IntStream;

@Service("snServiceImpl")
public class SNServiceImpl implements SNService {

    Logger LOG = LoggerFactory.getLogger(SNServiceImpl.class);
    private static final Integer SIZE_OF_PAGE = 20;

    private Gawl gawl = new Gawl();

    @Autowired
    private SNRepo snRepo;

    @Autowired
    private ProductRepo productRepo;

    private LicenseHistory licenseHistory;

    @Autowired
    private LicenseHistoryRepo licenseHistoryRepo;

    private short status = 0;
    private String message = "";

    private enum productTypeChoice {
        EL, EP
    }

    enum FilterSearch {
        SN, DATE, SCHOOL
    }

    @Override
    public ListLicenseDTO saveGeneratedSN(License serialNumber) {
        License snNumber = null;
        License sn = snRepo.findByLicense(serialNumber.getLicense());
        Product product = null;
        ListLicenseDTO listLicenseDTO = null;
        List<License> licenses = new ArrayList<>();

        if (gawl.validate(serialNumber.getLicense().toLowerCase())) {
            try {
                Map<String, Byte> extractResult = gawl.extract(serialNumber.getLicense());
                byte Type = extractResult.get(Gawl.TYPE);

                product = productRepo.findByProductCode(new Integer(Type));
                message = "One license: " + serialNumber.getLicense() + "for " + product.getProductName()
                        + " has been generated";
                status = 0;

                if (sn == null) {
                    snNumber = saveLicenseData(serialNumber, product);
                    setLicenseHistory(snNumber, status, message, null);
                    sn = snRepo.findByLicense(serialNumber.getLicense());
                }

                licenses.add(sn);
                listLicenseDTO = generateListLicenseDTO(sn, status);
            } catch (Exception e) {
                LoggingError.writeError(ExceptionUtils.getStackTrace(e));
            }

        }

        return listLicenseDTO;
    }

    private ListLicenseDTO generateListLicenseDTO(License sn, Short licenseStatus) {
        ListLicenseDTO listLicenseDTO = getListLicenseDTO(sn, licenseStatus);

        return listLicenseDTO;
    }

    private License saveLicenseData(License serialNumber, Product product) {
        License snNumber = new License();
        snNumber.setLicense(serialNumber.getLicense().toLowerCase());
        snNumber.setPassKey(serialNumber.getPassKey());
        snNumber.setCreatedDate(new Date().getTime());
        snNumber.setActivationKey(serialNumber.getActivationKey());
        snNumber.setNumberOfClient(serialNumber.getNumberOfClient());
        snNumber.setProduct(product);
        snNumber.setSchoolName(serialNumber.getSchoolName());
        return snRepo.save(snNumber);
    }

    @Override
    public int registerSN(License serialNumber) {
        Product product = null;
        License license = snRepo.findByLicense(serialNumber.getLicense());
        Short licenseStatus = 0;

        if (license != null) {
            if (licenseHistoryRepo.findLicenseHistoryByLicenseStatus(license.getId()) != null) {
                licenseStatus = licenseHistoryRepo.findLicenseHistoryByLicenseStatus(license.getId());
            }
        }

        String strSN = serialNumber.getLicense().toLowerCase();
        if (gawl.validate(strSN) && licenseStatus < 4) {
            if (licenseStatus < 4)
                try {
                    Map<String, Byte> extractResult = gawl.extract(strSN);
                    byte Type = extractResult.get(Gawl.TYPE);

                    product = productRepo.findByProductCode(new Integer(Type));
                    message = "One license: " + strSN + "for " + product.getProductName() + " has been registered";
                    status = 1;
                    String passkey = gawl.pass(extractResult.get(Gawl.SEED1), extractResult.get(Gawl.SEED2));

                    if (license == null) {
                        license = saveLicenseData(serialNumber, product);
                    } else {
                        if (license.getPassKey() == null) {
                            license.setPassKey(serialNumber.getPassKey());
                            license.setCreatedDate(new Date().getTime());
                        } else if (serialNumber.getPassKey().compareTo(license.getPassKey()) == 0) {
                            license.setCreatedDate(new Date().getTime());
                        }

                        snRepo.saveAndFlush(license);
                    }

                    setLicenseHistory(license, status, message, null);

                } catch (Exception e) {
                    LoggingError.writeError(ExceptionUtils.getStackTrace(e));
                    return 2;
                }

        } else {
            return 2;
        }

        return 0;
    }

    /**
     * @param serialNumber
     * @return
     */
    @Override
    public Map<String, Object> registerInHelpdesk(License serialNumber) {
        Product product = null;
        String strSN = serialNumber.getLicense().toLowerCase();
        License license = snRepo.findByLicense(strSN);
        Map<String, Object> map = new HashMap<>();
        Short licenseStatus = 0;

        if (license != null) {
            if (licenseHistoryRepo.findLicenseHistoryByLicenseStatus(license.getId()) != null) {
                licenseStatus = licenseHistoryRepo.findLicenseHistoryByLicenseStatus(license.getId());
            }
        }

        if (gawl.validate(strSN) && licenseStatus < 4) {
            if (licenseStatus < 4) {
                Map<String, Byte> extractResult;
                try {
                    extractResult = gawl.extract(strSN);
                    byte Type = extractResult.get(Gawl.TYPE);
                    product = productRepo.findByProductCode(new Integer(Type));
                } catch (UnknownCharacterException e1) {
                    map.put("error", 2);
                    map.put("license", license);
                    LoggingError.writeError(ExceptionUtils.getStackTrace(e1));
                    e1.printStackTrace();

                    return map;
                }

                message = "One license: " + strSN + "for " + product.getProductName() + " has been registered";
                status = 1;
                setLicenseHistory(license, status, message, null);

                String activationKey = "";
                try {
                    activationKey = gawl.activate(serialNumber.getPassKey());
                    map.put("error", 1);
                } catch (UnknownCharacterException | ArrayIndexOutOfBoundsException e) {
                    LoggingError.writeError(ExceptionUtils.getStackTrace(e));
                    map.put("error", 3);
                    map.put("license", license);

                    return map;
                }

                if (license == null) {
                    serialNumber.setActivationKey(activationKey);
                    serialNumber.setNumberOfActivation((short) (serialNumber.getNumberOfActivation() + 1));
                    license = saveLicenseData(serialNumber, product);
                    map.put("license", license);
                } else {
                    license.setNumberOfActivation((short) (license.getNumberOfActivation() + 1));
                    license.setActivationKey(activationKey);
                    license.setPassKey(serialNumber.getPassKey());
                    license.setSchoolName(serialNumber.getSchoolName());
                    license.setCreatedDate(new Date().getTime());
                    snRepo.saveAndFlush(license);
                    map.put("license", license);
                }

                message = "One license: " + strSN + "for " + product.getProductName() + " has been activated";
                status = 2;
                setLicenseHistory(license, status, message, null);
            }

        } else {
            map.put("error", 2);
            map.put("license", license);
        }

        return map;
    }

    private Pageable gotoPage(int page) {
        return new PageRequest(page, SIZE_OF_PAGE);
    }

    @Override
    public Map<String, Object> findAllSN(String category, int page, String searchText, Long startDate, Long endDate) {
        List<ListLicenseDTO> dtoList = new ArrayList<>();
        int totalRow;

        FilterSearch filter;
        filter = FilterSearch.valueOf(category);

        switch (filter) {
            case SN:
                totalRow = snRepo.countByLicenseLikeOrSchoolNameLikeAllIgnoreCaseOrderByIdDesc("%" + searchText + "%", "");
                dtoList = getListLicenseDTo(
                        snRepo.findByLicenseLikeOrSchoolNameLikeAllIgnoreCaseOrderByIdDesc(gotoPage(page), "%" + searchText + "%", ""), null);
                break;
            case SCHOOL:
                totalRow = snRepo.countByLicenseLikeOrSchoolNameLikeAllIgnoreCaseOrderByIdDesc("", "%" + searchText + "%");
                dtoList = getListLicenseDTo(
                        snRepo.findByLicenseLikeOrSchoolNameLikeAllIgnoreCaseOrderByIdDesc(gotoPage(page), "", "%" + searchText + "%"), null);
                break;
            case DATE:
                totalRow = snRepo.countByCreatedDateGreaterThanEqualAndCreatedDateLessThanEqualOrderByIdDesc(startDate, endDate);
                dtoList = getListLicenseDTo(snRepo.findLicenseByCreatedDateGreaterThanEqualAndCreatedDateLessThanEqualOrderByIdDesc(
                        gotoPage(page), startDate, endDate), null);
                break;
            default:
                totalRow = snRepo.countLicense();
                dtoList = getListLicenseDTo(snRepo.fetchLicenses(gotoPage(page)), null);
                break;
        }

        return licenseDTOResult(dtoList, page, totalRow);
    }

    private List<ListLicenseDTO> getListLicenseDTo(List<License> licenses, Short licenseStatus){
        List<ListLicenseDTO> list = new ArrayList<>();
        IntStream.range(0, licenses.size()).forEach(
                index->{
                        License sn = licenses.get(index);
                        list.add(getListLicenseDTO(sn, licenseStatus));
                    }
                );

        return list;
    }

    private ListLicenseDTO getListLicenseDTO(License sn, Short licenseStatus){
        ListLicenseDTO listLicenseDTO = new ListLicenseDTO();
        listLicenseDTO.setId(sn.getId());
        listLicenseDTO.setLicense(sn.getLicense().toLowerCase());
        listLicenseDTO.setProductName(modifyProductName(sn));
        listLicenseDTO.setCreatedDate(sn.getCreatedDate());
        listLicenseDTO.setNumberOfClient(sn.getNumberOfClient());
        listLicenseDTO.setSchoolName(sn.getSchoolName());
        listLicenseDTO.setLicenseStatus(licenseStatus);

        return  listLicenseDTO;
    }

    private String modifyProductName(License sn) {
        byte module = 0;
        if (sn.getProduct().getSubModuleType().equals(productTypeChoice.EP.name())) {
            try {
                Map<String, Byte> extractResult = gawl.extract(sn.getLicense());
                module = extractResult.get(Gawl.MODULE);
            } catch (Exception e) {
                LoggingError.writeError(ExceptionUtils.getStackTrace(e));
            }

            return sn.getProduct().getProductName() + "- kelas" + new Integer(module);
        } else {
            return sn.getProduct().getProductName();
        }
    }

    // List<Map<String, Object>
    private Map<String, Object> licenseDTOResult(List<ListLicenseDTO> dtoList, int page, int totalPage) {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> parentMap = new TreeMap<>();

        /*
         * int totalPage = dtoList.size()/SIZE_OF_PAGE; totalPage = ((totalPage % 20) >
         * 1? totalPage+1:totalPage);
         */

        dtoList.stream().forEachOrdered(data -> {
            Map<String, Object> objectMap = new TreeMap<>();
            objectMap.put("serialNumber", data);
            objectMap.put("status", (int) fetchLicenseHistory(data.getId()).getLicenseStatus());
            result.add(objectMap);
        });

        parentMap.put("data", result);
        parentMap.put("totalPage", totalPage);
        parentMap.put("currentPage", page);
        /*
         * for (ListLicenseDTO data : dtoList) { objectMap = new TreeMap<>();
         * objectMap.put("serialNumber", data); objectMap.put("status", (int)
         * fetchLicenseHistory(data.getId()).getLicenseStatus()); result.add(objectMap);
         * }
         */

        return parentMap;
    }

    public Map<String, Object> generateLicenseDTOResult(ListLicenseDTO data) {
        Map<String, Object> objectMap = null;
        objectMap = new TreeMap<>();
        objectMap.put("serialNumber", data);
        objectMap.put("status", (int) data.getLicenseStatus());
        //objectMap.put("status", (int) fetchLicenseHistory(data.getId()).getLicenseStatus());

        return objectMap;
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
            setLicenseHistory(snNumber, status, reason, null);
        } catch (Exception e) {
            LoggingError.writeError(ExceptionUtils.getStackTrace(e));
        }

        return snNumber;
    }

    @Override
    public License onlineActivation(License serialNumber) {
        License sn = snRepo.findByLicense(serialNumber.getLicense());

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
                    setLicenseHistory(sn, status, message, null);
                }
            } catch (Exception e) {
                LoggingError.writeError(ExceptionUtils.getStackTrace(e));
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
                    newLicense.setLicense(generatedSn.toLowerCase());
                    newLicense.setNumberOfClient(licenseGeneratorDTO.getSubProducts().get(0).getValue());
                    newLicense.setCreatedDate(new Date().getTime());
                    newLicense.setProduct(product);

                    list.add(newLicense);
                } catch (Exception e) {
                    LoggingError.writeError(ExceptionUtils.getStackTrace(e));
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
                        newLicense.setLicense(generatedSn.toLowerCase());
                        newLicense.setNumberOfClient(null);
                        newLicense.setCreatedDate(new Date().getTime());
                        newLicense.setProduct(product);
                        list.add(newLicense);
                    }

                    sortedData.put("Paket" + (i + 1), list);
                    list = new ArrayList<>();
                } catch (Exception e) {
                    LoggingError.writeError(ExceptionUtils.getStackTrace(e));
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
        Short licenseStatus = licenseHistoryRepo.findLicenseHistoryByLicenseStatus(license.getId());
        ;

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
        map.put("licenseStatus", licenseStatus);

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
    public void setLicenseHistory(License license, short status, String message, MultipartFile file) {
        try {
            if (license != null) {
                licenseHistory = new LicenseHistory();
                licenseHistory.setLicense(license);
                licenseHistory.setLicenseStatus(status);
                licenseHistory.setMessage(message);
                licenseHistory.setIsRead(false);
                licenseHistory.setCreatedDate(new Date().getTime());
                if (file != null) {
                    licenseHistory.setFileName(file.getOriginalFilename());
                    licenseHistory.setFileContentType(file.getContentType());
                    licenseHistory.setFileData(file.getBytes());
                }
                licenseHistoryRepo.save(licenseHistory);
            }
        } catch (Exception e) {
            LoggingError.writeError(ExceptionUtils.getStackTrace(e));
        }

    }

    @Override
    public LicenseHistory fetchLicenseHistory(Long licenseId) {
        List<LicenseHistory> licenseHistories = licenseHistoryRepo.findLicenseHistory(licenseId);
        return licenseHistories.get(0);
    }
}
