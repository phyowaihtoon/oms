package creatip.oms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import creatip.oms.domain.CodeDefinition;
import creatip.oms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CodeDefinitionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CodeDefinition.class);
        CodeDefinition codeDefinition1 = new CodeDefinition();
        codeDefinition1.setId(1L);
        CodeDefinition codeDefinition2 = new CodeDefinition();
        codeDefinition2.setId(codeDefinition1.getId());
        assertThat(codeDefinition1).isEqualTo(codeDefinition2);
        codeDefinition2.setId(2L);
        assertThat(codeDefinition1).isNotEqualTo(codeDefinition2);
        codeDefinition1.setId(null);
        assertThat(codeDefinition1).isNotEqualTo(codeDefinition2);
    }
}
