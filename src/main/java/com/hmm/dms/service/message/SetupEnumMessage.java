package com.hmm.dms.service.message;

import java.io.Serializable;

public class SetupEnumMessage<V, D> implements Serializable {

    private static final long serialVersionUID = 1L;

    private V value;
    private D description;

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public D getDescription() {
        return description;
    }

    public void setDescription(D description) {
        this.description = description;
    }
}
