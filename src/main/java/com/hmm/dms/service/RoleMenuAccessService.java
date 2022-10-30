package com.hmm.dms.service;

import com.hmm.dms.service.dto.RoleMenuAccessDTO;
import com.hmm.dms.service.message.MenuGroupMessage;
import java.util.List;

public interface RoleMenuAccessService {
    public List<RoleMenuAccessDTO> getAllMenuAccessByRole(Long roleId);

    public List<RoleMenuAccessDTO> getAllMenuItems();

    public List<MenuGroupMessage> getAllMenuGroupByRole(Long roleId);
}
