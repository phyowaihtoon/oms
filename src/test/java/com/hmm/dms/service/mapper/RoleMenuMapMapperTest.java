package com.hmm.dms.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleMenuMapMapperTest {

    private RoleMenuMapMapper roleMenuMapMapper;

    @BeforeEach
    public void setUp() {
        roleMenuMapMapper = new RoleMenuMapMapperImpl();
    }
}
