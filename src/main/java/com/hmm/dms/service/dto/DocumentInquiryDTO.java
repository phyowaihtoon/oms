package com.hmm.dms.service.dto;

import java.io.Serializable;

public class DocumentInquiryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long metaDataHeaderId;
    private String repositoryURL;
    private String createdDate;
    private String fieldValues;

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

    public String getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(String fieldValues) {
        this.fieldValues = fieldValues;
    }
}
