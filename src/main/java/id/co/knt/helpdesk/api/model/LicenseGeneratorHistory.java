/**
 * 
 */
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

/**
 * @author MNI
 *
 */
@Entity
@Table(name="helpdesk_license_generator_history")
public class LicenseGeneratorHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5245897062416509792L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="String")
	private String message;
	
	@Column(name="createdDate")
	private Long createdDate;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="license_id")
	private License license;
	
	@Column(name="is_read")
	private Boolean isRead;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Long getId() {
		return id;
	}

	public License getLicense() {
		return license;
	}

	public void setLicense(License license) {
		this.license = license;
	}
	
	public Boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}
}
