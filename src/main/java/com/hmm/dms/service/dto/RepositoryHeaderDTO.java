package com.hmm.dms.service.dto;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;

public class RepositoryHeaderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private String repositoryName;

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
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
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
        return "RepositoryHeaderDTO [id=" + id + ", repositoryName=" + repositoryName + ", repositoryDetails=" + repositoryDetails + "]";
    }
}
