package id.co.knt.helpdesk.api.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="helpdesk_notification")
public class Notification implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name="message")
    private String message;

    @Column(name="created_date")
    private Long createdDate;

    @Column(name="isRead")
    private Boolean isRead;

    public Long getId() {
        return id;
    }

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

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }
}
