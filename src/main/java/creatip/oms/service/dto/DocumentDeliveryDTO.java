package creatip.oms.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import javax.validation.constraints.NotNull;

public class DocumentDeliveryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String referenceNo;

    private String sentDate;

    @NotNull
    private String subject;

    @NotNull
    private String description;

    @NotNull
    private short deliveryStatus;

    @NotNull
    private short status;

    @NotNull
    private String delFlag;

    @NotNull
    private DepartmentDTO sender;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private short receiverType;

    private List<ReceiverInfoDTO> receiverList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getSentDate() {
        return sentDate;
    }

    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public short getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(short deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public DepartmentDTO getSender() {
        return sender;
    }

    public void setSender(DepartmentDTO sender) {
        this.sender = sender;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public short getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(short receiverType) {
        this.receiverType = receiverType;
    }

    public List<ReceiverInfoDTO> getReceiverList() {
        return receiverList;
    }

    public void setReceiverList(List<ReceiverInfoDTO> receiverList) {
        this.receiverList = receiverList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentDeliveryDTO)) {
            return false;
        }
        return id != null && id.equals(((DocumentDeliveryDTO) o).id);
    }
}
