package id.co.knt.helpdesk.api.model.dto;


public class ListLicenseDTO {
    private Long serialId;
    private String serial;
    private String productName;
    private Integer numberOfClient;
    private String schoolName;
    private Long createdDate;

    public Long getSerialId() {
        return serialId;
    }

    public void setSerialId(Long serialId) {
        this.serialId = serialId;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
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
