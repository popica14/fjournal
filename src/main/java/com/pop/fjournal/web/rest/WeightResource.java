package com.pop.fjournal.web.rest;

import com.pop.fjournal.service.WeightService;
import com.pop.fjournal.web.rest.errors.BadRequestAlertException;
import com.pop.fjournal.service.dto.WeightDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.pop.fjournal.domain.Weight}.
 */
@RestController
@RequestMapping("/api")
public class WeightResource {

    private final Logger log = LoggerFactory.getLogger(WeightResource.class);

    private static final String ENTITY_NAME = "weight";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WeightService weightService;

    public WeightResource(WeightService weightService) {
        this.weightService = weightService;
    }

    /**
     * {@code POST  /weights} : Create a new weight.
     *
     * @param weightDTO the weightDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new weightDTO, or with status {@code 400 (Bad Request)} if the weight has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/weights")
    public ResponseEntity<WeightDTO> createWeight(@Valid @RequestBody WeightDTO weightDTO) throws URISyntaxException {
        log.debug("REST request to save Weight : {}", weightDTO);
        if (weightDTO.getId() != null) {
            throw new BadRequestAlertException("A new weight cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WeightDTO result = weightService.save(weightDTO);
        return ResponseEntity.created(new URI("/api/weights/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /weights} : Updates an existing weight.
     *
     * @param weightDTO the weightDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated weightDTO,
     * or with status {@code 400 (Bad Request)} if the weightDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the weightDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/weights")
    public ResponseEntity<WeightDTO> updateWeight(@Valid @RequestBody WeightDTO weightDTO) throws URISyntaxException {
        log.debug("REST request to update Weight : {}", weightDTO);
        if (weightDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WeightDTO result = weightService.save(weightDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, weightDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /weights} : get all the weights.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of weights in body.
     */
    @GetMapping("/weights")
    public List<WeightDTO> getAllWeights() {
        log.debug("REST request to get all Weights");
        return weightService.findAll();
    }

    /**
     * {@code GET  /weights/:id} : get the "id" weight.
     *
     * @param id the id of the weightDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the weightDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/weights/{id}")
    public ResponseEntity<WeightDTO> getWeight(@PathVariable Long id) {
        log.debug("REST request to get Weight : {}", id);
        Optional<WeightDTO> weightDTO = weightService.findOne(id);
        return ResponseUtil.wrapOrNotFound(weightDTO);
    }

    /**
     * {@code DELETE  /weights/:id} : delete the "id" weight.
     *
     * @param id the id of the weightDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/weights/{id}")
    public ResponseEntity<Void> deleteWeight(@PathVariable Long id) {
        log.debug("REST request to delete Weight : {}", id);
        weightService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
