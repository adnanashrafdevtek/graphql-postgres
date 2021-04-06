package com.xcordia.document.web.rest;

import com.xcordia.document.domain.OrderUser;
import com.xcordia.document.service.OrderUserService;
import com.xcordia.document.web.rest.errors.BadRequestAlertException;
import com.xcordia.document.service.dto.OrderUserCriteria;
import com.xcordia.document.service.OrderUserQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.xcordia.document.domain.OrderUser}.
 */
@RestController
@RequestMapping("/api")
public class OrderUserResource {

    private final Logger log = LoggerFactory.getLogger(OrderUserResource.class);

    private static final String ENTITY_NAME = "orderUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderUserService orderUserService;

    private final OrderUserQueryService orderUserQueryService;

    public OrderUserResource(OrderUserService orderUserService, OrderUserQueryService orderUserQueryService) {
        this.orderUserService = orderUserService;
        this.orderUserQueryService = orderUserQueryService;
    }

    /**
     * {@code POST  /order-users} : Create a new orderUser.
     *
     * @param orderUser the orderUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderUser, or with status {@code 400 (Bad Request)} if the orderUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/order-users")
    public ResponseEntity<OrderUser> createOrderUser(@Valid @RequestBody OrderUser orderUser) throws URISyntaxException {
        log.debug("REST request to save OrderUser : {}", orderUser);
        if (orderUser.getId() != null) {
            throw new BadRequestAlertException("A new orderUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderUser result = orderUserService.save(orderUser);
        return ResponseEntity.created(new URI("/api/order-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-users} : Updates an existing orderUser.
     *
     * @param orderUser the orderUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderUser,
     * or with status {@code 400 (Bad Request)} if the orderUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/order-users")
    public ResponseEntity<OrderUser> updateOrderUser(@Valid @RequestBody OrderUser orderUser) throws URISyntaxException {
        log.debug("REST request to update OrderUser : {}", orderUser);
        if (orderUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrderUser result = orderUserService.save(orderUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, orderUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /order-users} : get all the orderUsers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderUsers in body.
     */
    @GetMapping("/order-users")
    public ResponseEntity<List<OrderUser>> getAllOrderUsers(OrderUserCriteria criteria, Pageable pageable) {
        log.debug("REST request to get OrderUsers by criteria: {}", criteria);
        Page<OrderUser> page = orderUserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-users/count} : count all the orderUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/order-users/count")
    public ResponseEntity<Long> countOrderUsers(OrderUserCriteria criteria) {
        log.debug("REST request to count OrderUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /order-users/:id} : get the "id" orderUser.
     *
     * @param id the id of the orderUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/order-users/{id}")
    public ResponseEntity<OrderUser> getOrderUser(@PathVariable Long id) {
        log.debug("REST request to get OrderUser : {}", id);
        Optional<OrderUser> orderUser = orderUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderUser);
    }

    /**
     * {@code DELETE  /order-users/:id} : delete the "id" orderUser.
     *
     * @param id the id of the orderUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/order-users/{id}")
    public ResponseEntity<Void> deleteOrderUser(@PathVariable Long id) {
        log.debug("REST request to delete OrderUser : {}", id);

        orderUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
