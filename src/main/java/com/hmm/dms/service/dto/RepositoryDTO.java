package com.hmm.dms.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.hmm.dms.domain.Repository} entity.
 */
public class RepositoryDTO implements Serializable {

    private Long id;

    private Integer parentID;

    @NotNull
    private String repositoryName;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getParentID() {
        return parentID;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RepositoryDTO)) {
            return false;
        }

        RepositoryDTO repositoryDTO = (RepositoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, repositoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RepositoryDTO{" +
            "id=" + getId() +
            ", parentID=" + getParentID() +
            ", repositoryName='" + getRepositoryName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
