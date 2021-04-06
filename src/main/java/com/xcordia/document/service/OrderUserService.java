package com.xcordia.document.service;

import com.xcordia.document.domain.OrderUser;
import com.xcordia.document.repository.OrderUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link OrderUser}.
 */
@Service
@Transactional
public class OrderUserService {

    private final Logger log = LoggerFactory.getLogger(OrderUserService.class);

    private final OrderUserRepository orderUserRepository;

    public OrderUserService(OrderUserRepository orderUserRepository) {
        this.orderUserRepository = orderUserRepository;
    }

    /**
     * Save a orderUser.
     *
     * @param orderUser the entity to save.
     * @return the persisted entity.
     */
    public OrderUser save(OrderUser orderUser) {
        log.debug("Request to save OrderUser : {}", orderUser);
        return orderUserRepository.save(orderUser);
    }

    /**
     * Get all the orderUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderUser> findAll(Pageable pageable) {
        log.debug("Request to get all OrderUsers");
        return orderUserRepository.findAll(pageable);
    }


    /**
     * Get one orderUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderUser> findOne(Long id) {
        log.debug("Request to get OrderUser : {}", id);
        return orderUserRepository.findById(id);
    }

    /**
     * Delete the orderUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderUser : {}", id);

        orderUserRepository.deleteById(id);
    }
}
