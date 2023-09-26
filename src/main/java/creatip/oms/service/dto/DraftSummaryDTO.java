package creatip.oms.service.dto;

import java.io.Serializable;

public class DraftSummaryDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long deliveryCount;
    private Long meetingCount;

    public Long getDeliveryCount() {
        return deliveryCount;
    }

    public Long getMeetingCount() {
        return meetingCount;
    }

    public void setDeliveryCount(Long deliveryCount) {
        this.deliveryCount = deliveryCount;
    }

    public void setMeetingCount(Long meetingCount) {
        this.meetingCount = meetingCount;
    }
}
