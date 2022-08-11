package com.hmm.dms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hmm.dms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MetaDataHeaderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetaDataHeader.class);
        MetaDataHeader metaDataHeader1 = new MetaDataHeader();
        metaDataHeader1.setId(1L);
        MetaDataHeader metaDataHeader2 = new MetaDataHeader();
        metaDataHeader2.setId(metaDataHeader1.getId());
        assertThat(metaDataHeader1).isEqualTo(metaDataHeader2);
        metaDataHeader2.setId(2L);
        assertThat(metaDataHeader1).isNotEqualTo(metaDataHeader2);
        metaDataHeader1.setId(null);
        assertThat(metaDataHeader1).isNotEqualTo(metaDataHeader2);
    }
}
