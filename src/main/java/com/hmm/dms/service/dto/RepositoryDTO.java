package com.hmm.dms.service.dto;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

public class RepositoryDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long headerId;

    @NotNull
    private String folderName;

    private Integer folderOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Integer getFolderOrder() {
        return folderOrder;
    }

    public void setFolderOrder(Integer folderOrder) {
        this.folderOrder = folderOrder;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((folderName == null) ? 0 : folderName.hashCode());
        result = prime * result + ((folderOrder == null) ? 0 : folderOrder.hashCode());
        result = prime * result + ((headerId == null) ? 0 : headerId.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        RepositoryDTO other = (RepositoryDTO) obj;
        if (folderName == null) {
            if (other.folderName != null) return false;
        } else if (!folderName.equals(other.folderName)) return false;
        if (folderOrder == null) {
            if (other.folderOrder != null) return false;
        } else if (!folderOrder.equals(other.folderOrder)) return false;
        if (headerId == null) {
            if (other.headerId != null) return false;
        } else if (!headerId.equals(other.headerId)) return false;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "RepositoryDTO [id=" + id + ", headerId=" + headerId + ", folderName=" + folderName + ", folderOrder=" + folderOrder + "]";
    }
}
