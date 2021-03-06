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

import com.xcordia.document.domain.OrderItem;
import com.xcordia.document.domain.*; // for static metamodels
import com.xcordia.document.repository.OrderItemRepository;
import com.xcordia.document.service.dto.OrderItemCriteria;

/**
 * Service for executing complex queries for {@link OrderItem} entities in the database.
 * The main input is a {@link OrderItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrderItem} or a {@link Page} of {@link OrderItem} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderItemQueryService extends QueryService<OrderItem> {

    private final Logger log = LoggerFactory.getLogger(OrderItemQueryService.class);

    private final OrderItemRepository orderItemRepository;

    public OrderItemQueryService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    /**
     * Return a {@link List} of {@link OrderItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrderItem> findByCriteria(OrderItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrderItem> specification = createSpecification(criteria);
        return orderItemRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link OrderItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderItem> findByCriteria(OrderItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrderItem> specification = createSpecification(criteria);
        return orderItemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OrderItem> specification = createSpecification(criteria);
        return orderItemRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrderItem> createSpecification(OrderItemCriteria criteria) {
        Specification<OrderItem> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrderItem_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), OrderItem_.name));
            }
            if (criteria.getDateCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCreated(), OrderItem_.dateCreated));
            }
            if (criteria.getDateUpdated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateUpdated(), OrderItem_.dateUpdated));
            }
            if (criteria.getCatalogItemId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCatalogItemId(), OrderItem_.catalogItemId));
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(buildSpecification(criteria.getOrderId(),
                    root -> root.join(OrderItem_.order, JoinType.LEFT).get(Order_.id)));
            }
        }
        return specification;
    }
}
