package creatip.oms.service.message;

import creatip.oms.service.dto.MeetingAttachmentDTO;
import creatip.oms.service.dto.MeetingDeliveryDTO;
import creatip.oms.service.dto.MeetingReceiverDTO;
import java.io.Serializable;
import java.util.List;

public class MeetingMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private MeetingDeliveryDTO meetingDelivery;
    private List<MeetingReceiverDTO> receiverList;
    private List<MeetingAttachmentDTO> attachmentList;

    public MeetingDeliveryDTO getMeetingDelivery() {
        return meetingDelivery;
    }

    public List<MeetingReceiverDTO> getReceiverList() {
        return receiverList;
    }

    public List<MeetingAttachmentDTO> getAttachmentList() {
        return attachmentList;
    }

    public void setMeetingDelivery(MeetingDeliveryDTO meetingDelivery) {
        this.meetingDelivery = meetingDelivery;
    }

    public void setReceiverList(List<MeetingReceiverDTO> receiverList) {
        this.receiverList = receiverList;
    }

    public void setAttachmentList(List<MeetingAttachmentDTO> attachmentList) {
        this.attachmentList = attachmentList;
    }
}
