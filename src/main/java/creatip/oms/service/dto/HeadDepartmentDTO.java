package creatip.oms.service.dto;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

public class HeadDepartmentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    Long id;

    @NotNull
    String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "HeadDepartmentDTO{" + "id=" + getId() + ", description='" + getDescription() + "'" + "}";
    }
}
