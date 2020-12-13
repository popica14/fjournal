package com.pop.fjournal.service.dto.utilDtos;

import java.util.List;

import com.pop.fjournal.service.dto.MealDTO;
import com.pop.fjournal.service.dto.WeightDTO;

public class ImporterEntry {

    List<MealDTO> meals;
    List<WeightDTO> weights;

    public ImporterEntry(List<MealDTO> meals, List<WeightDTO> weights) {
        this.meals = meals;
        this.weights = weights;
    }

    public List<MealDTO> getMeals() {
        return meals;
    }

    public void setMeals(List<MealDTO> meals) {
        this.meals = meals;
    }

    public List<WeightDTO> getWeights() {
        return weights;
    }

    public void setWeights(List<WeightDTO> weights) {
        this.weights = weights;
    }
}
