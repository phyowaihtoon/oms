package com.hmm.dms.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CodeDefinition.
 */
@Entity
@Table(name = "code_definition")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CodeDefinition extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    private MetaDataHeader metaDataHeader;

    @NotNull
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @NotNull
    @Column(name = "definition", nullable = false)
    private String definition;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CodeDefinition id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MetaDataHeader getMetaDataHeader() {
        return metaDataHeader;
    }

    public CodeDefinition metaDataHeader(MetaDataHeader metaDataHeader) {
        this.setMetaDataHeader(metaDataHeader);
        return this;
    }

    public void setMetaDataHeader(MetaDataHeader metaDataHeader) {
        this.metaDataHeader = metaDataHeader;
    }

    public String getCode() {
        return this.code;
    }

    public CodeDefinition code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDefinition() {
        return this.definition;
    }

    public CodeDefinition definition(String definition) {
        this.setDefinition(definition);
        return this;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CodeDefinition)) {
            return false;
        }
        return id != null && id.equals(((CodeDefinition) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CodeDefinition{" +
            "id=" + getId() +
            ", template='" + getMetaDataHeader()==null?"": getMetaDataHeader().getDocTitle()+ "'" +
            ", code='" + getCode() + "'" +
            ", definition='" + getDefinition() + "'" +
            "}";
    }
}
