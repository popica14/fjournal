package com.pop.fjournal.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class WeightMapperTest {

    private WeightMapper weightMapper;

    @BeforeEach
    public void setUp() {
        weightMapper = new WeightMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(weightMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(weightMapper.fromId(null)).isNull();
    }
}
