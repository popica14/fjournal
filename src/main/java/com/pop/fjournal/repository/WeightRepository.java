package com.pop.fjournal.repository;

import com.pop.fjournal.domain.Weight;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Weight entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WeightRepository extends JpaRepository<Weight, Long> {

    @Query("select weight from Weight weight where weight.myWeigth.login = ?#{principal.username}")
    List<Weight> findByMyWeigthIsCurrentUser();
}
