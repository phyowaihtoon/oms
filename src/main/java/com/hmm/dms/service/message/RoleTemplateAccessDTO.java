package com.hmm.dms.service.message;

import com.hmm.dms.service.dto.MetaDataHeaderDTO;
import com.hmm.dms.service.dto.UserRoleDTO;
import java.io.Serializable;

public class RoleTemplateAccessDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private MetaDataHeaderDTO metaDataHeader;
    private UserRoleDTO userRole;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MetaDataHeaderDTO getMetaDataHeader() {
        return metaDataHeader;
    }

    public void setMetaDataHeader(MetaDataHeaderDTO metaDataHeader) {
        this.metaDataHeader = metaDataHeader;
    }

    public UserRoleDTO getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRoleDTO userRole) {
        this.userRole = userRole;
    }
}
