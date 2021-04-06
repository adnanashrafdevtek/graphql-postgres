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

import com.xcordia.document.domain.Order;
import com.xcordia.document.domain.*; // for static metamodels
import com.xcordia.document.repository.OrderRepository;
import com.xcordia.document.service.dto.OrderCriteria;

/**
 * Service for executing complex queries for {@link Order} entities in the database.
 * The main input is a {@link OrderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Order} or a {@link Page} of {@link Order} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrderQueryService extends QueryService<Order> {

    private final Logger log = LoggerFactory.getLogger(OrderQueryService.class);

    private final OrderRepository orderRepository;

    public OrderQueryService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Return a {@link List} of {@link Order} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Order> findByCriteria(OrderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Order> specification = createSpecification(criteria);
        return orderRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Order} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Order> findByCriteria(OrderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Order> specification = createSpecification(criteria);
        return orderRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Order> specification = createSpecification(criteria);
        return orderRepository.count(specification);
    }

    /**
     * Function to convert {@link OrderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Order> createSpecification(OrderCriteria criteria) {
        Specification<Order> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Order_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Order_.name));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUuid(), Order_.uuid));
            }
            if (criteria.getDateCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateCreated(), Order_.dateCreated));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Order_.createdBy));
            }
            if (criteria.getDateUpdated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateUpdated(), Order_.dateUpdated));
            }
            if (criteria.getUpdatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUpdatedBy(), Order_.updatedBy));
            }
            if (criteria.getBuyerUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBuyerUserId(), Order_.buyerUserId));
            }
            if (criteria.getBuyerOrganizationId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBuyerOrganizationId(), Order_.buyerOrganizationId));
            }
            if (criteria.getSupplierOrganizationId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSupplierOrganizationId(), Order_.supplierOrganizationId));
            }
            if (criteria.getPrimarySupplierUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrimarySupplierUserId(), Order_.primarySupplierUserId));
            }
            if (criteria.getOrderUsersId() != null) {
                specification = specification.and(buildSpecification(criteria.getOrderUsersId(),
                    root -> root.join(Order_.orderUsers, JoinType.LEFT).get(OrderUser_.id)));
            }
            if (criteria.getMessagesId() != null) {
                specification = specification.and(buildSpecification(criteria.getMessagesId(),
                    root -> root.join(Order_.messages, JoinType.LEFT).get(Message_.id)));
            }
            if (criteria.getOrderItemsId() != null) {
                specification = specification.and(buildSpecification(criteria.getOrderItemsId(),
                    root -> root.join(Order_.orderItems, JoinType.LEFT).get(OrderItem_.id)));
            }
        }
        return specification;
    }
}
