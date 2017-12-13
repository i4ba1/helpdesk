package id.co.knt.helpdesk.api.model.dto;

import id.co.knt.helpdesk.api.model.License;

public class LicenseDTO {
    License license;

    int flag;

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
