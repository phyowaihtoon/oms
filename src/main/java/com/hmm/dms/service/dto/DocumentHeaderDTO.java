package com.hmm.dms.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class DocumentHeaderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long metaDataHeaderId;

    private String fieldNames;

    private String fieldValues;

    private String repositoryURL;

    private List<DocumentDTO> docList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMetaDataHeaderId() {
        return metaDataHeaderId;
    }

    public void setMetaDataHeaderId(Long metaDataHeaderId) {
        this.metaDataHeaderId = metaDataHeaderId;
    }

    public String getFieldNames() {
        return fieldNames;
    }

    public void setFieldNames(String fieldNames) {
        this.fieldNames = fieldNames;
    }

    public String getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(String fieldValues) {
        this.fieldValues = fieldValues;
    }

    public String getRepositoryURL() {
        return repositoryURL;
    }

    public void setRepositoryURL(String repositoryURL) {
        this.repositoryURL = repositoryURL;
    }

    public List<DocumentDTO> getDocList() {
        return docList;
    }

    public void setDocList(List<DocumentDTO> docList) {
        this.docList = docList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentHeaderDTO)) {
            return false;
        }

        DocumentHeaderDTO documentDTO = (DocumentHeaderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
