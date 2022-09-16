package com.hmm.dms.domain;

import com.hmm.dms.service.dto.RepositoryDTO;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MetaDataHeader.
 */
@Entity
@Table(name = "repository_header")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RepositoryHeader extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "repository_name", nullable = false)
    private String repositoryName;

    @NotNull
    @Column(name = "del_flag")
    private String delFlag;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RepositoryHeader)) {
            return false;
        }
        return id != null && id.equals(((RepositoryHeader) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "RepositoryHeader [id=" + id + ", repositoryName=" + repositoryName + ", delFlag=" + delFlag + "]";
    }

    public void setRepositoryDetails(List<RepositoryDTO> collect) {
        // TODO Auto-generated method stub

    }
}
