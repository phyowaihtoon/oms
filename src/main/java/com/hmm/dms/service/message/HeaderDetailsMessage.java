package com.hmm.dms.service.message;

import java.io.Serializable;
import java.util.List;

public class HeaderDetailsMessage<H, D> extends BaseMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private H header;
    private List<D> details;

    public H getHeader() {
        return header;
    }

    public void setHeader(H header) {
        this.header = header;
    }

    public List<D> getDetails() {
        return details;
    }

    public void setDetails(List<D> details) {
        this.details = details;
    }
}
