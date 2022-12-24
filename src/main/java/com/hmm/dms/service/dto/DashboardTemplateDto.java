package com.hmm.dms.service.dto;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

public class DashboardTemplateDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private String cardId;

    @NotNull
    private String cardName;

    @NotNull
    private String cardType;

    @NotNull
    private String serviceUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DashboardTemplateDto)) {
            return false;
        }
        return id != null && id.equals(((DashboardTemplateDto) o).id);
    }

    @Override
    public String toString() {
        return (
            "DashboardTemplateDto [id=" +
            id +
            ", cardId=" +
            cardId +
            ", cardName=" +
            cardName +
            ", cardType=" +
            cardType +
            ", serviceUrl=" +
            serviceUrl +
            "]"
        );
    }
}
