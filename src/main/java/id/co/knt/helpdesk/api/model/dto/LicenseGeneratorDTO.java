package id.co.knt.helpdesk.api.model.dto;

import id.co.knt.helpdesk.api.model.Product;
import id.co.knt.helpdesk.api.model.SubProduct;

import java.util.List;

public class LicenseGeneratorDTO {
    private Product product;

    private List<SubProduct> subProducts;

    private Integer licenseCount;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<SubProduct> getSubProducts() {
        return subProducts;
    }

    public void setSubProducts(List<SubProduct> subProducts) {
        this.subProducts = subProducts;
    }

    public Integer getLicenseCount() {
        return licenseCount;
    }

    public void setLicenseCount(Integer licenseCount) {
        this.licenseCount = licenseCount;
    }
}
