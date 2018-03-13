package id.co.knt.helpdesk.api.model;

import javax.persistence.*;
import java.io.Serializable;

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

	@Column(name="activation_limit", columnDefinition = "smallint")
	private Short activationLimit = 3;

	@Column(name="number_of_activation", columnDefinition = "smallint")
	private Short numberOfActivation = 0;

	@Column(name = "created_date")
	private Long createdDate;

	@Column(name = "xlock")
	private String xlock;

	@Column(name = "number_of_license")
	private Integer numberOfClient = null;

	@Column(name="school_name")
	private String schoolName;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id", nullable = true)
	private Product product;

	public Long getId() {
		return id;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
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

	public Short getActivationLimit() {
		return activationLimit;
	}

	public void setActivationLimit(Short activationLimit) {
		this.activationLimit = activationLimit;
	}

	public Short getNumberOfActivation() {
		return numberOfActivation;
	}

	public void setNumberOfActivation(Short numberOfActivation) {
		this.numberOfActivation = numberOfActivation;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public void setXlock(String xlock) {
		this.xlock = xlock;
	}

	public String getXlock() {
		return this.xlock;
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
