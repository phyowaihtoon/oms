package creatip.oms.service.dto;

public class InputParamDto {

    private Long templateId;
    private String fromDate;
    private String toDate;

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    @Override
    public String toString() {
        return "InputParamDto [templateId=" + templateId + ", fromDate=" + fromDate + ", toDate=" + toDate + "]";
    }
}
