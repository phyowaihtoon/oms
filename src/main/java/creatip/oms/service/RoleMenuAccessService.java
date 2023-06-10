package creatip.oms.service;

import creatip.oms.service.dto.RoleMenuAccessDTO;
import creatip.oms.service.message.MenuGroupMessage;
import java.util.List;

public interface RoleMenuAccessService {
    public List<RoleMenuAccessDTO> getAllMenuAccessByRole(Long roleId);

    public List<RoleMenuAccessDTO> getAllMenuItems();

    public List<MenuGroupMessage> getAllMenuGroupByRole(Long roleId);
}
