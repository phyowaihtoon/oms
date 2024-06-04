package creatip.oms.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link creatip.oms.domain.Announcement} entity.
 */
@JsonIgnoreProperties(value = { "isChecked" })
public class AnnouncementDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private String description;

    @NotNull
    private String delFlag;

    @NotNull
    private String activeFlag;

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

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnnouncementDTO)) {
            return false;
        }

        AnnouncementDTO announcementDTO = (AnnouncementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, announcementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "AnnouncementDTO{" +
            "id=" +
            getId() +
            ", description='" +
            getDescription() +
            "'" +
            ", delFlag='" +
            getDelFlag() +
            "'" +
            ", activeFlag='" +
            getActiveFlag() +
            "'" +
            "}"
        );
    }
}
