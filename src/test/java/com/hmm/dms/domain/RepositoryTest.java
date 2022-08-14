package com.hmm.dms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hmm.dms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RepositoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Repository.class);
        Repository repository1 = new Repository();
        repository1.setId(1L);
        Repository repository2 = new Repository();
        repository2.setId(repository1.getId());
        assertThat(repository1).isEqualTo(repository2);
        repository2.setId(2L);
        assertThat(repository1).isNotEqualTo(repository2);
        repository1.setId(null);
        assertThat(repository1).isNotEqualTo(repository2);
    }
}
