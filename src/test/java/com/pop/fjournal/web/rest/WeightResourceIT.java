package com.pop.fjournal.web.rest;

import com.pop.fjournal.FoodJournalApp;
import com.pop.fjournal.domain.Weight;
import com.pop.fjournal.repository.WeightRepository;
import com.pop.fjournal.service.WeightService;
import com.pop.fjournal.service.dto.WeightDTO;
import com.pop.fjournal.service.mapper.WeightMapper;

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
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link WeightResource} REST controller.
 */
@SpringBootTest(classes = FoodJournalApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class WeightResourceIT {

    private static final Float DEFAULT_VALUE = 40F;
    private static final Float UPDATED_VALUE = 41F;

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    @Autowired
    private WeightRepository weightRepository;

    @Autowired
    private WeightMapper weightMapper;

    @Autowired
    private WeightService weightService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWeightMockMvc;

    private Weight weight;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Weight createEntity(EntityManager em) {
        Weight weight = new Weight()
            .value(DEFAULT_VALUE)
            .date(DEFAULT_DATE)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE)
            .observation(DEFAULT_OBSERVATION);
        return weight;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Weight createUpdatedEntity(EntityManager em) {
        Weight weight = new Weight()
            .value(UPDATED_VALUE)
            .date(UPDATED_DATE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .observation(UPDATED_OBSERVATION);
        return weight;
    }

    @BeforeEach
    public void initTest() {
        weight = createEntity(em);
    }

    @Test
    @Transactional
    public void createWeight() throws Exception {
        int databaseSizeBeforeCreate = weightRepository.findAll().size();
        // Create the Weight
        WeightDTO weightDTO = weightMapper.toDto(weight);
        restWeightMockMvc.perform(post("/api/weights")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(weightDTO)))
            .andExpect(status().isCreated());

        // Validate the Weight in the database
        List<Weight> weightList = weightRepository.findAll();
        assertThat(weightList).hasSize(databaseSizeBeforeCreate + 1);
        Weight testWeight = weightList.get(weightList.size() - 1);
        assertThat(testWeight.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testWeight.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testWeight.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testWeight.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
        assertThat(testWeight.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
    }

    @Test
    @Transactional
    public void createWeightWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = weightRepository.findAll().size();

        // Create the Weight with an existing ID
        weight.setId(1L);
        WeightDTO weightDTO = weightMapper.toDto(weight);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWeightMockMvc.perform(post("/api/weights")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(weightDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Weight in the database
        List<Weight> weightList = weightRepository.findAll();
        assertThat(weightList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = weightRepository.findAll().size();
        // set the field null
        weight.setValue(null);

        // Create the Weight, which fails.
        WeightDTO weightDTO = weightMapper.toDto(weight);


        restWeightMockMvc.perform(post("/api/weights")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(weightDTO)))
            .andExpect(status().isBadRequest());

        List<Weight> weightList = weightRepository.findAll();
        assertThat(weightList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWeights() throws Exception {
        // Initialize the database
        weightRepository.saveAndFlush(weight);

        // Get all the weightList
        restWeightMockMvc.perform(get("/api/weights?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weight.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)));
    }
    
    @Test
    @Transactional
    public void getWeight() throws Exception {
        // Initialize the database
        weightRepository.saveAndFlush(weight);

        // Get the weight
        restWeightMockMvc.perform(get("/api/weights/{id}", weight.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(weight.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64Utils.encodeToString(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION));
    }
    @Test
    @Transactional
    public void getNonExistingWeight() throws Exception {
        // Get the weight
        restWeightMockMvc.perform(get("/api/weights/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWeight() throws Exception {
        // Initialize the database
        weightRepository.saveAndFlush(weight);

        int databaseSizeBeforeUpdate = weightRepository.findAll().size();

        // Update the weight
        Weight updatedWeight = weightRepository.findById(weight.getId()).get();
        // Disconnect from session so that the updates on updatedWeight are not directly saved in db
        em.detach(updatedWeight);
        updatedWeight
            .value(UPDATED_VALUE)
            .date(UPDATED_DATE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .observation(UPDATED_OBSERVATION);
        WeightDTO weightDTO = weightMapper.toDto(updatedWeight);

        restWeightMockMvc.perform(put("/api/weights")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(weightDTO)))
            .andExpect(status().isOk());

        // Validate the Weight in the database
        List<Weight> weightList = weightRepository.findAll();
        assertThat(weightList).hasSize(databaseSizeBeforeUpdate);
        Weight testWeight = weightList.get(weightList.size() - 1);
        assertThat(testWeight.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testWeight.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testWeight.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testWeight.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testWeight.getObservation()).isEqualTo(UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    public void updateNonExistingWeight() throws Exception {
        int databaseSizeBeforeUpdate = weightRepository.findAll().size();

        // Create the Weight
        WeightDTO weightDTO = weightMapper.toDto(weight);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeightMockMvc.perform(put("/api/weights")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(weightDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Weight in the database
        List<Weight> weightList = weightRepository.findAll();
        assertThat(weightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWeight() throws Exception {
        // Initialize the database
        weightRepository.saveAndFlush(weight);

        int databaseSizeBeforeDelete = weightRepository.findAll().size();

        // Delete the weight
        restWeightMockMvc.perform(delete("/api/weights/{id}", weight.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Weight> weightList = weightRepository.findAll();
        assertThat(weightList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
