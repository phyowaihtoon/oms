package com.hmm.dms.service.dto;

import java.io.Serializable;

public class MetaDataInquiryDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String docTitle;
    private String createdDate;

    public String getDocTitle() {
        return docTitle;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
        result = prime * result + ((docTitle == null) ? 0 : docTitle.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        MetaDataInquiryDTO other = (MetaDataInquiryDTO) obj;
        if (createdDate == null) {
            if (other.createdDate != null) return false;
        } else if (!createdDate.equals(other.createdDate)) return false;
        if (docTitle == null) {
            if (other.docTitle != null) return false;
        } else if (!docTitle.equals(other.docTitle)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "MetaDataInquiryDTO [docTitle=" + docTitle + ", createdDate=" + createdDate + "]";
    }
}
