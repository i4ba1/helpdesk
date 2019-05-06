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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.IntStream;

@Service("snServiceImpl")
public class SNServiceImpl implements SNService {

    private static final Integer SIZE_OF_PAGE = 20;
    Logger LOG = LoggerFactory.getLogger(SNServiceImpl.class);
    private Gawl gawl = new Gawl();

    private final SNRepo snRepo;

    private final ProductRepo productRepo;

    private final LicenseHistoryRepo licenseHistoryRepo;

    private short status = 0;
    private String message = "";
    private Set<License> list = null;
    private Set<String> generators = null;

    private StringWriter sw = null;
    private PrintWriter pw = null;

    @Autowired
    public SNServiceImpl(SNRepo snRepo, ProductRepo productRepo, LicenseHistoryRepo licenseHistoryRepo) {
        this.snRepo = snRepo;
        this.productRepo = productRepo;
        this.licenseHistoryRepo = licenseHistoryRepo;
    }

    @Override
    public List<ListLicenseDTO> saveLicenseEntities(Set<License> licenses) {
        List<License> result = snRepo.save(licenses);
        List<ListLicenseDTO> listLicenseDTOS = new ArrayList<>();
        byte Type = 0;

        License serialNumber = result.get(0);
        if (gawl.validate(serialNumber.getLicense().toLowerCase())) {
            Map<String, Byte> extractResult;
            try {
                extractResult = gawl.extract(serialNumber.getLicense());
                Type = extractResult.get(Gawl.TYPE);
            } catch (UnknownCharacterException e) {
                e.printStackTrace();
            }
        }

        Product product = productRepo.findByProductCode((int) Type);
        message = licenses.size() + "license for " + product.getProductName()
                + " has been generated";
        status = 0;
        setGeneratedLicenseHistory(result, (short) 0, message, null);
        listLicenseDTOS = generateListLicenseDTO(result, (short) 0);

        return listLicenseDTOS;
    }

    @Override
    public void emptyListOfLicense() {
        list.clear();
        generators.clear();
    }

    @Override
    public List<ListLicenseDTO> saveGeneratedSN(List<License> listSerialsNumber) {
        List<ListLicenseDTO> listLicenseDTO = null;
        try {
            List<License> results = snRepo.save(listSerialsNumber);
            String productName = results.get(0).getProduct().getProductName();
            for (int i = 0; i < results.size(); i++) {
                License snNumber = listSerialsNumber.get(i);
                String message = "One license: " + snNumber + "for " + productName
                        + " has been generated";
                setLicenseHistory(snNumber, status, message, null);
            }

            listLicenseDTO = generateListLicenseDTO(listSerialsNumber, status);
        } catch (Exception e) {
            LOG.error(LoggingError.stackTraceMessage(e).toString());
        }

        return listLicenseDTO;
    }

    private List<ListLicenseDTO> generateListLicenseDTO(List<License> licenseList, Short licenseStatus) {
        LOG.info("generateListLicenseDTO licenseStatus=====> " + licenseStatus);
        List<ListLicenseDTO> listLicenseDTOS = new ArrayList<>();

        for (int i = 0; i < licenseList.size(); i++) {
            ListLicenseDTO listLicenseDTO = new ListLicenseDTO();
            License sn = licenseList.get(i);
            listLicenseDTO.setId(sn.getId());
            listLicenseDTO.setLicense(sn.getLicense().toLowerCase());
            listLicenseDTO.setProductName(modifyProductName(sn));
            listLicenseDTO.setCreatedDate(sn.getCreatedDate());
            listLicenseDTO.setNumberOfClient(sn.getNumberOfClient());
            listLicenseDTO.setSchoolName(sn.getSchoolName());
            listLicenseDTO.setLicenseStatus(licenseStatus);
            listLicenseDTOS.add(listLicenseDTO);
        }

        return listLicenseDTOS;
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
        snNumber = snRepo.saveAndFlush(snNumber);
        LOG.info("saveLicenseData =====> " + snNumber.getId());
        LOG.info("Id=======> " + snNumber.getId());
        return snNumber;
    }

    @Override
    public int registerSN(License serialNumber) {
        Product product = null;
        License license = snRepo.findByLicense(serialNumber.getLicense());
        Short licenseStatus = 0;

        licenseStatus = isLicenseNull(license, licenseHistoryRepo);
        String strSN = serialNumber.getLicense().toLowerCase();
        if (gawl.validate(strSN) && licenseStatus < 4) {
            //if (licenseStatus < 4)
            try {
                Map<String, Byte> extractResult = gawl.extract(strSN);
                byte Type = extractResult.get(Gawl.TYPE);

                product = productRepo.findByProductCode(new Integer(Type));
                message = "One license: " + strSN + "for " + product.getProductName() + " has been registered";
                status = 1;

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

        licenseStatus = isLicenseNull(license, licenseHistoryRepo);
        if (gawl.validate(strSN) && licenseStatus < 4) {
            //if (licenseStatus < 4) {
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
            //}

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

    private List<ListLicenseDTO> getListLicenseDTo(List<License> licenses, Short licenseStatus) {
        List<ListLicenseDTO> list = new ArrayList<>();
        LOG.info("Size licenses ====> " + licenses.isEmpty() + " - " + licenses.size());
        if (!licenses.isEmpty()) {
            list = generateListLicenseDTO(licenses, licenseStatus);
        }

        return list;
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
        IntStream.range(0, dtoList.size()).forEach(
                i -> {
                    Map<String, Object> objectMap = new TreeMap<>();
                    ListLicenseDTO data = dtoList.get(i);
                    objectMap.put("serialNumber", data);

                    LicenseHistory licenseHistory = fetchLicenseHistory(data.getId());
                    objectMap.put("status", licenseHistory == null ? 0 : (int) licenseHistory.getLicenseStatus());

                    result.add(objectMap);
                }
        );
        /*dtoList.stream().forEachOrdered(data -> {
            Map<String, Object> objectMap = new TreeMap<>();
            objectMap.put("serialNumber", data);
            objectMap.put("status", (int) fetchLicenseHistory(data.getId()).getLicenseStatus());
            result.add(objectMap);
        });*/

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
            LoggingError.writeError(ExceptionUtils.getStackTrace(e));
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
    public TreeMap<String, Object> serialNumberGenerator(LicenseGeneratorDTO licenseGeneratorDTO) {

        TreeMap<String, Object> sortedData = new TreeMap<>();
        try {
            Product product = licenseGeneratorDTO.getProduct();
            String generatedSn = "";
            list = new LinkedHashSet<>();
            generators = new LinkedHashSet<>();

            /* For Direct Entry */
            if (product.getSubModuleType().equals("EL")) {
                for (int i = 0; i < licenseGeneratorDTO.getLicenseCount(); i++) {
                    try {
                        generatedSn = gawl.generate(product.getProductCode(), licenseGeneratorDTO.getSubProducts().get(0).getValue());

                        if (snRepo.findByLicense(generatedSn) != null) {
                            generatedSn = gawl.generate(product.getProductCode(), licenseGeneratorDTO.getSubProducts().get(0).getValue());
                        }

                        i = getI(licenseGeneratorDTO, product, generatedSn, i);
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
                            if (snRepo.findByLicense(generatedSn) != null) {
                                generatedSn = gawl.generate(product.getProductCode(), sp.getValue());
                            }
                            i = getI(licenseGeneratorDTO, product, generatedSn, i);
                        }


                        sortedData.put("Paket" + (i + 1), list);
                        list = new LinkedHashSet<>();
                    } catch (Exception e) {
                        LoggingError.writeError(ExceptionUtils.getStackTrace(e));
                    }
                }
            }
            sortedData.put("product", product);
        }catch (Exception e){
            LOG.error(LoggingError.stackTraceMessage(e).toString());
        }

        return sortedData;
    }

    private int getI(LicenseGeneratorDTO licenseGeneratorDTO, Product product, String generatedSn, int i) {
        if (!generators.contains(generatedSn)) {
            License newLicense = addLicenseToGenerators(generatedSn, licenseGeneratorDTO, product);
            list.add(newLicense);
            generators.add(generatedSn);
        } else {
            i--;
        }
        return i;
    }

    private License addLicenseToGenerators(String generatedSn, LicenseGeneratorDTO licenseGeneratorDTO, Product product) {
        License newLicense = new License();
        newLicense.setLicense(generatedSn.toLowerCase());
        newLicense.setNumberOfClient(licenseGeneratorDTO.getSubProducts().get(0).getValue());
        newLicense.setCreatedDate(new Date().getTime());
        newLicense.setProduct(product);
        return newLicense;
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

    private void setGeneratedLicenseHistory(List<License> licenses, short status, String message, MultipartFile file) {
        try {
            IntStream.range(0, licenses.size()).forEach(
                    i -> {
                        newLicenseHistory(licenses.get(i), status, message, file);
                    }
            );
        } catch (Exception e) {
            LoggingError.writeError(ExceptionUtils.getStackTrace(e));
        }
    }

    private void newLicenseHistory(License license, short status, String message, MultipartFile file) {
        try {
            LicenseHistory licenseHistory = new LicenseHistory();
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
            licenseHistoryRepo.saveAndFlush(licenseHistory);
            LOG.info("newLicenseHistory=======> " + licenseHistory.getId());
        } catch (Exception e) {
            e.getStackTrace();
            LoggingError.writeError(ExceptionUtils.getStackTrace(e));
        }
    }

    @Override
    public void setLicenseHistory(License license, short status, String message, MultipartFile file) {
        newLicenseHistory(license, status, message, file);
    }

    @Override
    public LicenseHistory fetchLicenseHistory(Long licenseId) {
        List<LicenseHistory> licenseHistories = licenseHistoryRepo.findLicenseHistory(licenseId);
        if (!licenseHistories.isEmpty()) {
            return licenseHistories.get(0);
        }
        return null;
    }

    private Short isLicenseNull(License license, LicenseHistoryRepo licenseHistoryRepo) {
        Short licenseStatus = 0;
        if (license != null) {
            if (licenseHistoryRepo.findLicenseHistoryByLicenseStatus(license.getId()) != null) {
                licenseStatus = licenseHistoryRepo.findLicenseHistoryByLicenseStatus(license.getId());
            }
        }

        return licenseStatus;
    }

    private enum productTypeChoice {
        EL, EP
    }

    enum FilterSearch {
        SN, DATE, SCHOOL
    }
}
