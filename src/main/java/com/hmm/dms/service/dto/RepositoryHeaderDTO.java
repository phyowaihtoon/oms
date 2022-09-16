package com.hmm.dms.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import javax.validation.constraints.NotNull;

public class RepositoryHeaderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private String repositoryName;

    @NotNull
    private String delFlag;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    @NotNull
    private List<RepositoryDTO> repositoryDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public List<RepositoryDTO> getRepositoryDetails() {
        return repositoryDetails;
    }

    public void setRepositoryDetails(List<RepositoryDTO> repositoryDetails) {
        this.repositoryDetails = repositoryDetails;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
        result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
        result = prime * result + ((delFlag == null) ? 0 : delFlag.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((lastModifiedBy == null) ? 0 : lastModifiedBy.hashCode());
        result = prime * result + ((lastModifiedDate == null) ? 0 : lastModifiedDate.hashCode());
        result = prime * result + ((repositoryDetails == null) ? 0 : repositoryDetails.hashCode());
        result = prime * result + ((repositoryName == null) ? 0 : repositoryName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        RepositoryHeaderDTO other = (RepositoryHeaderDTO) obj;
        if (createdBy == null) {
            if (other.createdBy != null) return false;
        } else if (!createdBy.equals(other.createdBy)) return false;
        if (createdDate == null) {
            if (other.createdDate != null) return false;
        } else if (!createdDate.equals(other.createdDate)) return false;
        if (delFlag == null) {
            if (other.delFlag != null) return false;
        } else if (!delFlag.equals(other.delFlag)) return false;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (lastModifiedBy == null) {
            if (other.lastModifiedBy != null) return false;
        } else if (!lastModifiedBy.equals(other.lastModifiedBy)) return false;
        if (lastModifiedDate == null) {
            if (other.lastModifiedDate != null) return false;
        } else if (!lastModifiedDate.equals(other.lastModifiedDate)) return false;
        if (repositoryDetails == null) {
            if (other.repositoryDetails != null) return false;
        } else if (!repositoryDetails.equals(other.repositoryDetails)) return false;
        if (repositoryName == null) {
            if (other.repositoryName != null) return false;
        } else if (!repositoryName.equals(other.repositoryName)) return false;
        return true;
    }

    @Override
    public String toString() {
        return (
            "RepositoryHeaderDTO [id=" +
            id +
            ", repositoryName=" +
            repositoryName +
            ", delFlag=" +
            delFlag +
            ", createdBy=" +
            createdBy +
            ", createdDate=" +
            createdDate +
            ", lastModifiedBy=" +
            lastModifiedBy +
            ", lastModifiedDate=" +
            lastModifiedDate +
            ", repositoryDetails=" +
            repositoryDetails +
            "]"
        );
    }
}
