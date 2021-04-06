package com.xcordia.document.repository;

import com.xcordia.document.domain.LookupValue;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the LookupValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LookupValueRepository extends JpaRepository<LookupValue, Long>, JpaSpecificationExecutor<LookupValue> {
}
