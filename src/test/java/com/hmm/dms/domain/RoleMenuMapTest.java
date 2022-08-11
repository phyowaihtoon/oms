package com.hmm.dms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hmm.dms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoleMenuMapTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoleMenuMap.class);
        RoleMenuMap roleMenuMap1 = new RoleMenuMap();
        roleMenuMap1.setId(1L);
        RoleMenuMap roleMenuMap2 = new RoleMenuMap();
        roleMenuMap2.setId(roleMenuMap1.getId());
        assertThat(roleMenuMap1).isEqualTo(roleMenuMap2);
        roleMenuMap2.setId(2L);
        assertThat(roleMenuMap1).isNotEqualTo(roleMenuMap2);
        roleMenuMap1.setId(null);
        assertThat(roleMenuMap1).isNotEqualTo(roleMenuMap2);
    }
}
