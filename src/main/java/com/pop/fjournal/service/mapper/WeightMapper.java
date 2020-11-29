package com.pop.fjournal.service.mapper;


import com.pop.fjournal.domain.*;
import com.pop.fjournal.service.dto.WeightDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Weight} and its DTO {@link WeightDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface WeightMapper extends EntityMapper<WeightDTO, Weight> {

    @Mapping(source = "myWeigth.id", target = "myWeigthId")
    @Mapping(source = "myWeigth.login", target = "myWeigthLogin")
    WeightDTO toDto(Weight weight);

    @Mapping(source = "myWeigthId", target = "myWeigth")
    Weight toEntity(WeightDTO weightDTO);

    default Weight fromId(Long id) {
        if (id == null) {
            return null;
        }
        Weight weight = new Weight();
        weight.setId(id);
        return weight;
    }
}
