package creatip.oms.service.message;

import creatip.oms.service.dto.DocumentAttachmentDTO;
import creatip.oms.service.dto.DocumentDeliveryDTO;
import creatip.oms.service.dto.DocumentReceiverDTO;
import java.io.Serializable;
import java.util.List;

public class DeliveryMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private DocumentDeliveryDTO documentDelivery;
    private List<DocumentReceiverDTO> receiverList;
    private List<DocumentAttachmentDTO> attachmentList;

    public DocumentDeliveryDTO getDocumentDelivery() {
        return documentDelivery;
    }

    public List<DocumentReceiverDTO> getReceiverList() {
        return receiverList;
    }

    public List<DocumentAttachmentDTO> getAttachmentList() {
        return attachmentList;
    }

    public void setDocumentDelivery(DocumentDeliveryDTO documentDelivery) {
        this.documentDelivery = documentDelivery;
    }

    public void setReceiverList(List<DocumentReceiverDTO> receiverList) {
        this.receiverList = receiverList;
    }

    public void setAttachmentList(List<DocumentAttachmentDTO> attachmentList) {
        this.attachmentList = attachmentList;
    }
}
