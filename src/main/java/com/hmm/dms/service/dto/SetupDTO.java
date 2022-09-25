package com.hmm.dms.service.dto;

import java.io.Serializable;

public class SetupDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String value;
    private String description;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
