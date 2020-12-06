package com.pop.fjournal.web.rest;

import com.pop.fjournal.FoodJournalApp;
import com.pop.fjournal.domain.Importer;
import com.pop.fjournal.repository.ImporterRepository;
import com.pop.fjournal.service.ImporterService;
import com.pop.fjournal.service.dto.ImporterDTO;
import com.pop.fjournal.service.mapper.ImporterMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.pop.fjournal.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ImporterResource} REST controller.
 */
@SpringBootTest(classes = FoodJournalApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ImporterResourceIT {

    private static final byte[] DEFAULT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_TYPE = "image/png";

    private static final ZonedDateTime DEFAULT_IMPORT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_IMPORT_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_SEPARATOR = "AAAAAAAAAA";
    private static final String UPDATED_SEPARATOR = "BBBBBBBBBB";

    @Autowired
    private ImporterRepository importerRepository;

    @Autowired
    private ImporterMapper importerMapper;

    @Autowired
    private ImporterService importerService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImporterMockMvc;

    private Importer importer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Importer createEntity(EntityManager em) {
        Importer importer = new Importer()
            .file(DEFAULT_FILE)
            .fileContentType(DEFAULT_FILE_CONTENT_TYPE)
            .importDate(DEFAULT_IMPORT_DATE)
            .separator(DEFAULT_SEPARATOR);
        return importer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Importer createUpdatedEntity(EntityManager em) {
        Importer importer = new Importer()
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .importDate(UPDATED_IMPORT_DATE)
            .separator(UPDATED_SEPARATOR);
        return importer;
    }

    @BeforeEach
    public void initTest() {
        importer = createEntity(em);
    }

    @Test
    @Transactional
    public void createImporter() throws Exception {
        int databaseSizeBeforeCreate = importerRepository.findAll().size();
        // Create the Importer
        ImporterDTO importerDTO = importerMapper.toDto(importer);
        restImporterMockMvc.perform(post("/api/importers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(importerDTO)))
            .andExpect(status().isCreated());

        // Validate the Importer in the database
        List<Importer> importerList = importerRepository.findAll();
        assertThat(importerList).hasSize(databaseSizeBeforeCreate + 1);
        Importer testImporter = importerList.get(importerList.size() - 1);
        assertThat(testImporter.getFile()).isEqualTo(DEFAULT_FILE);
        assertThat(testImporter.getFileContentType()).isEqualTo(DEFAULT_FILE_CONTENT_TYPE);
        assertThat(testImporter.getImportDate()).isEqualTo(DEFAULT_IMPORT_DATE);
        assertThat(testImporter.getSeparator()).isEqualTo(DEFAULT_SEPARATOR);
    }

    @Test
    @Transactional
    public void createImporterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = importerRepository.findAll().size();

        // Create the Importer with an existing ID
        importer.setId(1L);
        ImporterDTO importerDTO = importerMapper.toDto(importer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restImporterMockMvc.perform(post("/api/importers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(importerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Importer in the database
        List<Importer> importerList = importerRepository.findAll();
        assertThat(importerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkImportDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = importerRepository.findAll().size();
        // set the field null
        importer.setImportDate(null);

        // Create the Importer, which fails.
        ImporterDTO importerDTO = importerMapper.toDto(importer);


        restImporterMockMvc.perform(post("/api/importers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(importerDTO)))
            .andExpect(status().isBadRequest());

        List<Importer> importerList = importerRepository.findAll();
        assertThat(importerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllImporters() throws Exception {
        // Initialize the database
        importerRepository.saveAndFlush(importer);

        // Get all the importerList
        restImporterMockMvc.perform(get("/api/importers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(importer.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))))
            .andExpect(jsonPath("$.[*].importDate").value(hasItem(sameInstant(DEFAULT_IMPORT_DATE))))
            .andExpect(jsonPath("$.[*].separator").value(hasItem(DEFAULT_SEPARATOR)));
    }
    
    @Test
    @Transactional
    public void getImporter() throws Exception {
        // Initialize the database
        importerRepository.saveAndFlush(importer);

        // Get the importer
        restImporterMockMvc.perform(get("/api/importers/{id}", importer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(importer.getId().intValue()))
            .andExpect(jsonPath("$.fileContentType").value(DEFAULT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.file").value(Base64Utils.encodeToString(DEFAULT_FILE)))
            .andExpect(jsonPath("$.importDate").value(sameInstant(DEFAULT_IMPORT_DATE)))
            .andExpect(jsonPath("$.separator").value(DEFAULT_SEPARATOR));
    }
    @Test
    @Transactional
    public void getNonExistingImporter() throws Exception {
        // Get the importer
        restImporterMockMvc.perform(get("/api/importers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateImporter() throws Exception {
        // Initialize the database
        importerRepository.saveAndFlush(importer);

        int databaseSizeBeforeUpdate = importerRepository.findAll().size();

        // Update the importer
        Importer updatedImporter = importerRepository.findById(importer.getId()).get();
        // Disconnect from session so that the updates on updatedImporter are not directly saved in db
        em.detach(updatedImporter);
        updatedImporter
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .importDate(UPDATED_IMPORT_DATE)
            .separator(UPDATED_SEPARATOR);
        ImporterDTO importerDTO = importerMapper.toDto(updatedImporter);

        restImporterMockMvc.perform(put("/api/importers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(importerDTO)))
            .andExpect(status().isOk());

        // Validate the Importer in the database
        List<Importer> importerList = importerRepository.findAll();
        assertThat(importerList).hasSize(databaseSizeBeforeUpdate);
        Importer testImporter = importerList.get(importerList.size() - 1);
        assertThat(testImporter.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testImporter.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);
        assertThat(testImporter.getImportDate()).isEqualTo(UPDATED_IMPORT_DATE);
        assertThat(testImporter.getSeparator()).isEqualTo(UPDATED_SEPARATOR);
    }

    @Test
    @Transactional
    public void updateNonExistingImporter() throws Exception {
        int databaseSizeBeforeUpdate = importerRepository.findAll().size();

        // Create the Importer
        ImporterDTO importerDTO = importerMapper.toDto(importer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImporterMockMvc.perform(put("/api/importers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(importerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Importer in the database
        List<Importer> importerList = importerRepository.findAll();
        assertThat(importerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteImporter() throws Exception {
        // Initialize the database
        importerRepository.saveAndFlush(importer);

        int databaseSizeBeforeDelete = importerRepository.findAll().size();

        // Delete the importer
        restImporterMockMvc.perform(delete("/api/importers/{id}", importer.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Importer> importerList = importerRepository.findAll();
        assertThat(importerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
