package com.pop.fjournal.service.mapper;


import com.pop.fjournal.domain.*;
import com.pop.fjournal.service.dto.MealDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Meal} and its DTO {@link MealDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface MealMapper extends EntityMapper<MealDTO, Meal> {

    @Mapping(source = "myMeal.id", target = "myMealId")
    @Mapping(source = "myMeal.login", target = "myMealLogin")
    MealDTO toDto(Meal meal);

    @Mapping(source = "myMealId", target = "myMeal")
    Meal toEntity(MealDTO mealDTO);

    default Meal fromId(Long id) {
        if (id == null) {
            return null;
        }
        Meal meal = new Meal();
        meal.setId(id);
        return meal;
    }
}
