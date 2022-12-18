package com.hmm.dms.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CodeDefinitionMapperTest {

    private CodeDefinitionMapper codeDefinitionMapper;

    @BeforeEach
    public void setUp() {
        codeDefinitionMapper = new CodeDefinitionMapperImpl();
    }
}
