package com.hmm.dms.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Document.
 */
@Entity
@Table(name = "document")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Document extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "header_id", nullable = false)
    private Long headerId;

    @NotNull
    @Column(name = "file_path", nullable = false)
    private String filePath;

    @NotNull
    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_name_version", nullable = false)
    private String fileNameVersion;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "version")
    private float version;

    @Column(name = "remark")
    private String remark;

    @NotNull
    @Column(name = "del_flag")
    private String delFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Document id(Long id) {
        this.id = id;
        return this;
    }

    public Long getHeaderId() {
        return this.headerId;
    }

    public Document headerId(Long headerId) {
        this.headerId = headerId;
        return this;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public Document filePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Document fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileNameVersion() {
        return fileNameVersion;
    }

    public void setFileNameVersion(String fileNameVersion) {
        this.fileNameVersion = fileNameVersion;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        if (!(o instanceof Document)) {
            return false;
        }
        return id != null && id.equals(((Document) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Document{" +
            "id=" + getId() +
            ", headerId=" + getHeaderId() +
            ", filePath='" + getFilePath() + "'" +
            "}";
    }
}
