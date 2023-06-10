package creatip.oms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import creatip.oms.domain.DocumentHeader;
import creatip.oms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentHeaderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentHeader.class);
        DocumentHeader documentHeader1 = new DocumentHeader();
        documentHeader1.setId(1L);
        DocumentHeader documentHeader2 = new DocumentHeader();
        documentHeader2.setId(documentHeader1.getId());
        assertThat(documentHeader1).isEqualTo(documentHeader2);
        documentHeader2.setId(2L);
        assertThat(documentHeader1).isNotEqualTo(documentHeader2);
        documentHeader1.setId(null);
        assertThat(documentHeader1).isNotEqualTo(documentHeader2);
    }
}
