package creatip.oms.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class DocumentHeaderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long metaDataHeaderId;

    private String fieldNames;

    private String fieldValues;

    private String repositoryURL;

    private String approvedBy;

    private int priority;

    private int status;

    private String reasonForAmend;

    private String reasonForReject;

    private String message;

    private String delFlag;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Instant approvedDate;

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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReasonForAmend() {
        return reasonForAmend;
    }

    public void setReasonForAmend(String reasonForAmend) {
        this.reasonForAmend = reasonForAmend;
    }

    public String getReasonForReject() {
        return reasonForReject;
    }

    public void setReasonForReject(String reasonForReject) {
        this.reasonForReject = reasonForReject;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public Instant getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Instant approvedDate) {
        this.approvedDate = approvedDate;
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
