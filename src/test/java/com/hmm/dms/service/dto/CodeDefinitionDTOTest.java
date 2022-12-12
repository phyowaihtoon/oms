package com.hmm.dms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hmm.dms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CodeDefinitionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CodeDefinitionDTO.class);
        CodeDefinitionDTO codeDefinitionDTO1 = new CodeDefinitionDTO();
        codeDefinitionDTO1.setId(1L);
        CodeDefinitionDTO codeDefinitionDTO2 = new CodeDefinitionDTO();
        assertThat(codeDefinitionDTO1).isNotEqualTo(codeDefinitionDTO2);
        codeDefinitionDTO2.setId(codeDefinitionDTO1.getId());
        assertThat(codeDefinitionDTO1).isEqualTo(codeDefinitionDTO2);
        codeDefinitionDTO2.setId(2L);
        assertThat(codeDefinitionDTO1).isNotEqualTo(codeDefinitionDTO2);
        codeDefinitionDTO1.setId(null);
        assertThat(codeDefinitionDTO1).isNotEqualTo(codeDefinitionDTO2);
    }
}
