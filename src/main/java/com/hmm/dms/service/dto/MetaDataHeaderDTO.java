package com.hmm.dms.service.dto;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;

public class MetaDataHeaderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private String docTitle;

    @NotNull
    private List<MetaDataDTO> metaDataDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocTitle() {
        return docTitle;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    public List<MetaDataDTO> getMetaDataDetails() {
        return metaDataDetails;
    }

    public void setMetaDataDetails(List<MetaDataDTO> metaDataDetails) {
        this.metaDataDetails = metaDataDetails;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((docTitle == null) ? 0 : docTitle.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((metaDataDetails == null) ? 0 : metaDataDetails.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        MetaDataHeaderDTO other = (MetaDataHeaderDTO) obj;
        if (docTitle == null) {
            if (other.docTitle != null) return false;
        } else if (!docTitle.equals(other.docTitle)) return false;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (metaDataDetails == null) {
            if (other.metaDataDetails != null) return false;
        } else if (!metaDataDetails.equals(other.metaDataDetails)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "MetaDataHeaderDTO [id=" + id + ", docTitle=" + docTitle + ", metaDataDetails=" + metaDataDetails + "]";
    }
}
