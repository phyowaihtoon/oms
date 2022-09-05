package com.hmm.dms.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MetaData.
 */
@Entity
@Table(name = "meta_data")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MetaData extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "header_id", nullable = false)
    private Long headerId;

    @NotNull
    @Column(name = "field_name", nullable = false)
    private String fieldName;

    @NotNull
    @Column(name = "field_type", nullable = false)
    private String fieldType;

    @NotNull
    @Column(name = "is_required", nullable = false)
    private String isRequired;

    @Column(name = "field_order")
    private Integer fieldOrder;

    @Column(name = "field_value")
    private String fieldValue;

    @NotNull
    @Column(name = "del_flag")
    private String delFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MetaData id(Long id) {
        this.id = id;
        return this;
    }

    public Long getHeaderId() {
        return this.headerId;
    }

    public MetaData headerId(Long headerId) {
        this.headerId = headerId;
        return this;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public MetaData fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return this.fieldType;
    }

    public MetaData fieldType(String fieldType) {
        this.fieldType = fieldType;
        return this;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getIsRequired() {
        return this.isRequired;
    }

    public MetaData isRequired(String isRequired) {
        this.isRequired = isRequired;
        return this;
    }

    public void setIsRequired(String isRequired) {
        this.isRequired = isRequired;
    }

    public Integer getFieldOrder() {
        return this.fieldOrder;
    }

    public MetaData fieldOrder(Integer fieldOrder) {
        this.fieldOrder = fieldOrder;
        return this;
    }

    public void setFieldOrder(Integer fieldOrder) {
        this.fieldOrder = fieldOrder;
    }

    public String getFieldValue() {
        return this.fieldValue;
    }

    public MetaData fieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
        return this;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
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
        if (!(o instanceof MetaData)) {
            return false;
        }
        return id != null && id.equals(((MetaData) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MetaData{" +
            "id=" + getId() +
            ", headerId=" + getHeaderId() +
            ", fieldName='" + getFieldName() + "'" +
            ", fieldType='" + getFieldType() + "'" +
            ", isRequired='" + getIsRequired() + "'" +
            ", fieldOrder=" + getFieldOrder() +
            ", fieldValue='" + getFieldValue() + "'" +
            "}";
    }
}
