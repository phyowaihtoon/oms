package com.hmm.dms.service.dto;

import java.io.Serializable;

public class ReplyMessage<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;
    private String description;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
