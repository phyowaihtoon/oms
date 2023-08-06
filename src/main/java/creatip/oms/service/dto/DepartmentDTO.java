package creatip.oms.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link creatip.oms.domain.Department} entity.
 */
public class DepartmentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private String departmentName;

    @NotNull
    private String delFlag;

    private HeadDepartmentDTO headDepartment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public HeadDepartmentDTO getHeadDepartment() {
        return headDepartment;
    }

    public void setHeadDepartment(HeadDepartmentDTO headDepartment) {
        this.headDepartment = headDepartment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepartmentDTO)) {
            return false;
        }

        DepartmentDTO departmentDTO = (DepartmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, departmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepartmentDTO{" +
            "id=" + getId() +
            ", departmentName='" + getDepartmentName() + "'" +
            ", delFlag='" + getDelFlag() + "'" +
            "}";
    }
}
