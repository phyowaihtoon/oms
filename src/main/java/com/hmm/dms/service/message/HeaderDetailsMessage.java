package com.hmm.dms.service.message;

import java.io.Serializable;
import java.util.List;

public class HeaderDetailsMessage<H, D1, D2, D3> extends BaseMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private H header;
    private List<D1> details1;
    private List<D2> details2;
    private List<D3> details3;

    public H getHeader() {
        return header;
    }

    public void setHeader(H header) {
        this.header = header;
    }

    public List<D1> getDetails1() {
        return details1;
    }

    public void setDetails1(List<D1> details1) {
        this.details1 = details1;
    }

    public List<D2> getDetails2() {
        return details2;
    }

    public void setDetails2(List<D2> details2) {
        this.details2 = details2;
    }

    public List<D3> getDetails3() {
        return details3;
    }

    public void setDetails3(List<D3> details3) {
        this.details3 = details3;
    }
}
