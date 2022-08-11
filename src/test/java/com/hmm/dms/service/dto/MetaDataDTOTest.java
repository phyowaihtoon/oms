package com.hmm.dms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hmm.dms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MetaDataDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetaDataDTO.class);
        MetaDataDTO metaDataDTO1 = new MetaDataDTO();
        metaDataDTO1.setId(1L);
        MetaDataDTO metaDataDTO2 = new MetaDataDTO();
        assertThat(metaDataDTO1).isNotEqualTo(metaDataDTO2);
        metaDataDTO2.setId(metaDataDTO1.getId());
        assertThat(metaDataDTO1).isEqualTo(metaDataDTO2);
        metaDataDTO2.setId(2L);
        assertThat(metaDataDTO1).isNotEqualTo(metaDataDTO2);
        metaDataDTO1.setId(null);
        assertThat(metaDataDTO1).isNotEqualTo(metaDataDTO2);
    }
}
