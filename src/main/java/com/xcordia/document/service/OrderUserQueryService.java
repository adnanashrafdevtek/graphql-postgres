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

import com.xcordia.document.domain.OrderUser;
import com.xcordia.document.domain.*; // for static metamodels
import com.xcordia.document.repository.OrderUserRepository;
import com.xcordia.document.service.dto.OrderUserCriteria;

/**
 * Service for executing complex queries for {@link OrderUser} entities in the database.
 * The main input is a {@link OrderUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrderUser} or a {@link Page} of {@link OrderUser} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderUserQueryService extends QueryService<OrderUser> {

    private final Logger log = LoggerFactory.getLogger(OrderUserQueryService.class);

    private final OrderUserRepository orderUserRepository;

    public OrderUserQueryService(OrderUserRepository orderUserRepository) {
        this.orderUserRepository = orderUserRepository;
    }

    /**
     * Return a {@link List} of {@link OrderUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrderUser> findByCriteria(OrderUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrderUser> specification = createSpecification(criteria);
        return orderUserRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link OrderUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderUser> findByCriteria(OrderUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderUser> specification = createSpecification(criteria);
        return orderUserRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OrderUser> specification = createSpecification(criteria);
        return orderUserRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderUser> createSpecification(OrderUserCriteria criteria) {
        Specification<OrderUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrderUser_.id));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), OrderUser_.userId));
            }
            if (criteria.getDateCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCreated(), OrderUser_.dateCreated));
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(buildSpecification(criteria.getOrderId(),
                    root -> root.join(OrderUser_.order, JoinType.LEFT).get(Order_.id)));
            }
        }
        return specification;
    }
}
