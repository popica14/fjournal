package com.pop.fjournal.service;

import com.pop.fjournal.service.dto.ImporterDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.pop.fjournal.domain.Importer}.
 */
public interface ImporterService {

    /**
     * Save a importer.
     *
     * @param importerDTO the entity to save.
     * @return the persisted entity.
     */
    ImporterDTO save(ImporterDTO importerDTO);

    /**
     * Get all the importers.
     *
     * @return the list of entities.
     */
    List<ImporterDTO> findAll();


    /**
     * Get the "id" importer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ImporterDTO> findOne(Long id);

    /**
     * Delete the "id" importer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
