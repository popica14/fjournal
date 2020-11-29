package com.pop.fjournal.repository;

import com.pop.fjournal.domain.Importer;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Importer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImporterRepository extends JpaRepository<Importer, Long> {

    @Query("select importer from Importer importer where importer.owner.login = ?#{principal.username}")
    List<Importer> findByOwnerIsCurrentUser();

    @Query("select importer from Importer importer where importer.uploader.login = ?#{principal.username}")
    List<Importer> findByUploaderIsCurrentUser();
}
