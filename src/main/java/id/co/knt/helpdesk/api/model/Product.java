package id.co.knt.helpdesk.api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "helpdesk_product")
public class Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "product_name")
	private String productName;

	@Column(name = "product_code")
	private Integer productCode;

	@Column(name = "created_date")
	private Long createdDate;

	@Column(name = "description")
	private String description;

	@Column(name = "sub_module_type")
	private String subModuleType;

	@Column(name = "sub_module_lable")
	private String subModuleLable;

	@Column(name = "deleted")
	private Boolean deleted;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getProductCode() {
		return productCode;
	}

	public void setProductCode(Integer productCode) {
		this.productCode = productCode;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getId() {
		return id;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSubModuleType() {
		return subModuleType;
	}

	public void setSubModuleType(String subModuleType) {
		this.subModuleType = subModuleType;
	}

	public String getSubModuleLable() {
		return subModuleLable;
	}

	public void setSubModuleLable(String subModuleLable) {
		this.subModuleLable = subModuleLable;
	}

}
