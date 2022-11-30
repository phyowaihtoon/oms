package com.hmm.dms.service.message;

import java.io.Serializable;

public class DocumentInquiryMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long metaDataHeaderId;
    private String repositoryURL;
    private String createdDate;
    private String fieldValue1;
    private String fieldValue2;
    private String reason;
    private int status;
    private int fieldIndex1;
    private int fieldIndex2;
    private String generalValue;
    private String approvedBy;

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
}
