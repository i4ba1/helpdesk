package id.co.knt.helpdesk.api.model.dto;


public class ListLicenseDTO {
    private Long id;
    private String license;
    private String productName;
    private Integer numberOfClient;
    private String schoolName;
    private Long createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getNumberOfClient() {
        return numberOfClient;
    }

    public void setNumberOfClient(Integer numberOfClient) {
        this.numberOfClient = numberOfClient;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }
}
