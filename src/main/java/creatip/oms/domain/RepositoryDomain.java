package creatip.oms.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MetaData.
 */
@Entity
@Table(name = "repository")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RepositoryDomain implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "header_id", nullable = false)
    private Long headerId;

    @NotNull
    @Column(name = "folder_name", nullable = false)
    private String folderName;

    @Column(name = "folder_order")
    private Integer folderOrder;

    @NotNull
    @Column(name = "del_flag")
    private String delFlag;

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Integer getFolderOrder() {
        return folderOrder;
    }

    public void setFolderOrder(Integer folderOrder) {
        this.folderOrder = folderOrder;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RepositoryDomain)) {
            return false;
        }
        return id != null && id.equals(((RepositoryDomain) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "RepositoryDomain [id=" +
            id +
            ", headerId=" +
            headerId +
            ", folderName=" +
            folderName +
            ", folderOrder=" +
            folderOrder +
            ", delFlag=" +
            delFlag +
            "]"
        );
    }
    // prettier-ignore

}
