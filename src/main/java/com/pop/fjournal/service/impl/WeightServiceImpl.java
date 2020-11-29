package com.pop.fjournal.service.impl;

import com.pop.fjournal.service.WeightService;
import com.pop.fjournal.domain.Weight;
import com.pop.fjournal.repository.WeightRepository;
import com.pop.fjournal.service.dto.WeightDTO;
import com.pop.fjournal.service.mapper.WeightMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Weight}.
 */
@Service
@Transactional
public class WeightServiceImpl implements WeightService {

    private final Logger log = LoggerFactory.getLogger(WeightServiceImpl.class);

    private final WeightRepository weightRepository;

    private final WeightMapper weightMapper;

    public WeightServiceImpl(WeightRepository weightRepository, WeightMapper weightMapper) {
        this.weightRepository = weightRepository;
        this.weightMapper = weightMapper;
    }

    @Override
    public WeightDTO save(WeightDTO weightDTO) {
        log.debug("Request to save Weight : {}", weightDTO);
        Weight weight = weightMapper.toEntity(weightDTO);
        weight = weightRepository.save(weight);
        return weightMapper.toDto(weight);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WeightDTO> findAll() {
        log.debug("Request to get all Weights");
        return weightRepository.findAll().stream()
            .map(weightMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<WeightDTO> findOne(Long id) {
        log.debug("Request to get Weight : {}", id);
        return weightRepository.findById(id)
            .map(weightMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Weight : {}", id);
        weightRepository.deleteById(id);
    }
}
