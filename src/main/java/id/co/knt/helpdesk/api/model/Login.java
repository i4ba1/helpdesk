package id.co.knt.helpdesk.api.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="helpdesk_login")
public class 			Login implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7210133046111533458L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="login_date")
	private Date loginDate;
	
	@Column(name="login_token")
	private String token;
	
	@Column(name="expired_time")
	private Long expiredTime;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="user_id", referencedColumnName="id")
	private User user;

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setExpiredTime(Long expiredTime) {
		this.expiredTime = expiredTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getId() {
		return id;
	}
}
