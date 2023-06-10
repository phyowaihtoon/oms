package creatip.oms.service.impl;

import creatip.oms.domain.MenuGroup;
import creatip.oms.domain.MenuItem;
import creatip.oms.domain.RoleMenuAccess;
import creatip.oms.repository.MenuGroupRepository;
import creatip.oms.repository.MenuItemRepository;
import creatip.oms.repository.RoleMenuAccessRepository;
import creatip.oms.service.RoleMenuAccessService;
import creatip.oms.service.dto.MenuItemDTO;
import creatip.oms.service.dto.RoleMenuAccessDTO;
import creatip.oms.service.mapper.MenuItemMapper;
import creatip.oms.service.mapper.RoleMenuAccessMapper;
import creatip.oms.service.message.MenuGroupMessage;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleMenuAccessServiceImpl implements RoleMenuAccessService {

    private final RoleMenuAccessRepository roleMenuAccessRepository;
    private final RoleMenuAccessMapper roleMenuAccessMapper;
    private final MenuItemRepository menuItemRepository;
    private final MenuItemMapper menuItemMapper;
    private final MenuGroupRepository menuGroupRepository;

    public RoleMenuAccessServiceImpl(
        RoleMenuAccessRepository roleMenuAccessRepository,
        RoleMenuAccessMapper roleMenuAccessMapper,
        MenuItemRepository menuItemRepository,
        MenuItemMapper menuItemMapper,
        MenuGroupRepository menuGroupRepository
    ) {
        this.roleMenuAccessRepository = roleMenuAccessRepository;
        this.roleMenuAccessMapper = roleMenuAccessMapper;
        this.menuItemRepository = menuItemRepository;
        this.menuItemMapper = menuItemMapper;
        this.menuGroupRepository = menuGroupRepository;
    }

    @Override
    public List<RoleMenuAccessDTO> getAllMenuAccessByRole(Long roleId) {
        List<RoleMenuAccess> entityList = this.roleMenuAccessRepository.findAllByUserRoleId(roleId);
        List<RoleMenuAccessDTO> dtoList = this.roleMenuAccessMapper.toDto(entityList);

        // Menu Items which are not defined in Role Menu Access
        List<MenuItem> menuItemEntityList = this.menuItemRepository.findAllNotDefinedByRoleId(roleId);
        List<MenuItemDTO> menuItemDTOList = this.menuItemMapper.toDto(menuItemEntityList);
        if (menuItemDTOList != null && menuItemDTOList.size() > 0) {
            for (MenuItemDTO menuItemDTO : menuItemDTOList) {
                RoleMenuAccessDTO menuAccessDTO = new RoleMenuAccessDTO();
                menuAccessDTO.setMenuItem(menuItemDTO);
                dtoList.add(menuAccessDTO);
            }
        }

        return dtoList;
    }

    /* Retrieving All Menu Items*/
    @Override
    public List<RoleMenuAccessDTO> getAllMenuItems() {
        List<RoleMenuAccessDTO> dtoList = null;
        List<MenuItem> menuItemEntityList = this.menuItemRepository.findAllOrderByMenuGroup();
        List<MenuItemDTO> menuItemDTOList = this.menuItemMapper.toDto(menuItemEntityList);
        if (menuItemDTOList != null && menuItemDTOList.size() > 0) {
            dtoList = new ArrayList<RoleMenuAccessDTO>();
            for (MenuItemDTO menuItemDTO : menuItemDTOList) {
                RoleMenuAccessDTO menuAccessDTO = new RoleMenuAccessDTO();
                menuAccessDTO.setMenuItem(menuItemDTO);
                dtoList.add(menuAccessDTO);
            }
        }

        return dtoList;
    }

    @Override
    public List<MenuGroupMessage> getAllMenuGroupByRole(Long roleId) {
        List<MenuGroupMessage> menuGroupMessageList = null;
        List<MenuGroup> menuGroupEnityList = this.menuGroupRepository.findAllMenuGroupByRoleId(roleId);
        if (menuGroupEnityList != null && menuGroupEnityList.size() > 0) {
            menuGroupMessageList = new ArrayList<MenuGroupMessage>();
            for (MenuGroup data : menuGroupEnityList) {
                List<RoleMenuAccess> entityList = this.roleMenuAccessRepository.findAllByUserRoleAndMenuGroup(roleId, data.getId());
                List<RoleMenuAccessDTO> roleMenuAccessList = this.roleMenuAccessMapper.toDto(entityList);

                MenuGroupMessage message = new MenuGroupMessage();
                message.setId(data.getId());
                message.setGroupCode(data.getGroupCode());
                message.setName(data.getName());
                message.setTranslateKey(data.getTranslateKey());
                message.setFaIcon(data.getFaIcon());
                message.setOrderNo(data.getOrderNo());
                message.setRouterLink(data.getRouterLink());
                message.setSubMenuItems(roleMenuAccessList);

                menuGroupMessageList.add(message);
            }
        }

        return menuGroupMessageList;
    }
}
