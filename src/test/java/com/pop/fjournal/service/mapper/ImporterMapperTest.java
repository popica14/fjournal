package com.pop.fjournal.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ImporterMapperTest {

    private ImporterMapper importerMapper;

    @BeforeEach
    public void setUp() {
        importerMapper = new ImporterMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(importerMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(importerMapper.fromId(null)).isNull();
    }
}
