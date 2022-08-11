package com.hmm.dms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hmm.dms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoleMenuMapDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoleMenuMapDTO.class);
        RoleMenuMapDTO roleMenuMapDTO1 = new RoleMenuMapDTO();
        roleMenuMapDTO1.setId(1L);
        RoleMenuMapDTO roleMenuMapDTO2 = new RoleMenuMapDTO();
        assertThat(roleMenuMapDTO1).isNotEqualTo(roleMenuMapDTO2);
        roleMenuMapDTO2.setId(roleMenuMapDTO1.getId());
        assertThat(roleMenuMapDTO1).isEqualTo(roleMenuMapDTO2);
        roleMenuMapDTO2.setId(2L);
        assertThat(roleMenuMapDTO1).isNotEqualTo(roleMenuMapDTO2);
        roleMenuMapDTO1.setId(null);
        assertThat(roleMenuMapDTO1).isNotEqualTo(roleMenuMapDTO2);
    }
}
