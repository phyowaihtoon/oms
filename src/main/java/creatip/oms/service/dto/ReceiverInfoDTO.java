package creatip.oms.service.dto;

import java.io.Serializable;

public class ReceiverInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long departmentId;
    private String departmentName;
    private short receiverType;

    public Long getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public short getReceiverType() {
        return receiverType;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setReceiverType(short receiverType) {
        this.receiverType = receiverType;
    }
}
