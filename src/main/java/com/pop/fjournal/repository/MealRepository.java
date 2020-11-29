package com.pop.fjournal.repository;

import com.pop.fjournal.domain.Meal;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Meal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    @Query("select meal from Meal meal where meal.myMeal.login = ?#{principal.username}")
    List<Meal> findByMyMealIsCurrentUser();
}
