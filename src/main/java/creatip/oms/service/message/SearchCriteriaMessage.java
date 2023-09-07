package creatip.oms.service.message;

import java.io.Serializable;

public class SearchCriteriaMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String dateOn;
    private String dateFrom;
    private String dateTo;
    private short status;

    public String getDateOn() {
        return dateOn;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public short getStatus() {
        return status;
    }

    public void setDateOn(String dateOn) {
        this.dateOn = dateOn;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public void setStatus(short status) {
        this.status = status;
    }
}
