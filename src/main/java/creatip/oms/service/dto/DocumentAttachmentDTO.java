package creatip.oms.service.dto;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

public class DocumentAttachmentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private DocumentDeliveryDTO header;

    @NotNull
    private String filePath;

    @NotNull
    private String fileName;

    @NotNull
    private String delFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentDeliveryDTO getHeader() {
        return header;
    }

    public void setHeader(DocumentDeliveryDTO header) {
        this.header = header;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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
        if (!(o instanceof DocumentAttachmentDTO)) {
            return false;
        }
        return id != null && id.equals(((DocumentAttachmentDTO) o).id);
    }
}
