package id.co.knt.helpdesk.api.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="helpdesk_activation_history")
public class ActivationHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -89336745576474568L;
	
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activation_history_generator")
	@SequenceGenerator(name="activation_history_generator", sequenceName = "activation_history_seq", allocationSize = 1)
	@Column(name="id", updatable = false, nullable = false)
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "activation_date")
	private Date activationDate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="sn_id", referencedColumnName="id")
	private License serialNumber;

	public ActivationHistory(Date activationDate, License serialNumber) {
		super();
		this.activationDate = activationDate;
		this.serialNumber = serialNumber;
	}

	public Date getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}

	public Long getId() {
		return id;
	}

	public License getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(License serialNumber) {
		this.serialNumber = serialNumber;
	}
}
