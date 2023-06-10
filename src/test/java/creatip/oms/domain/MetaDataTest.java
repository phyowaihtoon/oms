package creatip.oms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import creatip.oms.domain.MetaData;
import creatip.oms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MetaDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetaData.class);
        MetaData metaData1 = new MetaData();
        metaData1.setId(1L);
        MetaData metaData2 = new MetaData();
        metaData2.setId(metaData1.getId());
        assertThat(metaData1).isEqualTo(metaData2);
        metaData2.setId(2L);
        assertThat(metaData1).isNotEqualTo(metaData2);
        metaData1.setId(null);
        assertThat(metaData1).isNotEqualTo(metaData2);
    }
}
