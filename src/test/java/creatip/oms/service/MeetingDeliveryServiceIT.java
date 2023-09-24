package creatip.oms.service;

import static org.assertj.core.api.Assertions.assertThat;

import creatip.oms.IntegrationTest;
import creatip.oms.service.dto.DepartmentDTO;
import creatip.oms.service.dto.MeetingAttachmentDTO;
import creatip.oms.service.dto.MeetingDeliveryDTO;
import creatip.oms.service.dto.MeetingReceiverDTO;
import creatip.oms.service.message.MeetingMessage;
import creatip.oms.service.message.ReplyMessage;
import creatip.oms.service.message.UploadFailedException;
import creatip.oms.util.ResponseCode;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.jfree.util.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

@IntegrationTest
public class MeetingDeliveryServiceIT {

    @Autowired
    private MeetingDeliveryService meetingDeliveryService;

    private MeetingMessage message;

    @BeforeEach
    public void createMessage() {
        message = new MeetingMessage();

        MeetingDeliveryDTO delivery = new MeetingDeliveryDTO();
        DepartmentDTO sender = new DepartmentDTO();
        sender.setId(1L);
        // delivery.setId(1L);
        delivery.setSender(sender);
        delivery.setSubject("OMS Issue");
        delivery.setDescription("Dear Monkey, How are you? We hope to meet you next weekend");
        delivery.setStatus((short) 0);
        delivery.setDeliveryStatus((short) 1);
        delivery.setDelFlag("N");
        delivery.setPlace("Main Building");
        delivery.setMeetingStatus((short) 1);

        Instant startTime = Instant.now();
        Instant endTime = startTime.plus(5, ChronoUnit.MINUTES);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Start Time: {} " + sdf.format(startTime));
        System.out.println("End Time: {} " + sdf.format(endTime));

        delivery.setStartDate(startTime.toString());
        delivery.setEndDate(endTime.toString());

        List<MeetingReceiverDTO> receiverList = new ArrayList<MeetingReceiverDTO>();
        DepartmentDTO mReceiver1 = new DepartmentDTO();
        mReceiver1.setId(2L);
        MeetingReceiverDTO docReciver1 = new MeetingReceiverDTO();
        //docReciver1.setId(1L);
        docReciver1.setReceiver(mReceiver1);
        docReciver1.setReceiverType((short) 1);
        docReciver1.setStatus((short) 0);
        docReciver1.setDelFlag("N");
        receiverList.add(docReciver1);

        DepartmentDTO ccReceiver1 = new DepartmentDTO();
        ccReceiver1.setId(3L);
        MeetingReceiverDTO docReciver2 = new MeetingReceiverDTO();
        docReciver2.setReceiver(ccReceiver1);
        docReciver2.setReceiverType((short) 1);
        docReciver2.setStatus((short) 0);
        docReciver2.setDelFlag("N");
        receiverList.add(docReciver2);

        List<MeetingAttachmentDTO> attachmentList = new ArrayList<MeetingAttachmentDTO>();
        MeetingAttachmentDTO attachment1 = new MeetingAttachmentDTO();
        //attachment1.setId(1L);
        attachment1.setFilePath("OMS\\20230814");
        attachment1.setFileName("test.pdf");
        attachment1.setDelFlag("N");
        attachmentList.add(attachment1);

        message.setMeetingDelivery(delivery);
        message.setReceiverList(receiverList);
        message.setAttachmentList(attachmentList);
    }

    @Test
    @Rollback(value = false)
    void saveDelivery() {
        try {
            ReplyMessage<MeetingMessage> replyMessage = meetingDeliveryService.save(message, null);
            if (replyMessage != null && replyMessage.getCode().equals(ResponseCode.SUCCESS)) assertThat(
                replyMessage.getData().getMeetingDelivery().getId()
            )
                .isGreaterThan(0);
        } catch (UploadFailedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
