package creatip.oms.service;

import static org.assertj.core.api.Assertions.assertThat;

import creatip.oms.IntegrationTest;
import creatip.oms.service.dto.DepartmentDTO;
import creatip.oms.service.dto.DocumentAttachmentDTO;
import creatip.oms.service.dto.DocumentDeliveryDTO;
import creatip.oms.service.dto.DocumentReceiverDTO;
import creatip.oms.service.message.DeliveryMessage;
import creatip.oms.service.message.ReplyMessage;
import creatip.oms.service.message.SearchCriteriaMessage;
import creatip.oms.service.message.UploadFailedException;
import creatip.oms.util.ResponseCode;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

@IntegrationTest
public class DocumentDeliveryServiceIT {

    @Autowired
    private DocumentDeliveryService documentDeliveryService;

    private DeliveryMessage message;

    @BeforeEach
    public void createMessage() {
        message = new DeliveryMessage();

        DocumentDeliveryDTO delivery = new DocumentDeliveryDTO();
        DepartmentDTO sender = new DepartmentDTO();
        sender.setId(1L);
        // delivery.setId(1L);
        delivery.setSender(sender);
        delivery.setSubject("OMS Issue");
        delivery.setDescription("Dear Monkey, How are you? We hope to meet you next weekend");
        delivery.setStatus((short) 0);
        delivery.setDeliveryStatus((short) 1);
        delivery.setDelFlag("N");

        List<DocumentReceiverDTO> receiverList = new ArrayList<DocumentReceiverDTO>();
        DepartmentDTO mReceiver1 = new DepartmentDTO();
        mReceiver1.setId(2L);
        DocumentReceiverDTO docReciver1 = new DocumentReceiverDTO();
        //docReciver1.setId(1L);
        docReciver1.setReceiver(mReceiver1);
        docReciver1.setReceiverType((short) 1);
        docReciver1.setStatus((short) 0);
        docReciver1.setDelFlag("N");
        receiverList.add(docReciver1);

        DepartmentDTO ccReceiver1 = new DepartmentDTO();
        ccReceiver1.setId(3L);
        DocumentReceiverDTO docReciver2 = new DocumentReceiverDTO();
        docReciver2.setReceiver(ccReceiver1);
        docReciver2.setReceiverType((short) 1);
        docReciver2.setStatus((short) 0);
        docReciver2.setDelFlag("N");
        receiverList.add(docReciver2);

        List<DocumentAttachmentDTO> attachmentList = new ArrayList<DocumentAttachmentDTO>();
        DocumentAttachmentDTO attachment1 = new DocumentAttachmentDTO();
        //attachment1.setId(1L);
        attachment1.setFilePath("OMS\\20230814");
        attachment1.setFileName("test.pdf");
        attachment1.setDelFlag("N");
        attachmentList.add(attachment1);

        message.setDocumentDelivery(delivery);
        message.setReceiverList(receiverList);
        message.setAttachmentList(attachmentList);
    }

    @Test
    @Rollback(value = false)
    void saveDelivery() {
        try {
            ReplyMessage<DeliveryMessage> replyMessage = documentDeliveryService.save(message, null);
            if (replyMessage != null && replyMessage.getCode().equals(ResponseCode.SUCCESS)) assertThat(
                replyMessage.getData().getDocumentDelivery().getId()
            )
                .isGreaterThan(0);
        } catch (UploadFailedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    void getDocmentReceived() {
        try {
            Pageable pageable = PageRequest.of(0, 10); // Page 0, Page size 10
            SearchCriteriaMessage message = new SearchCriteriaMessage();
            message.setReceiverId(1L);
            message.setStatus((short) 0);
            Page<DocumentDeliveryDTO> page = documentDeliveryService.getReceivedDeliveryList(message, pageable);
            List<DocumentDeliveryDTO> list = page.getContent();
            for (DocumentDeliveryDTO data : list) {
                System.out.println("Document ID " + data.getId() + " Document No. " + data.getReferenceNo());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
