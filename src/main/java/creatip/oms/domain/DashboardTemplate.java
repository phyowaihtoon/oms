package creatip.oms.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DashboardTemplate.
 */
@Entity
@Table(name = "dashboard_template")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DashboardTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "card_id", nullable = false)
    private String cardId;

    @NotNull
    @Column(name = "card_name", nullable = false)
    private String cardName;

    @NotNull
    @Column(name = "card_type", nullable = false)
    private String cardType;

    @NotNull
    @Column(name = "service_url", nullable = false)
    private String serviceUrl;

    @Column(name = "name_in_myanmar")
    private String nameInMyanmar;

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

    public String getNameInMyanmar() {
        return nameInMyanmar;
    }

    public void setNameInMyanmar(String nameInMyanmar) {
        this.nameInMyanmar = nameInMyanmar;
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
        if (!(o instanceof DashboardTemplate)) {
            return false;
        }
        return id != null && id.equals(((DashboardTemplate) o).id);
    }

    @Override
    public String toString() {
        return (
            "DashboardTemplate [id=" +
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
