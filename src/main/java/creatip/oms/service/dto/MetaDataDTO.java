package creatip.oms.service.dto;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link creatip.oms.domain.MetaData} entity.
 */
public class MetaDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long headerId;

    @NotNull
    private String fieldName;

    private String fieldNameInMyanmar;

    @NotNull
    private String fieldType;

    @NotNull
    private String isRequired;

    private Integer fieldOrder;

    private String fieldValue;

    @NotNull
    private String delFlag;

    @NotNull
    private String showDashboard;

    @NotNull
    private String searchBy;

    @NotNull
    private String searchType;

    @NotNull
    private String sortBy;

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

    public String getFieldNameInMyanmar() {
        return fieldNameInMyanmar;
    }

    public void setFieldNameInMyanmar(String fieldNameInMyanmar) {
        this.fieldNameInMyanmar = fieldNameInMyanmar;
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

    public String getFieldValue() {
        return fieldValue;
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

    public String getShowDashboard() {
        return showDashboard;
    }

    public void setShowDashboard(String showDashboard) {
        this.showDashboard = showDashboard;
    }

    public String getSearchBy() {
        return searchBy;
    }

    public void setSearchBy(String searchBy) {
        this.searchBy = searchBy;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
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
        if (!(o instanceof MetaDataDTO)) {
            return false;
        }
        return id != null && id.equals(((MetaDataDTO) o).id);
    }

    @Override
    public String toString() {
        return (
            "MetaDataDTO [id=" +
            id +
            ", headerId=" +
            headerId +
            ", fieldName=" +
            fieldName +
            ", fieldType=" +
            fieldType +
            ", isRequired=" +
            isRequired +
            ", fieldOrder=" +
            fieldOrder +
            ", fieldValue=" +
            fieldValue +
            ", delFlag=" +
            delFlag +
            ", showDashboard=" +
            showDashboard +
            "]"
        );
    }
}
