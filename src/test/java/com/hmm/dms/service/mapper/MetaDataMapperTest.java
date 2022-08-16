package com.hmm.dms.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MetaDataMapperTest {

    private MetaDataMapper metaDataMapper;

    @BeforeEach
    public void setUp() {
        metaDataMapper = new MetaDataMapperImpl();
    }
}
