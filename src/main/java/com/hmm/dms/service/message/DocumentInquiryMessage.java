package com.hmm.dms.service.message;

import java.io.Serializable;

public class DocumentInquiryMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long metaDataHeaderId;
    private String repositoryURL;
    private String createdDate;
    private String generalValue;
    private String approvedBy;
    private String reason;
    private int status;
    // Field Value
    private String fieldValue1;
    private String fieldValue2;
    private String fieldValue3;
    private String fieldValue4;
    // Field Order
    private int fieldIndex1;
    private int fieldIndex2;
    private int fieldIndex3;
    private int fieldIndex4;
    // Search Type for each field
    private String fieldSearchType1;
    private String fieldSearchType2;
    private String fieldSearchType3;
    private String fieldSearchType4;
    // Field for sorting
    private int fieldSortBy1;
    private int fieldSortBy2;
    private int fieldSortBy3;
    private int fieldSortBy4;

    public Long getMetaDataHeaderId() {
        return metaDataHeaderId;
    }

    public void setMetaDataHeaderId(Long metaDataHeaderId) {
        this.metaDataHeaderId = metaDataHeaderId;
    }

    public String getRepositoryURL() {
        return repositoryURL;
    }

    public void setRepositoryURL(String repositoryURL) {
        this.repositoryURL = repositoryURL;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getGeneralValue() {
        return generalValue;
    }

    public void setGeneralValue(String generalValue) {
        this.generalValue = generalValue;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFieldValue1() {
        return fieldValue1;
    }

    public void setFieldValue1(String fieldValue1) {
        this.fieldValue1 = fieldValue1;
    }

    public String getFieldValue2() {
        return fieldValue2;
    }

    public void setFieldValue2(String fieldValue2) {
        this.fieldValue2 = fieldValue2;
    }

    public String getFieldValue3() {
        return fieldValue3;
    }

    public void setFieldValue3(String fieldValue3) {
        this.fieldValue3 = fieldValue3;
    }

    public String getFieldValue4() {
        return fieldValue4;
    }

    public void setFieldValue4(String fieldValue4) {
        this.fieldValue4 = fieldValue4;
    }

    public int getFieldIndex1() {
        return fieldIndex1;
    }

    public void setFieldIndex1(int fieldIndex1) {
        this.fieldIndex1 = fieldIndex1;
    }

    public int getFieldIndex2() {
        return fieldIndex2;
    }

    public void setFieldIndex2(int fieldIndex2) {
        this.fieldIndex2 = fieldIndex2;
    }

    public int getFieldIndex3() {
        return fieldIndex3;
    }

    public void setFieldIndex3(int fieldIndex3) {
        this.fieldIndex3 = fieldIndex3;
    }

    public int getFieldIndex4() {
        return fieldIndex4;
    }

    public void setFieldIndex4(int fieldIndex4) {
        this.fieldIndex4 = fieldIndex4;
    }

    public String getFieldSearchType1() {
        return fieldSearchType1;
    }

    public void setFieldSearchType1(String fieldSearchType1) {
        this.fieldSearchType1 = fieldSearchType1;
    }

    public String getFieldSearchType2() {
        return fieldSearchType2;
    }

    public void setFieldSearchType2(String fieldSearchType2) {
        this.fieldSearchType2 = fieldSearchType2;
    }

    public String getFieldSearchType3() {
        return fieldSearchType3;
    }

    public void setFieldSearchType3(String fieldSearchType3) {
        this.fieldSearchType3 = fieldSearchType3;
    }

    public String getFieldSearchType4() {
        return fieldSearchType4;
    }

    public void setFieldSearchType4(String fieldSearchType4) {
        this.fieldSearchType4 = fieldSearchType4;
    }

    public int getFieldSortBy1() {
        return fieldSortBy1;
    }

    public void setFieldSortBy1(int fieldSortBy1) {
        this.fieldSortBy1 = fieldSortBy1;
    }

    public int getFieldSortBy2() {
        return fieldSortBy2;
    }

    public void setFieldSortBy2(int fieldSortBy2) {
        this.fieldSortBy2 = fieldSortBy2;
    }

    public int getFieldSortBy3() {
        return fieldSortBy3;
    }

    public void setFieldSortBy3(int fieldSortBy3) {
        this.fieldSortBy3 = fieldSortBy3;
    }

    public int getFieldSortBy4() {
        return fieldSortBy4;
    }

    public void setFieldSortBy4(int fieldSortBy4) {
        this.fieldSortBy4 = fieldSortBy4;
    }
}
