package com.hmm.dms.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MetaDataHeader.
 */
@Entity
@Table(name = "meta_data_header")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MetaDataHeader extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "doc_title", nullable = false)
    private String docTitle;

    @NotNull
    @Column(name = "del_flag")
    private String delFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MetaDataHeader id(Long id) {
        this.id = id;
        return this;
    }

    public String getDocTitle() {
        return this.docTitle;
    }

    public MetaDataHeader docTitle(String docTitle) {
        this.docTitle = docTitle;
        return this;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
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
        if (!(o instanceof MetaDataHeader)) {
            return false;
        }
        return id != null && id.equals(((MetaDataHeader) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MetaDataHeader{" +
            "id=" + getId() +
            ", docTitle='" + getDocTitle() + "'" +
            "}";
    }
}
