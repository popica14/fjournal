package com.pop.fjournal.service.mapper;


import com.pop.fjournal.domain.*;
import com.pop.fjournal.service.dto.ImporterDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Importer} and its DTO {@link ImporterDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ImporterMapper extends EntityMapper<ImporterDTO, Importer> {

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.login", target = "ownerLogin")
    @Mapping(source = "uploader.id", target = "uploaderId")
    @Mapping(source = "uploader.login", target = "uploaderLogin")
    ImporterDTO toDto(Importer importer);

    @Mapping(source = "ownerId", target = "owner")
    @Mapping(source = "uploaderId", target = "uploader")
    Importer toEntity(ImporterDTO importerDTO);

    default Importer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Importer importer = new Importer();
        importer.setId(id);
        return importer;
    }
}
