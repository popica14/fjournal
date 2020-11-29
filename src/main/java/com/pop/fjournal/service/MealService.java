package com.pop.fjournal.service;

import com.pop.fjournal.service.dto.MealDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.pop.fjournal.domain.Meal}.
 */
public interface MealService {

    /**
     * Save a meal.
     *
     * @param mealDTO the entity to save.
     * @return the persisted entity.
     */
    MealDTO save(MealDTO mealDTO);

    /**
     * Get all the meals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MealDTO> findAll(Pageable pageable);


    /**
     * Get the "id" meal.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MealDTO> findOne(Long id);

    /**
     * Delete the "id" meal.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
