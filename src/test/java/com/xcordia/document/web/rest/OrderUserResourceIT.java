package com.xcordia.document.web.rest;

import com.xcordia.document.JhipsterApp;
import com.xcordia.document.domain.OrderUser;
import com.xcordia.document.domain.Order;
import com.xcordia.document.repository.OrderUserRepository;
import com.xcordia.document.service.OrderUserService;
import com.xcordia.document.service.dto.OrderUserCriteria;
import com.xcordia.document.service.OrderUserQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link OrderUserResource} REST controller.
 */
@SpringBootTest(classes = JhipsterApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class OrderUserResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;
    private static final Long SMALLER_USER_ID = 1L - 1L;

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private OrderUserRepository orderUserRepository;

    @Autowired
    private OrderUserService orderUserService;

    @Autowired
    private OrderUserQueryService orderUserQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderUserMockMvc;

    private OrderUser orderUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderUser createEntity(EntityManager em) {
        OrderUser orderUser = new OrderUser()
            .userId(DEFAULT_USER_ID)
            .dateCreated(DEFAULT_DATE_CREATED);
        return orderUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderUser createUpdatedEntity(EntityManager em) {
        OrderUser orderUser = new OrderUser()
            .userId(UPDATED_USER_ID)
            .dateCreated(UPDATED_DATE_CREATED);
        return orderUser;
    }

    @BeforeEach
    public void initTest() {
        orderUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrderUser() throws Exception {
        int databaseSizeBeforeCreate = orderUserRepository.findAll().size();
        // Create the OrderUser
        restOrderUserMockMvc.perform(post("/api/order-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderUser)))
            .andExpect(status().isCreated());

        // Validate the OrderUser in the database
        List<OrderUser> orderUserList = orderUserRepository.findAll();
        assertThat(orderUserList).hasSize(databaseSizeBeforeCreate + 1);
        OrderUser testOrderUser = orderUserList.get(orderUserList.size() - 1);
        assertThat(testOrderUser.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testOrderUser.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
    }

    @Test
    @Transactional
    public void createOrderUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orderUserRepository.findAll().size();

        // Create the OrderUser with an existing ID
        orderUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderUserMockMvc.perform(post("/api/order-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderUser)))
            .andExpect(status().isBadRequest());

        // Validate the OrderUser in the database
        List<OrderUser> orderUserList = orderUserRepository.findAll();
        assertThat(orderUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderUserRepository.findAll().size();
        // set the field null
        orderUser.setUserId(null);

        // Create the OrderUser, which fails.


        restOrderUserMockMvc.perform(post("/api/order-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderUser)))
            .andExpect(status().isBadRequest());

        List<OrderUser> orderUserList = orderUserRepository.findAll();
        assertThat(orderUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrderUsers() throws Exception {
        // Initialize the database
        orderUserRepository.saveAndFlush(orderUser);

        // Get all the orderUserList
        restOrderUserMockMvc.perform(get("/api/order-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())));
    }
    
    @Test
    @Transactional
    public void getOrderUser() throws Exception {
        // Initialize the database
        orderUserRepository.saveAndFlush(orderUser);

        // Get the orderUser
        restOrderUserMockMvc.perform(get("/api/order-users/{id}", orderUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderUser.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()));
    }


    @Test
    @Transactional
    public void getOrderUsersByIdFiltering() throws Exception {
        // Initialize the database
        orderUserRepository.saveAndFlush(orderUser);

        Long id = orderUser.getId();

        defaultOrderUserShouldBeFound("id.equals=" + id);
        defaultOrderUserShouldNotBeFound("id.notEquals=" + id);

        defaultOrderUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrderUserShouldNotBeFound("id.greaterThan=" + id);

        defaultOrderUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrderUserShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllOrderUsersByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        orderUserRepository.saveAndFlush(orderUser);

        // Get all the orderUserList where userId equals to DEFAULT_USER_ID
        defaultOrderUserShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the orderUserList where userId equals to UPDATED_USER_ID
        defaultOrderUserShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllOrderUsersByUserIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderUserRepository.saveAndFlush(orderUser);

        // Get all the orderUserList where userId not equals to DEFAULT_USER_ID
        defaultOrderUserShouldNotBeFound("userId.notEquals=" + DEFAULT_USER_ID);

        // Get all the orderUserList where userId not equals to UPDATED_USER_ID
        defaultOrderUserShouldBeFound("userId.notEquals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllOrderUsersByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        orderUserRepository.saveAndFlush(orderUser);

        // Get all the orderUserList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultOrderUserShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the orderUserList where userId equals to UPDATED_USER_ID
        defaultOrderUserShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllOrderUsersByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderUserRepository.saveAndFlush(orderUser);

        // Get all the orderUserList where userId is not null
        defaultOrderUserShouldBeFound("userId.specified=true");

        // Get all the orderUserList where userId is null
        defaultOrderUserShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrderUsersByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderUserRepository.saveAndFlush(orderUser);

        // Get all the orderUserList where userId is greater than or equal to DEFAULT_USER_ID
        defaultOrderUserShouldBeFound("userId.greaterThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the orderUserList where userId is greater than or equal to UPDATED_USER_ID
        defaultOrderUserShouldNotBeFound("userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllOrderUsersByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderUserRepository.saveAndFlush(orderUser);

        // Get all the orderUserList where userId is less than or equal to DEFAULT_USER_ID
        defaultOrderUserShouldBeFound("userId.lessThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the orderUserList where userId is less than or equal to SMALLER_USER_ID
        defaultOrderUserShouldNotBeFound("userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllOrderUsersByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        orderUserRepository.saveAndFlush(orderUser);

        // Get all the orderUserList where userId is less than DEFAULT_USER_ID
        defaultOrderUserShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the orderUserList where userId is less than UPDATED_USER_ID
        defaultOrderUserShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllOrderUsersByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderUserRepository.saveAndFlush(orderUser);

        // Get all the orderUserList where userId is greater than DEFAULT_USER_ID
        defaultOrderUserShouldNotBeFound("userId.greaterThan=" + DEFAULT_USER_ID);

        // Get all the orderUserList where userId is greater than SMALLER_USER_ID
        defaultOrderUserShouldBeFound("userId.greaterThan=" + SMALLER_USER_ID);
    }


    @Test
    @Transactional
    public void getAllOrderUsersByDateCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        orderUserRepository.saveAndFlush(orderUser);

        // Get all the orderUserList where dateCreated equals to DEFAULT_DATE_CREATED
        defaultOrderUserShouldBeFound("dateCreated.equals=" + DEFAULT_DATE_CREATED);

        // Get all the orderUserList where dateCreated equals to UPDATED_DATE_CREATED
        defaultOrderUserShouldNotBeFound("dateCreated.equals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllOrderUsersByDateCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderUserRepository.saveAndFlush(orderUser);

        // Get all the orderUserList where dateCreated not equals to DEFAULT_DATE_CREATED
        defaultOrderUserShouldNotBeFound("dateCreated.notEquals=" + DEFAULT_DATE_CREATED);

        // Get all the orderUserList where dateCreated not equals to UPDATED_DATE_CREATED
        defaultOrderUserShouldBeFound("dateCreated.notEquals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllOrderUsersByDateCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        orderUserRepository.saveAndFlush(orderUser);

        // Get all the orderUserList where dateCreated in DEFAULT_DATE_CREATED or UPDATED_DATE_CREATED
        defaultOrderUserShouldBeFound("dateCreated.in=" + DEFAULT_DATE_CREATED + "," + UPDATED_DATE_CREATED);

        // Get all the orderUserList where dateCreated equals to UPDATED_DATE_CREATED
        defaultOrderUserShouldNotBeFound("dateCreated.in=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllOrderUsersByDateCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderUserRepository.saveAndFlush(orderUser);

        // Get all the orderUserList where dateCreated is not null
        defaultOrderUserShouldBeFound("dateCreated.specified=true");

        // Get all the orderUserList where dateCreated is null
        defaultOrderUserShouldNotBeFound("dateCreated.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrderUsersByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        orderUserRepository.saveAndFlush(orderUser);
        Order order = OrderResourceIT.createEntity(em);
        em.persist(order);
        em.flush();
        orderUser.setOrder(order);
        orderUserRepository.saveAndFlush(orderUser);
        Long orderId = order.getId();

        // Get all the orderUserList where order equals to orderId
        defaultOrderUserShouldBeFound("orderId.equals=" + orderId);

        // Get all the orderUserList where order equals to orderId + 1
        defaultOrderUserShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderUserShouldBeFound(String filter) throws Exception {
        restOrderUserMockMvc.perform(get("/api/order-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())));

        // Check, that the count call also returns 1
        restOrderUserMockMvc.perform(get("/api/order-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderUserShouldNotBeFound(String filter) throws Exception {
        restOrderUserMockMvc.perform(get("/api/order-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderUserMockMvc.perform(get("/api/order-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingOrderUser() throws Exception {
        // Get the orderUser
        restOrderUserMockMvc.perform(get("/api/order-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderUser() throws Exception {
        // Initialize the database
        orderUserService.save(orderUser);

        int databaseSizeBeforeUpdate = orderUserRepository.findAll().size();

        // Update the orderUser
        OrderUser updatedOrderUser = orderUserRepository.findById(orderUser.getId()).get();
        // Disconnect from session so that the updates on updatedOrderUser are not directly saved in db
        em.detach(updatedOrderUser);
        updatedOrderUser
            .userId(UPDATED_USER_ID)
            .dateCreated(UPDATED_DATE_CREATED);

        restOrderUserMockMvc.perform(put("/api/order-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrderUser)))
            .andExpect(status().isOk());

        // Validate the OrderUser in the database
        List<OrderUser> orderUserList = orderUserRepository.findAll();
        assertThat(orderUserList).hasSize(databaseSizeBeforeUpdate);
        OrderUser testOrderUser = orderUserList.get(orderUserList.size() - 1);
        assertThat(testOrderUser.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testOrderUser.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void updateNonExistingOrderUser() throws Exception {
        int databaseSizeBeforeUpdate = orderUserRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderUserMockMvc.perform(put("/api/order-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderUser)))
            .andExpect(status().isBadRequest());

        // Validate the OrderUser in the database
        List<OrderUser> orderUserList = orderUserRepository.findAll();
        assertThat(orderUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrderUser() throws Exception {
        // Initialize the database
        orderUserService.save(orderUser);

        int databaseSizeBeforeDelete = orderUserRepository.findAll().size();

        // Delete the orderUser
        restOrderUserMockMvc.perform(delete("/api/order-users/{id}", orderUser.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderUser> orderUserList = orderUserRepository.findAll();
        assertThat(orderUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
