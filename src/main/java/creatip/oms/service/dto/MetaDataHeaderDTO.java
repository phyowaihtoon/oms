package creatip.oms.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import javax.validation.constraints.NotNull;

public class MetaDataHeaderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private String docTitle;

    @NotNull
    private String delFlag;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

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

    public List<MetaDataDTO> getMetaDataDetails() {
        return metaDataDetails;
    }

    public void setMetaDataDetails(List<MetaDataDTO> metaDataDetails) {
        this.metaDataDetails = metaDataDetails;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MetaDataHeaderDTO)) {
            return false;
        }
        return id != null && id.equals(((MetaDataHeaderDTO) o).id);
    }

    @Override
    public String toString() {
        return (
            "MetaDataHeaderDTO [id=" +
            id +
            ", docTitle=" +
            docTitle +
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
            ", metaDataDetails=" +
            metaDataDetails +
            "]"
        );
    }
}
