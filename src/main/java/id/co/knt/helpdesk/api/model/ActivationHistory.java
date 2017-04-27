package id.co.knt.helpdesk.api.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="helpdesk_activation_history")
public class ActivationHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -89336745576474568L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "activation_date")
	private Date activationDate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="sn_id", referencedColumnName="id")
	private SerialNumber serialNumber;

	public ActivationHistory(Date activationDate, SerialNumber serialNumber) {
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

	public SerialNumber getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(SerialNumber serialNumber) {
		this.serialNumber = serialNumber;
	}
}
