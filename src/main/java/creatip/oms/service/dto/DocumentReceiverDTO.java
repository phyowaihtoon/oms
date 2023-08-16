package creatip.oms.service.dto;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

public class DocumentReceiverDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private DocumentDeliveryDTO header;

    private short receiverType;

    private short status;

    @NotNull
    private String delFlag;

    @NotNull
    private DepartmentDTO receiver;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentDeliveryDTO getHeader() {
        return header;
    }

    public void setHeader(DocumentDeliveryDTO header) {
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

    public DepartmentDTO getReceiver() {
        return receiver;
    }

    public void setReceiver(DepartmentDTO receiver) {
        this.receiver = receiver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentReceiverDTO)) {
            return false;
        }
        return id != null && id.equals(((DocumentReceiverDTO) o).id);
    }
}
