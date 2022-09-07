package com.hmm.dms.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DocumentHeader.
 */
@Entity
@Table(name = "document_header")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DocumentHeader extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "meta_data_header_id")
    private Long metaDataHeaderId;

    @Column(name = "field_names")
    private String fieldNames;

    @Column(name = "field_values")
    private String fieldValues;

    @Column(name = "repository_url")
    private String repositoryURL;

    @Column(name = "message")
    private String message;

    @NotNull
    @Column(name = "del_flag")
    private String delFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentHeader id(Long id) {
        this.id = id;
        return this;
    }

    public Long getMetaDataHeaderId() {
        return this.metaDataHeaderId;
    }

    public DocumentHeader metaDataHeaderId(Long metaDataHeaderId) {
        this.metaDataHeaderId = metaDataHeaderId;
        return this;
    }

    public void setMetaDataHeaderId(Long metaDataHeaderId) {
        this.metaDataHeaderId = metaDataHeaderId;
    }

    public String getFieldNames() {
        return this.fieldNames;
    }

    public DocumentHeader fieldNames(String fieldNames) {
        this.fieldNames = fieldNames;
        return this;
    }

    public void setFieldNames(String fieldNames) {
        this.fieldNames = fieldNames;
    }

    public String getFieldValues() {
        return this.fieldValues;
    }

    public DocumentHeader fieldValues(String fieldValues) {
        this.fieldValues = fieldValues;
        return this;
    }

    public void setFieldValues(String fieldValues) {
        this.fieldValues = fieldValues;
    }

    public String getRepositoryURL() {
        return this.repositoryURL;
    }

    public DocumentHeader repositoryURL(String repositoryURL) {
        this.repositoryURL = repositoryURL;
        return this;
    }

    public void setRepositoryURL(String repositoryURL) {
        this.repositoryURL = repositoryURL;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
        if (!(o instanceof DocumentHeader)) {
            return false;
        }
        return id != null && id.equals(((DocumentHeader) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentHeader{" +
            "id=" + getId() +
            ", metaDataHeaderId=" + getMetaDataHeaderId() +
            ", fieldNames='" + getFieldNames() + "'" +
            ", fieldValues='" + getFieldValues() + "'" +
            ", repositoryURL='" + getRepositoryURL() + "'" +
            ", message='" + getMessage() + "'" +
            "}";
    }
}
