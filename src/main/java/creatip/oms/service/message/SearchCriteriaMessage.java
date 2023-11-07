package creatip.oms.service.message;

import java.io.Serializable;

public class SearchCriteriaMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private byte requestFrom;
    private String dateOn;
    private String dateFrom;
    private String dateTo;
    private String referenceNo;
    private short status;
    private Long senderId;
    private Long receiverId;
    private String subject;

    public byte getRequestFrom() {
        return requestFrom;
    }

    public String getDateOn() {
        return dateOn;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public short getStatus() {
        return status;
    }

    public Long getSenderId() {
        return senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public String getSubject() {
        return subject;
    }

    public void setRequestFrom(byte requestFrom) {
        this.requestFrom = requestFrom;
    }

    public void setDateOn(String dateOn) {
        this.dateOn = dateOn;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
