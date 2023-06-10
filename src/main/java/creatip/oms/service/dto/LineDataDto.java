package creatip.oms.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

public class LineDataDto implements Serializable {

    private String date;
    private Long newCount;
    private Long amendmentCount;
    private Long rejectedCount;
    private Long approvalCount;
    private Long approvedCount;
    private Long canceledCount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getNewCount() {
        return newCount;
    }

    public void setNewCount(Long newCount) {
        this.newCount = newCount;
    }

    public Long getAmendmentCount() {
        return amendmentCount;
    }

    public void setAmendmentCount(Long amendmentCount) {
        this.amendmentCount = amendmentCount;
    }

    public Long getRejectedCount() {
        return rejectedCount;
    }

    public void setRejectedCount(Long rejectedCount) {
        this.rejectedCount = rejectedCount;
    }

    public Long getApprovalCount() {
        return approvalCount;
    }

    public void setApprovalCount(Long approvalCount) {
        this.approvalCount = approvalCount;
    }

    public Long getApprovedCount() {
        return approvedCount;
    }

    public void setApprovedCount(Long approvedCount) {
        this.approvedCount = approvedCount;
    }

    public Long getCanceledCount() {
        return canceledCount;
    }

    public void setCanceledCount(Long canceledCount) {
        this.canceledCount = canceledCount;
    }

    @Override
    public String toString() {
        return (
            "LineDataDto [date=" +
            date +
            ", newCount=" +
            newCount +
            ", amendmentCount=" +
            amendmentCount +
            ", rejectedCount=" +
            rejectedCount +
            ", approvalCount=" +
            approvalCount +
            ", approvedCount=" +
            approvedCount +
            ", canceledCount=" +
            canceledCount +
            "]"
        );
    }

    public static LineDataDto toDTO(Object[] object) {
        LineDataDto dto = new LineDataDto();
        dto.setDate(((String) object[0]));
        dto.setNewCount(((BigDecimal) object[1]).longValue());
        dto.setAmendmentCount(((BigDecimal) object[2]).longValue());
        dto.setRejectedCount(((BigDecimal) object[3]).longValue());
        dto.setApprovalCount(((BigDecimal) object[4]).longValue());
        dto.setApprovedCount(((BigDecimal) object[5]).longValue());
        dto.setCanceledCount(((BigDecimal) object[6]).longValue());
        return dto;
    }
}
