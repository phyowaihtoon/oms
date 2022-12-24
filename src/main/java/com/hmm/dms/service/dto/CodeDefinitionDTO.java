package com.hmm.dms.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.hmm.dms.domain.CodeDefinition} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CodeDefinitionDTO implements Serializable {

    private Long id;

    @NotNull
    private String code;

    @NotNull
    private String definition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CodeDefinitionDTO)) {
            return false;
        }

        CodeDefinitionDTO codeDefinitionDTO = (CodeDefinitionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, codeDefinitionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CodeDefinitionDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", definition='" + getDefinition() + "'" +
            "}";
    }
}