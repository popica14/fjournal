package com.pop.fjournal.service.impl;

import com.pop.fjournal.service.MealService;
import com.pop.fjournal.domain.Meal;
import com.pop.fjournal.repository.MealRepository;
import com.pop.fjournal.service.dto.MealDTO;
import com.pop.fjournal.service.mapper.MealMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Meal}.
 */
@Service
@Transactional
public class MealServiceImpl implements MealService {

    private final Logger log = LoggerFactory.getLogger(MealServiceImpl.class);

    private final MealRepository mealRepository;

    private final MealMapper mealMapper;

    public MealServiceImpl(MealRepository mealRepository, MealMapper mealMapper) {
        this.mealRepository = mealRepository;
        this.mealMapper = mealMapper;
    }

    @Override
    public MealDTO save(MealDTO mealDTO) {
        log.debug("Request to save Meal : {}", mealDTO);
        Meal meal = mealMapper.toEntity(mealDTO);
        meal = mealRepository.save(meal);
        return mealMapper.toDto(meal);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MealDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Meals");
        return mealRepository.findAll(pageable)
            .map(mealMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<MealDTO> findOne(Long id) {
        log.debug("Request to get Meal : {}", id);
        return mealRepository.findById(id)
            .map(mealMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Meal : {}", id);
        mealRepository.deleteById(id);
    }
}
