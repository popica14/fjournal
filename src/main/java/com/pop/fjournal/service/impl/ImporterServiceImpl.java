package com.pop.fjournal.service.impl;

import com.pop.fjournal.service.ImporterService;
import com.pop.fjournal.domain.Importer;
import com.pop.fjournal.repository.ImporterRepository;
import com.pop.fjournal.service.MealService;
import com.pop.fjournal.service.WeightService;
import com.pop.fjournal.service.dto.ImporterDTO;
import com.pop.fjournal.service.dto.MealDTO;
import com.pop.fjournal.service.dto.WeightDTO;
import com.pop.fjournal.service.dto.utilDtos.ImporterEntry;
import com.pop.fjournal.service.mapper.ImporterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Importer}.
 */
@Service
@Transactional
public class ImporterServiceImpl implements ImporterService {

    private final Logger log = LoggerFactory.getLogger(ImporterServiceImpl.class);

    private final ImporterRepository importerRepository;

    private final ImporterMapper importerMapper;

    private final MealService mealsService;
    private WeightService weightsService;

    public ImporterServiceImpl(ImporterRepository importerRepository, ImporterMapper importerMapper,
                               MealService mealsService, WeightService weightsService) {
        this.importerRepository = importerRepository;
        this.importerMapper = importerMapper;
        this.mealsService = mealsService;
        this.weightsService = weightsService;
    }

    @Override
    public ImporterDTO save(ImporterDTO importerDTO) throws IOException {
        log.debug("Request to save Importer : {}", importerDTO);
        Importer importer = importerMapper.toEntity(importerDTO);

        saveImporterResults(importer);

        importer = importerRepository.save(importer);
        return importerMapper.toDto(importer);
    }

    private void saveImporterResults(Importer importer) throws IOException {
        log.debug("Request to save meals and weights for : {}", importer.getOwner());

        ImporterEntry mealsAndWeightsFromImporter = ImporterUtils.importJournal(importer);

        List<MealDTO> meals = mealsAndWeightsFromImporter.getMeals();
        List<WeightDTO> weights = mealsAndWeightsFromImporter.getWeights();

        for (MealDTO m : meals) {
            this.mealsService.save(m);
        }

        for (WeightDTO weight : weights) {
            this.weightsService.save(weight);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImporterDTO> findAll() {
        log.debug("Request to get all Importers");
        return importerRepository.findAll().stream()
            .map(importerMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ImporterDTO> findOne(Long id) {
        log.debug("Request to get Importer : {}", id);
        return importerRepository.findById(id)
            .map(importerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Importer : {}", id);
        importerRepository.deleteById(id);
    }
}
