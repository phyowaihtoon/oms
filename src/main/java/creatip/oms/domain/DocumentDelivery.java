package creatip.oms.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "document_delivery")
public class DocumentDelivery extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference_no")
    private String referenceNo;

    @Column(name = "sent_date")
    private Instant sentDate;

    @NotNull
    @Column(name = "subject")
    private String subject;

    @NotNull
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "delivery_status")
    private short deliveryStatus;

    @NotNull
    @Column(name = "status")
    private short status;

    @NotNull
    @Size(max = 1)
    @Column(name = "del_flag", length = 1, nullable = false)
    private String delFlag;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "sender_id")
    private Department sender;

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

    public Instant getSentDate() {
        return sentDate;
    }

    public void setSentDate(Instant sentDate) {
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

    public Department getSender() {
        return sender;
    }

    public void setSender(Department sender) {
        this.sender = sender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentDelivery)) {
            return false;
        }
        return id != null && id.equals(((DocumentDelivery) o).id);
    }
}
