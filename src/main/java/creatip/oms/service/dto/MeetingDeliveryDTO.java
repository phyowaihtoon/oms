package creatip.oms.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import javax.validation.constraints.NotNull;

public class MeetingDeliveryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String referenceNo;

    private String sentDate;

    private String startDate;

    private String endDate;

    private String place;

    @NotNull
    private String subject;

    @NotNull
    private String description;

    @NotNull
    private short deliveryStatus;

    @NotNull
    private short meetingStatus;

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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

    public short getMeetingStatus() {
        return meetingStatus;
    }

    public void setMeetingStatus(short meetingStatus) {
        this.meetingStatus = meetingStatus;
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
        if (!(o instanceof MeetingDeliveryDTO)) {
            return false;
        }
        return id != null && id.equals(((MeetingDeliveryDTO) o).id);
    }
}
