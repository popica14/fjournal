package com.pop.fjournal.web.rest;

import com.pop.fjournal.service.ImporterService;
import com.pop.fjournal.web.rest.errors.BadRequestAlertException;
import com.pop.fjournal.service.dto.ImporterDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.pop.fjournal.domain.Importer}.
 */
@RestController
@RequestMapping("/api")
public class ImporterResource {

    private final Logger log = LoggerFactory.getLogger(ImporterResource.class);

    private static final String ENTITY_NAME = "importer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImporterService importerService;

    public ImporterResource(ImporterService importerService) {
        this.importerService = importerService;
    }

    /**
     * {@code POST  /importers} : Create a new importer.
     *
     * @param importerDTO the importerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new importerDTO, or with status {@code 400 (Bad Request)} if the importer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/importers")
    public ResponseEntity<ImporterDTO> createImporter(@Valid @RequestBody ImporterDTO importerDTO) throws URISyntaxException,
        IOException {
        log.debug("REST request to save Importer : {}", importerDTO);
        if (importerDTO.getId() != null) {
            throw new BadRequestAlertException("A new importer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ImporterDTO result = importerService.save(importerDTO);
        return ResponseEntity.created(new URI("/api/importers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /importers} : Updates an existing importer.
     *
     * @param importerDTO the importerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated importerDTO,
     * or with status {@code 400 (Bad Request)} if the importerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the importerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/importers")
    public ResponseEntity<ImporterDTO> updateImporter(@Valid @RequestBody ImporterDTO importerDTO) throws URISyntaxException,
        IOException {
        log.debug("REST request to update Importer : {}", importerDTO);
        if (importerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ImporterDTO result = importerService.save(importerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, importerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /importers} : get all the importers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of importers in body.
     */
    @GetMapping("/importers")
    public List<ImporterDTO> getAllImporters() {
        log.debug("REST request to get all Importers");
        return importerService.findAll();
    }

    /**
     * {@code GET  /importers/:id} : get the "id" importer.
     *
     * @param id the id of the importerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the importerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/importers/{id}")
    public ResponseEntity<ImporterDTO> getImporter(@PathVariable Long id) {
        log.debug("REST request to get Importer : {}", id);
        Optional<ImporterDTO> importerDTO = importerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(importerDTO);
    }

    /**
     * {@code DELETE  /importers/:id} : delete the "id" importer.
     *
     * @param id the id of the importerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/importers/{id}")
    public ResponseEntity<Void> deleteImporter(@PathVariable Long id) {
        log.debug("REST request to delete Importer : {}", id);
        importerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
