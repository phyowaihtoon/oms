package com.hmm.dms.service.dto;

import java.io.Serializable;

public class RepositoryInquiryDTO implements Serializable {

    private String repositoryName;
    private String createdDate;

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
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
        result = prime * result + ((repositoryName == null) ? 0 : repositoryName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        RepositoryInquiryDTO other = (RepositoryInquiryDTO) obj;
        if (createdDate == null) {
            if (other.createdDate != null) return false;
        } else if (!createdDate.equals(other.createdDate)) return false;
        if (repositoryName == null) {
            if (other.repositoryName != null) return false;
        } else if (!repositoryName.equals(other.repositoryName)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "RepositoryInquiryDTO [repositoryName=" + repositoryName + ", createdDate=" + createdDate + "]";
    }
}
