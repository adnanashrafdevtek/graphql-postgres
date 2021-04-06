package com.xcordia.document.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.xcordia.document.domain.LookupValue;
import com.xcordia.document.domain.*; // for static metamodels
import com.xcordia.document.repository.LookupValueRepository;
import com.xcordia.document.service.dto.LookupValueCriteria;

/**
 * Service for executing complex queries for {@link LookupValue} entities in the database.
 * The main input is a {@link LookupValueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LookupValue} or a {@link Page} of {@link LookupValue} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LookupValueQueryService extends QueryService<LookupValue> {

    private final Logger log = LoggerFactory.getLogger(LookupValueQueryService.class);

    private final LookupValueRepository lookupValueRepository;

    public LookupValueQueryService(LookupValueRepository lookupValueRepository) {
        this.lookupValueRepository = lookupValueRepository;
    }

    /**
     * Return a {@link List} of {@link LookupValue} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LookupValue> findByCriteria(LookupValueCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LookupValue> specification = createSpecification(criteria);
        return lookupValueRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link LookupValue} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LookupValue> findByCriteria(LookupValueCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LookupValue> specification = createSpecification(criteria);
        return lookupValueRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LookupValueCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LookupValue> specification = createSpecification(criteria);
        return lookupValueRepository.count(specification);
    }

    /**
     * Function to convert {@link LookupValueCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LookupValue> createSpecification(LookupValueCriteria criteria) {
        Specification<LookupValue> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LookupValue_.id));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), LookupValue_.value));
            }
            if (criteria.getLookupId() != null) {
                specification = specification.and(buildSpecification(criteria.getLookupId(),
                    root -> root.join(LookupValue_.lookup, JoinType.LEFT).get(Lookup_.id)));
            }
        }
        return specification;
    }
}
