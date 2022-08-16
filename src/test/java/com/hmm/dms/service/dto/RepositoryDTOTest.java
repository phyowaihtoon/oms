package com.hmm.dms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hmm.dms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RepositoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RepositoryDocDTO.class);
        RepositoryDocDTO repositoryDTO1 = new RepositoryDocDTO();
        repositoryDTO1.setId(1L);
        RepositoryDocDTO repositoryDTO2 = new RepositoryDocDTO();
        assertThat(repositoryDTO1).isNotEqualTo(repositoryDTO2);
        repositoryDTO2.setId(repositoryDTO1.getId());
        assertThat(repositoryDTO1).isEqualTo(repositoryDTO2);
        repositoryDTO2.setId(2L);
        assertThat(repositoryDTO1).isNotEqualTo(repositoryDTO2);
        repositoryDTO1.setId(null);
        assertThat(repositoryDTO1).isNotEqualTo(repositoryDTO2);
    }
}
