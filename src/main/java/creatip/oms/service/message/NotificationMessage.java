package creatip.oms.service.message;

import java.io.Serializable;

public class NotificationMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    String referenceNo;
    String subject;

    public String getReferenceNo() {
        return referenceNo;
    }

    public String getSubject() {
        return subject;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
