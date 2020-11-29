package com.pop.fjournal.service;

import com.pop.fjournal.service.dto.WeightDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.pop.fjournal.domain.Weight}.
 */
public interface WeightService {

    /**
     * Save a weight.
     *
     * @param weightDTO the entity to save.
     * @return the persisted entity.
     */
    WeightDTO save(WeightDTO weightDTO);

    /**
     * Get all the weights.
     *
     * @return the list of entities.
     */
    List<WeightDTO> findAll();


    /**
     * Get the "id" weight.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WeightDTO> findOne(Long id);

    /**
     * Delete the "id" weight.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
