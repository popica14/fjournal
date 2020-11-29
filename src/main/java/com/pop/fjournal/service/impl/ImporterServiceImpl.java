package com.pop.fjournal.service.impl;

import com.pop.fjournal.service.ImporterService;
import com.pop.fjournal.domain.Importer;
import com.pop.fjournal.repository.ImporterRepository;
import com.pop.fjournal.service.dto.ImporterDTO;
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

    public ImporterServiceImpl(ImporterRepository importerRepository, ImporterMapper importerMapper) {
        this.importerRepository = importerRepository;
        this.importerMapper = importerMapper;
    }

    @Override
    public ImporterDTO save(ImporterDTO importerDTO) throws IOException {
        log.debug("Request to save Importer : {}", importerDTO);
        Importer importer = importerMapper.toEntity(importerDTO);
        ImporterUtils.importJournal(importer);
        importer = importerRepository.save(importer);
        return importerMapper.toDto(importer);
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
