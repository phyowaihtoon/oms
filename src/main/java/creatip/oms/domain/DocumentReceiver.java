package creatip.oms.domain;

import java.io.Serializable;
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
@Table(name = "document_receiver")
public class DocumentReceiver implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "header_id")
    private DocumentDelivery header;

    @Column(name = "receiver_type", nullable = false)
    private short receiverType;

    @Column(name = "status", nullable = false)
    private short status;

    @NotNull
    @Size(max = 1)
    @Column(name = "del_flag", length = 1, nullable = false)
    private String delFlag;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "receiver_id")
    private Department receiver;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentDelivery getHeader() {
        return header;
    }

    public void setHeader(DocumentDelivery header) {
        this.header = header;
    }

    public short getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(short receiverType) {
        this.receiverType = receiverType;
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

    public Department getReceiver() {
        return receiver;
    }

    public void setReceiver(Department receiver) {
        this.receiver = receiver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentReceiver)) {
            return false;
        }
        return id != null && id.equals(((DocumentReceiver) o).id);
    }
}
