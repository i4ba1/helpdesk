package id.co.knt.helpdesk.api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "helpdesk_sn", uniqueConstraints = @UniqueConstraint(columnNames = "serial_number"))
public class License implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 7230259872048745134L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "serial_number")
	private String license;

	@Column(name = "passkey")
	private String passKey;

	@Column(name = "activation_key")
	private String activationKey;

	@Column(name = "created_date")
	private Long createdDate;

	@Column(name = "xlock")
	private String xlock;

	@Column(name = "mac_addr")
	private byte[] macAddr;

	@Column(name = "number_of_license")
	private Integer numberOfClient = 0;

	@Column(name = "licenseStatus")
	private boolean licenseStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "school_id", nullable = true)
	private School school;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = true)
	private Product product;

	public Long getId() {
		return id;
	}

	public String getPassKey() {
		return passKey;
	}

	public void setPassKey(String passKey) {
		this.passKey = passKey;
	}

	public String getActivationKey() {
		return activationKey;
	}

	public void setActivationKey(String activationKey) {
		this.activationKey = activationKey;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public void setXlock(String xlock) {
		this.xlock = xlock;
	}

	public String getXlock() {
		return this.xlock;
	}

	public byte[] getMacAddr() {
		return macAddr;
	}

	public void setMacAddr(byte[] macAddr) {
		this.macAddr = macAddr;
	}

	public boolean isLicenseStatus() {
		return licenseStatus;
	}

	public void setLicenseStatus(boolean serialNumberStatus) {
		this.licenseStatus = serialNumberStatus;
	}

	public Integer getNumberOfClient() {
		return numberOfClient;
	}

	public void setNumberOfClient(Integer numberOfClient) {
		this.numberOfClient = numberOfClient;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
