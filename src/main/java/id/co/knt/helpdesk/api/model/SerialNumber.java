package id.co.knt.helpdesk.api.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="helpdesk_sn")
public class SerialNumber implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7230259872048745134L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name="serial_number")
    private String serialNumber;

    @Column(name="passkey")
    private String passKey;

    @Column(name="activation_key")
    private String activationKey;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="register_date")
    private Date registerDate;

	@Column(name="type_app")
	private String typeApp;

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

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public void setTypeApp(String typeApp){
		this.typeApp = typeApp;
	}

	public String getTypeApp(){
		return this.typeApp;
	}
}
