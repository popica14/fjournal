package com.pop.fjournal.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.pop.fjournal.web.rest.TestUtil;

public class ImporterTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Importer.class);
        Importer importer1 = new Importer();
        importer1.setId(1L);
        Importer importer2 = new Importer();
        importer2.setId(importer1.getId());
        assertThat(importer1).isEqualTo(importer2);
        importer2.setId(2L);
        assertThat(importer1).isNotEqualTo(importer2);
        importer1.setId(null);
        assertThat(importer1).isNotEqualTo(importer2);
    }
}
