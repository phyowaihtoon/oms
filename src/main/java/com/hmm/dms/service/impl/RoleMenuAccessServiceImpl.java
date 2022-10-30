package com.hmm.dms.service.impl;

import com.hmm.dms.domain.MenuGroup;
import com.hmm.dms.domain.MenuItem;
import com.hmm.dms.domain.RoleMenuAccess;
import com.hmm.dms.repository.MenuGroupRepository;
import com.hmm.dms.repository.MenuItemRepository;
import com.hmm.dms.repository.RoleMenuAccessRepository;
import com.hmm.dms.service.RoleMenuAccessService;
import com.hmm.dms.service.dto.MenuGroupDTO;
import com.hmm.dms.service.dto.MenuItemDTO;
import com.hmm.dms.service.dto.RoleMenuAccessDTO;
import com.hmm.dms.service.mapper.MenuGroupMapper;
import com.hmm.dms.service.mapper.MenuItemMapper;
import com.hmm.dms.service.mapper.RoleMenuAccessMapper;
import com.hmm.dms.service.message.MenuGroupMessage;
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
        List<MenuItem> menuItemEntityList = this.menuItemRepository.findAll();
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
        List<MenuGroup> menuGroupEnityList = this.menuGroupRepository.findAllByRole(roleId);
        if (menuGroupEnityList != null && menuGroupEnityList.size() > 0) {
            menuGroupMessageList = new ArrayList<MenuGroupMessage>();
            for (MenuGroup data : menuGroupEnityList) {
                List<RoleMenuAccess> entityList = this.roleMenuAccessRepository.findAllByUserRoleAndMenuGroup(roleId, data.getId());
                List<RoleMenuAccessDTO> roleMenuAccessList = this.roleMenuAccessMapper.toDto(entityList);

                MenuGroupMessage message = new MenuGroupMessage();
                message.setId(data.getId());
                message.setName(data.getName());
                message.setTranslateKey(data.getTranslateKey());
                message.setOrderNo(data.getOrderNo());
                message.setSubMenuItems(roleMenuAccessList);

                menuGroupMessageList.add(message);
            }
        }

        return menuGroupMessageList;
    }
}
