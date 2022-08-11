package com.hmm.dms.service.impl;

import com.hmm.dms.domain.RoleMenuMap;
import com.hmm.dms.repository.RoleMenuMapRepository;
import com.hmm.dms.service.RoleMenuMapService;
import com.hmm.dms.service.dto.RoleMenuMapDTO;
import com.hmm.dms.service.mapper.RoleMenuMapMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RoleMenuMap}.
 */
@Service
@Transactional
public class RoleMenuMapServiceImpl implements RoleMenuMapService {

    private final Logger log = LoggerFactory.getLogger(RoleMenuMapServiceImpl.class);

    private final RoleMenuMapRepository roleMenuMapRepository;

    private final RoleMenuMapMapper roleMenuMapMapper;

    public RoleMenuMapServiceImpl(RoleMenuMapRepository roleMenuMapRepository, RoleMenuMapMapper roleMenuMapMapper) {
        this.roleMenuMapRepository = roleMenuMapRepository;
        this.roleMenuMapMapper = roleMenuMapMapper;
    }

    @Override
    public RoleMenuMapDTO save(RoleMenuMapDTO roleMenuMapDTO) {
        log.debug("Request to save RoleMenuMap : {}", roleMenuMapDTO);
        RoleMenuMap roleMenuMap = roleMenuMapMapper.toEntity(roleMenuMapDTO);
        roleMenuMap = roleMenuMapRepository.save(roleMenuMap);
        return roleMenuMapMapper.toDto(roleMenuMap);
    }

    @Override
    public Optional<RoleMenuMapDTO> partialUpdate(RoleMenuMapDTO roleMenuMapDTO) {
        log.debug("Request to partially update RoleMenuMap : {}", roleMenuMapDTO);

        return roleMenuMapRepository
            .findById(roleMenuMapDTO.getId())
            .map(
                existingRoleMenuMap -> {
                    roleMenuMapMapper.partialUpdate(existingRoleMenuMap, roleMenuMapDTO);
                    return existingRoleMenuMap;
                }
            )
            .map(roleMenuMapRepository::save)
            .map(roleMenuMapMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleMenuMapDTO> findAll() {
        log.debug("Request to get all RoleMenuMaps");
        return roleMenuMapRepository.findAll().stream().map(roleMenuMapMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoleMenuMapDTO> findOne(Long id) {
        log.debug("Request to get RoleMenuMap : {}", id);
        return roleMenuMapRepository.findById(id).map(roleMenuMapMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RoleMenuMap : {}", id);
        roleMenuMapRepository.deleteById(id);
    }
}
