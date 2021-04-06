package com.xcordia.document.repository;

import com.xcordia.document.domain.Lookup;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Lookup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LookupRepository extends JpaRepository<Lookup, Long>, JpaSpecificationExecutor<Lookup> {
}
