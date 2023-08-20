package creatip.oms.service.message;

import java.io.Serializable;

public class NotificationMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String referenceNo;
    private String subject;
    private String senderName;

    public Long getId() {
        return id;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public String getSubject() {
        return subject;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
