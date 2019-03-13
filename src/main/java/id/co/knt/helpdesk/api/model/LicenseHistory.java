/**
 * 
 */
package id.co.knt.helpdesk.api.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author MNI
 *
 */
@Entity
@Table(name = "helpdesk_license_history")
public class LicenseHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5245897062416509792L;

	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "license_history_generator")
	@SequenceGenerator(name="license_history_generator", sequenceName = "license_history_seq", allocationSize = 1)
	//@Column(name="id", updatable = false, nullable = false)
	private Long id;

	@Column(name = "message")
	private String message;

	@Column(name = "createdDate")
	private Long createdDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "license_id")
	private License license;

	@Column(name = "file_name", nullable = true)
	private String fileName;

	@Column(name = "file_content_type", nullable = true)
	private String fileContentType;

	@Column(name = "file_data", nullable = true)
	private byte[] fileData;

	/**
	 * 0 is generated, 1 is registered, 2 is activated, 3 is warning/problem, 4 is
	 * disabled
	 */
	@Column(name = "license_status", columnDefinition = "smallint")
	private Short licenseStatus;

	@Column(name = "is_read")
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

	public Short getLicenseStatus() {
		return licenseStatus;
	}

	public void setLicenseStatus(Short licenseStatus) {
		this.licenseStatus = licenseStatus;
	}

	public Boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

}
