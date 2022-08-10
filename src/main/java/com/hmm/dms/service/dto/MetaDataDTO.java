package com.hmm.dms.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.hmm.dms.domain.MetaData} entity.
 */
public class MetaDataDTO implements Serializable {

    private Long id;

    @NotNull
    private Long headerId;

    @NotNull
    private String fieldName;

    @NotNull
    private String fieldType;

    @NotNull
    private String isRequired;

    private Integer fieldOrder;

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

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(String isRequired) {
        this.isRequired = isRequired;
    }

    public Integer getFieldOrder() {
        return fieldOrder;
    }

    public void setFieldOrder(Integer fieldOrder) {
        this.fieldOrder = fieldOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MetaDataDTO)) {
            return false;
        }

        MetaDataDTO metaDataDTO = (MetaDataDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, metaDataDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MetaDataDTO{" +
            "id=" + getId() +
            ", headerId=" + getHeaderId() +
            ", fieldName='" + getFieldName() + "'" +
            ", fieldType='" + getFieldType() + "'" +
            ", isRequired='" + getIsRequired() + "'" +
            ", fieldOrder=" + getFieldOrder() +
            "}";
    }
}
