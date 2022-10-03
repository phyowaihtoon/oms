package com.hmm.dms.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserRoleMapperTest {

    private UserRoleMapper userRoleMapper;

    @BeforeEach
    public void setUp() {
        userRoleMapper = new UserRoleMapperImpl();
    }
}
