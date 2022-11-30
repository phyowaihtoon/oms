package com.hmm.dms.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "role_template_access")
public class RoleTemplateAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private MetaDataHeader metaDataHeader;

    @ManyToOne
    private UserRole userRole;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MetaDataHeader getMetaDataHeader() {
        return metaDataHeader;
    }

    public void setMetaDataHeader(MetaDataHeader metaDataHeader) {
        this.metaDataHeader = metaDataHeader;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
