package com.pop.fjournal.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.pop.fjournal.web.rest.TestUtil;

public class ImporterDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImporterDTO.class);
        ImporterDTO importerDTO1 = new ImporterDTO();
        importerDTO1.setId(1L);
        ImporterDTO importerDTO2 = new ImporterDTO();
        assertThat(importerDTO1).isNotEqualTo(importerDTO2);
        importerDTO2.setId(importerDTO1.getId());
        assertThat(importerDTO1).isEqualTo(importerDTO2);
        importerDTO2.setId(2L);
        assertThat(importerDTO1).isNotEqualTo(importerDTO2);
        importerDTO1.setId(null);
        assertThat(importerDTO1).isNotEqualTo(importerDTO2);
    }
}
