package creatip.oms.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "head_department")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class HeadDepartment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    Long id;

    @NotNull
    @Column(name = "description", nullable = false)
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HeadDepartment)) {
            return false;
        }
        return id != null && id.equals(((HeadDepartment) o).id);
    }
}
