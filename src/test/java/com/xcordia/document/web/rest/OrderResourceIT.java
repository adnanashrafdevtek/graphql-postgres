package com.xcordia.document.web.rest;

import com.xcordia.document.JhipsterApp;
import com.xcordia.document.domain.Order;
import com.xcordia.document.domain.OrderUser;
import com.xcordia.document.domain.Message;
import com.xcordia.document.domain.OrderItem;
import com.xcordia.document.repository.OrderRepository;
import com.xcordia.document.service.OrderService;
import com.xcordia.document.service.dto.OrderCriteria;
import com.xcordia.document.service.OrderQueryService;

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
 * Integration tests for the {@link OrderResource} REST controller.
 */
@SpringBootTest(classes = JhipsterApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class OrderResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_UUID = "AAAAAAAAAA";
    private static final String UPDATED_UUID = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final Long DEFAULT_BUYER_USER_ID = 1L;
    private static final Long UPDATED_BUYER_USER_ID = 2L;
    private static final Long SMALLER_BUYER_USER_ID = 1L - 1L;

    private static final Long DEFAULT_BUYER_ORGANIZATION_ID = 1L;
    private static final Long UPDATED_BUYER_ORGANIZATION_ID = 2L;
    private static final Long SMALLER_BUYER_ORGANIZATION_ID = 1L - 1L;

    private static final Long DEFAULT_SUPPLIER_ORGANIZATION_ID = 1L;
    private static final Long UPDATED_SUPPLIER_ORGANIZATION_ID = 2L;
    private static final Long SMALLER_SUPPLIER_ORGANIZATION_ID = 1L - 1L;

    private static final Long DEFAULT_PRIMARY_SUPPLIER_USER_ID = 1L;
    private static final Long UPDATED_PRIMARY_SUPPLIER_USER_ID = 2L;
    private static final Long SMALLER_PRIMARY_SUPPLIER_USER_ID = 1L - 1L;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderQueryService orderQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderMockMvc;

    private Order order;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createEntity(EntityManager em) {
        Order order = new Order()
            .name(DEFAULT_NAME)
            .uuid(DEFAULT_UUID)
            .dateCreated(DEFAULT_DATE_CREATED)
            .createdBy(DEFAULT_CREATED_BY)
            .dateUpdated(DEFAULT_DATE_UPDATED)
            .updatedBy(DEFAULT_UPDATED_BY)
            .buyerUserId(DEFAULT_BUYER_USER_ID)
            .buyerOrganizationId(DEFAULT_BUYER_ORGANIZATION_ID)
            .supplierOrganizationId(DEFAULT_SUPPLIER_ORGANIZATION_ID)
            .primarySupplierUserId(DEFAULT_PRIMARY_SUPPLIER_USER_ID);
        return order;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createUpdatedEntity(EntityManager em) {
        Order order = new Order()
            .name(UPDATED_NAME)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .createdBy(UPDATED_CREATED_BY)
            .dateUpdated(UPDATED_DATE_UPDATED)
            .updatedBy(UPDATED_UPDATED_BY)
            .buyerUserId(UPDATED_BUYER_USER_ID)
            .buyerOrganizationId(UPDATED_BUYER_ORGANIZATION_ID)
            .supplierOrganizationId(UPDATED_SUPPLIER_ORGANIZATION_ID)
            .primarySupplierUserId(UPDATED_PRIMARY_SUPPLIER_USER_ID);
        return order;
    }

    @BeforeEach
    public void initTest() {
        order = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrder() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().size();
        // Create the Order
        restOrderMockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isCreated());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate + 1);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrder.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testOrder.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testOrder.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOrder.getDateUpdated()).isEqualTo(DEFAULT_DATE_UPDATED);
        assertThat(testOrder.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testOrder.getBuyerUserId()).isEqualTo(DEFAULT_BUYER_USER_ID);
        assertThat(testOrder.getBuyerOrganizationId()).isEqualTo(DEFAULT_BUYER_ORGANIZATION_ID);
        assertThat(testOrder.getSupplierOrganizationId()).isEqualTo(DEFAULT_SUPPLIER_ORGANIZATION_ID);
        assertThat(testOrder.getPrimarySupplierUserId()).isEqualTo(DEFAULT_PRIMARY_SUPPLIER_USER_ID);
    }

    @Test
    @Transactional
    public void createOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().size();

        // Create the Order with an existing ID
        order.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderMockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setName(null);

        // Create the Order, which fails.


        restOrderMockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setUuid(null);

        // Create the Order, which fails.


        restOrderMockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setDateCreated(null);

        // Create the Order, which fails.


        restOrderMockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setCreatedBy(null);

        // Create the Order, which fails.


        restOrderMockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBuyerUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setBuyerUserId(null);

        // Create the Order, which fails.


        restOrderMockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBuyerOrganizationIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setBuyerOrganizationId(null);

        // Create the Order, which fails.


        restOrderMockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSupplierOrganizationIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setSupplierOrganizationId(null);

        // Create the Order, which fails.


        restOrderMockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrimarySupplierUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setPrimarySupplierUserId(null);

        // Create the Order, which fails.


        restOrderMockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrders() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList
        restOrderMockMvc.perform(get("/api/orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].buyerUserId").value(hasItem(DEFAULT_BUYER_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].buyerOrganizationId").value(hasItem(DEFAULT_BUYER_ORGANIZATION_ID.intValue())))
            .andExpect(jsonPath("$.[*].supplierOrganizationId").value(hasItem(DEFAULT_SUPPLIER_ORGANIZATION_ID.intValue())))
            .andExpect(jsonPath("$.[*].primarySupplierUserId").value(hasItem(DEFAULT_PRIMARY_SUPPLIER_USER_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get the order
        restOrderMockMvc.perform(get("/api/orders/{id}", order.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(order.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.dateUpdated").value(DEFAULT_DATE_UPDATED.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.buyerUserId").value(DEFAULT_BUYER_USER_ID.intValue()))
            .andExpect(jsonPath("$.buyerOrganizationId").value(DEFAULT_BUYER_ORGANIZATION_ID.intValue()))
            .andExpect(jsonPath("$.supplierOrganizationId").value(DEFAULT_SUPPLIER_ORGANIZATION_ID.intValue()))
            .andExpect(jsonPath("$.primarySupplierUserId").value(DEFAULT_PRIMARY_SUPPLIER_USER_ID.intValue()));
    }


    @Test
    @Transactional
    public void getOrdersByIdFiltering() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        Long id = order.getId();

        defaultOrderShouldBeFound("id.equals=" + id);
        defaultOrderShouldNotBeFound("id.notEquals=" + id);

        defaultOrderShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrderShouldNotBeFound("id.greaterThan=" + id);

        defaultOrderShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrderShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllOrdersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where name equals to DEFAULT_NAME
        defaultOrderShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the orderList where name equals to UPDATED_NAME
        defaultOrderShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrdersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where name not equals to DEFAULT_NAME
        defaultOrderShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the orderList where name not equals to UPDATED_NAME
        defaultOrderShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrdersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOrderShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the orderList where name equals to UPDATED_NAME
        defaultOrderShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrdersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where name is not null
        defaultOrderShouldBeFound("name.specified=true");

        // Get all the orderList where name is null
        defaultOrderShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllOrdersByNameContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where name contains DEFAULT_NAME
        defaultOrderShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the orderList where name contains UPDATED_NAME
        defaultOrderShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrdersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where name does not contain DEFAULT_NAME
        defaultOrderShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the orderList where name does not contain UPDATED_NAME
        defaultOrderShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllOrdersByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where uuid equals to DEFAULT_UUID
        defaultOrderShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the orderList where uuid equals to UPDATED_UUID
        defaultOrderShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    public void getAllOrdersByUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where uuid not equals to DEFAULT_UUID
        defaultOrderShouldNotBeFound("uuid.notEquals=" + DEFAULT_UUID);

        // Get all the orderList where uuid not equals to UPDATED_UUID
        defaultOrderShouldBeFound("uuid.notEquals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    public void getAllOrdersByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultOrderShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the orderList where uuid equals to UPDATED_UUID
        defaultOrderShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    public void getAllOrdersByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where uuid is not null
        defaultOrderShouldBeFound("uuid.specified=true");

        // Get all the orderList where uuid is null
        defaultOrderShouldNotBeFound("uuid.specified=false");
    }
                @Test
    @Transactional
    public void getAllOrdersByUuidContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where uuid contains DEFAULT_UUID
        defaultOrderShouldBeFound("uuid.contains=" + DEFAULT_UUID);

        // Get all the orderList where uuid contains UPDATED_UUID
        defaultOrderShouldNotBeFound("uuid.contains=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    public void getAllOrdersByUuidNotContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where uuid does not contain DEFAULT_UUID
        defaultOrderShouldNotBeFound("uuid.doesNotContain=" + DEFAULT_UUID);

        // Get all the orderList where uuid does not contain UPDATED_UUID
        defaultOrderShouldBeFound("uuid.doesNotContain=" + UPDATED_UUID);
    }


    @Test
    @Transactional
    public void getAllOrdersByDateCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where dateCreated equals to DEFAULT_DATE_CREATED
        defaultOrderShouldBeFound("dateCreated.equals=" + DEFAULT_DATE_CREATED);

        // Get all the orderList where dateCreated equals to UPDATED_DATE_CREATED
        defaultOrderShouldNotBeFound("dateCreated.equals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllOrdersByDateCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where dateCreated not equals to DEFAULT_DATE_CREATED
        defaultOrderShouldNotBeFound("dateCreated.notEquals=" + DEFAULT_DATE_CREATED);

        // Get all the orderList where dateCreated not equals to UPDATED_DATE_CREATED
        defaultOrderShouldBeFound("dateCreated.notEquals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllOrdersByDateCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where dateCreated in DEFAULT_DATE_CREATED or UPDATED_DATE_CREATED
        defaultOrderShouldBeFound("dateCreated.in=" + DEFAULT_DATE_CREATED + "," + UPDATED_DATE_CREATED);

        // Get all the orderList where dateCreated equals to UPDATED_DATE_CREATED
        defaultOrderShouldNotBeFound("dateCreated.in=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllOrdersByDateCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where dateCreated is not null
        defaultOrderShouldBeFound("dateCreated.specified=true");

        // Get all the orderList where dateCreated is null
        defaultOrderShouldNotBeFound("dateCreated.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where createdBy equals to DEFAULT_CREATED_BY
        defaultOrderShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the orderList where createdBy equals to UPDATED_CREATED_BY
        defaultOrderShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllOrdersByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where createdBy not equals to DEFAULT_CREATED_BY
        defaultOrderShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the orderList where createdBy not equals to UPDATED_CREATED_BY
        defaultOrderShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllOrdersByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultOrderShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the orderList where createdBy equals to UPDATED_CREATED_BY
        defaultOrderShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllOrdersByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where createdBy is not null
        defaultOrderShouldBeFound("createdBy.specified=true");

        // Get all the orderList where createdBy is null
        defaultOrderShouldNotBeFound("createdBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllOrdersByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where createdBy contains DEFAULT_CREATED_BY
        defaultOrderShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the orderList where createdBy contains UPDATED_CREATED_BY
        defaultOrderShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllOrdersByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where createdBy does not contain DEFAULT_CREATED_BY
        defaultOrderShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the orderList where createdBy does not contain UPDATED_CREATED_BY
        defaultOrderShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }


    @Test
    @Transactional
    public void getAllOrdersByDateUpdatedIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where dateUpdated equals to DEFAULT_DATE_UPDATED
        defaultOrderShouldBeFound("dateUpdated.equals=" + DEFAULT_DATE_UPDATED);

        // Get all the orderList where dateUpdated equals to UPDATED_DATE_UPDATED
        defaultOrderShouldNotBeFound("dateUpdated.equals=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllOrdersByDateUpdatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where dateUpdated not equals to DEFAULT_DATE_UPDATED
        defaultOrderShouldNotBeFound("dateUpdated.notEquals=" + DEFAULT_DATE_UPDATED);

        // Get all the orderList where dateUpdated not equals to UPDATED_DATE_UPDATED
        defaultOrderShouldBeFound("dateUpdated.notEquals=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllOrdersByDateUpdatedIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where dateUpdated in DEFAULT_DATE_UPDATED or UPDATED_DATE_UPDATED
        defaultOrderShouldBeFound("dateUpdated.in=" + DEFAULT_DATE_UPDATED + "," + UPDATED_DATE_UPDATED);

        // Get all the orderList where dateUpdated equals to UPDATED_DATE_UPDATED
        defaultOrderShouldNotBeFound("dateUpdated.in=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllOrdersByDateUpdatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where dateUpdated is not null
        defaultOrderShouldBeFound("dateUpdated.specified=true");

        // Get all the orderList where dateUpdated is null
        defaultOrderShouldNotBeFound("dateUpdated.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByUpdatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where updatedBy equals to DEFAULT_UPDATED_BY
        defaultOrderShouldBeFound("updatedBy.equals=" + DEFAULT_UPDATED_BY);

        // Get all the orderList where updatedBy equals to UPDATED_UPDATED_BY
        defaultOrderShouldNotBeFound("updatedBy.equals=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    public void getAllOrdersByUpdatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where updatedBy not equals to DEFAULT_UPDATED_BY
        defaultOrderShouldNotBeFound("updatedBy.notEquals=" + DEFAULT_UPDATED_BY);

        // Get all the orderList where updatedBy not equals to UPDATED_UPDATED_BY
        defaultOrderShouldBeFound("updatedBy.notEquals=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    public void getAllOrdersByUpdatedByIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where updatedBy in DEFAULT_UPDATED_BY or UPDATED_UPDATED_BY
        defaultOrderShouldBeFound("updatedBy.in=" + DEFAULT_UPDATED_BY + "," + UPDATED_UPDATED_BY);

        // Get all the orderList where updatedBy equals to UPDATED_UPDATED_BY
        defaultOrderShouldNotBeFound("updatedBy.in=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    public void getAllOrdersByUpdatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where updatedBy is not null
        defaultOrderShouldBeFound("updatedBy.specified=true");

        // Get all the orderList where updatedBy is null
        defaultOrderShouldNotBeFound("updatedBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllOrdersByUpdatedByContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where updatedBy contains DEFAULT_UPDATED_BY
        defaultOrderShouldBeFound("updatedBy.contains=" + DEFAULT_UPDATED_BY);

        // Get all the orderList where updatedBy contains UPDATED_UPDATED_BY
        defaultOrderShouldNotBeFound("updatedBy.contains=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    public void getAllOrdersByUpdatedByNotContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where updatedBy does not contain DEFAULT_UPDATED_BY
        defaultOrderShouldNotBeFound("updatedBy.doesNotContain=" + DEFAULT_UPDATED_BY);

        // Get all the orderList where updatedBy does not contain UPDATED_UPDATED_BY
        defaultOrderShouldBeFound("updatedBy.doesNotContain=" + UPDATED_UPDATED_BY);
    }


    @Test
    @Transactional
    public void getAllOrdersByBuyerUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where buyerUserId equals to DEFAULT_BUYER_USER_ID
        defaultOrderShouldBeFound("buyerUserId.equals=" + DEFAULT_BUYER_USER_ID);

        // Get all the orderList where buyerUserId equals to UPDATED_BUYER_USER_ID
        defaultOrderShouldNotBeFound("buyerUserId.equals=" + UPDATED_BUYER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersByBuyerUserIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where buyerUserId not equals to DEFAULT_BUYER_USER_ID
        defaultOrderShouldNotBeFound("buyerUserId.notEquals=" + DEFAULT_BUYER_USER_ID);

        // Get all the orderList where buyerUserId not equals to UPDATED_BUYER_USER_ID
        defaultOrderShouldBeFound("buyerUserId.notEquals=" + UPDATED_BUYER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersByBuyerUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where buyerUserId in DEFAULT_BUYER_USER_ID or UPDATED_BUYER_USER_ID
        defaultOrderShouldBeFound("buyerUserId.in=" + DEFAULT_BUYER_USER_ID + "," + UPDATED_BUYER_USER_ID);

        // Get all the orderList where buyerUserId equals to UPDATED_BUYER_USER_ID
        defaultOrderShouldNotBeFound("buyerUserId.in=" + UPDATED_BUYER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersByBuyerUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where buyerUserId is not null
        defaultOrderShouldBeFound("buyerUserId.specified=true");

        // Get all the orderList where buyerUserId is null
        defaultOrderShouldNotBeFound("buyerUserId.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByBuyerUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where buyerUserId is greater than or equal to DEFAULT_BUYER_USER_ID
        defaultOrderShouldBeFound("buyerUserId.greaterThanOrEqual=" + DEFAULT_BUYER_USER_ID);

        // Get all the orderList where buyerUserId is greater than or equal to UPDATED_BUYER_USER_ID
        defaultOrderShouldNotBeFound("buyerUserId.greaterThanOrEqual=" + UPDATED_BUYER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersByBuyerUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where buyerUserId is less than or equal to DEFAULT_BUYER_USER_ID
        defaultOrderShouldBeFound("buyerUserId.lessThanOrEqual=" + DEFAULT_BUYER_USER_ID);

        // Get all the orderList where buyerUserId is less than or equal to SMALLER_BUYER_USER_ID
        defaultOrderShouldNotBeFound("buyerUserId.lessThanOrEqual=" + SMALLER_BUYER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersByBuyerUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where buyerUserId is less than DEFAULT_BUYER_USER_ID
        defaultOrderShouldNotBeFound("buyerUserId.lessThan=" + DEFAULT_BUYER_USER_ID);

        // Get all the orderList where buyerUserId is less than UPDATED_BUYER_USER_ID
        defaultOrderShouldBeFound("buyerUserId.lessThan=" + UPDATED_BUYER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersByBuyerUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where buyerUserId is greater than DEFAULT_BUYER_USER_ID
        defaultOrderShouldNotBeFound("buyerUserId.greaterThan=" + DEFAULT_BUYER_USER_ID);

        // Get all the orderList where buyerUserId is greater than SMALLER_BUYER_USER_ID
        defaultOrderShouldBeFound("buyerUserId.greaterThan=" + SMALLER_BUYER_USER_ID);
    }


    @Test
    @Transactional
    public void getAllOrdersByBuyerOrganizationIdIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where buyerOrganizationId equals to DEFAULT_BUYER_ORGANIZATION_ID
        defaultOrderShouldBeFound("buyerOrganizationId.equals=" + DEFAULT_BUYER_ORGANIZATION_ID);

        // Get all the orderList where buyerOrganizationId equals to UPDATED_BUYER_ORGANIZATION_ID
        defaultOrderShouldNotBeFound("buyerOrganizationId.equals=" + UPDATED_BUYER_ORGANIZATION_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersByBuyerOrganizationIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where buyerOrganizationId not equals to DEFAULT_BUYER_ORGANIZATION_ID
        defaultOrderShouldNotBeFound("buyerOrganizationId.notEquals=" + DEFAULT_BUYER_ORGANIZATION_ID);

        // Get all the orderList where buyerOrganizationId not equals to UPDATED_BUYER_ORGANIZATION_ID
        defaultOrderShouldBeFound("buyerOrganizationId.notEquals=" + UPDATED_BUYER_ORGANIZATION_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersByBuyerOrganizationIdIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where buyerOrganizationId in DEFAULT_BUYER_ORGANIZATION_ID or UPDATED_BUYER_ORGANIZATION_ID
        defaultOrderShouldBeFound("buyerOrganizationId.in=" + DEFAULT_BUYER_ORGANIZATION_ID + "," + UPDATED_BUYER_ORGANIZATION_ID);

        // Get all the orderList where buyerOrganizationId equals to UPDATED_BUYER_ORGANIZATION_ID
        defaultOrderShouldNotBeFound("buyerOrganizationId.in=" + UPDATED_BUYER_ORGANIZATION_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersByBuyerOrganizationIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where buyerOrganizationId is not null
        defaultOrderShouldBeFound("buyerOrganizationId.specified=true");

        // Get all the orderList where buyerOrganizationId is null
        defaultOrderShouldNotBeFound("buyerOrganizationId.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByBuyerOrganizationIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where buyerOrganizationId is greater than or equal to DEFAULT_BUYER_ORGANIZATION_ID
        defaultOrderShouldBeFound("buyerOrganizationId.greaterThanOrEqual=" + DEFAULT_BUYER_ORGANIZATION_ID);

        // Get all the orderList where buyerOrganizationId is greater than or equal to UPDATED_BUYER_ORGANIZATION_ID
        defaultOrderShouldNotBeFound("buyerOrganizationId.greaterThanOrEqual=" + UPDATED_BUYER_ORGANIZATION_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersByBuyerOrganizationIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where buyerOrganizationId is less than or equal to DEFAULT_BUYER_ORGANIZATION_ID
        defaultOrderShouldBeFound("buyerOrganizationId.lessThanOrEqual=" + DEFAULT_BUYER_ORGANIZATION_ID);

        // Get all the orderList where buyerOrganizationId is less than or equal to SMALLER_BUYER_ORGANIZATION_ID
        defaultOrderShouldNotBeFound("buyerOrganizationId.lessThanOrEqual=" + SMALLER_BUYER_ORGANIZATION_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersByBuyerOrganizationIdIsLessThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where buyerOrganizationId is less than DEFAULT_BUYER_ORGANIZATION_ID
        defaultOrderShouldNotBeFound("buyerOrganizationId.lessThan=" + DEFAULT_BUYER_ORGANIZATION_ID);

        // Get all the orderList where buyerOrganizationId is less than UPDATED_BUYER_ORGANIZATION_ID
        defaultOrderShouldBeFound("buyerOrganizationId.lessThan=" + UPDATED_BUYER_ORGANIZATION_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersByBuyerOrganizationIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where buyerOrganizationId is greater than DEFAULT_BUYER_ORGANIZATION_ID
        defaultOrderShouldNotBeFound("buyerOrganizationId.greaterThan=" + DEFAULT_BUYER_ORGANIZATION_ID);

        // Get all the orderList where buyerOrganizationId is greater than SMALLER_BUYER_ORGANIZATION_ID
        defaultOrderShouldBeFound("buyerOrganizationId.greaterThan=" + SMALLER_BUYER_ORGANIZATION_ID);
    }


    @Test
    @Transactional
    public void getAllOrdersBySupplierOrganizationIdIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where supplierOrganizationId equals to DEFAULT_SUPPLIER_ORGANIZATION_ID
        defaultOrderShouldBeFound("supplierOrganizationId.equals=" + DEFAULT_SUPPLIER_ORGANIZATION_ID);

        // Get all the orderList where supplierOrganizationId equals to UPDATED_SUPPLIER_ORGANIZATION_ID
        defaultOrderShouldNotBeFound("supplierOrganizationId.equals=" + UPDATED_SUPPLIER_ORGANIZATION_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersBySupplierOrganizationIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where supplierOrganizationId not equals to DEFAULT_SUPPLIER_ORGANIZATION_ID
        defaultOrderShouldNotBeFound("supplierOrganizationId.notEquals=" + DEFAULT_SUPPLIER_ORGANIZATION_ID);

        // Get all the orderList where supplierOrganizationId not equals to UPDATED_SUPPLIER_ORGANIZATION_ID
        defaultOrderShouldBeFound("supplierOrganizationId.notEquals=" + UPDATED_SUPPLIER_ORGANIZATION_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersBySupplierOrganizationIdIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where supplierOrganizationId in DEFAULT_SUPPLIER_ORGANIZATION_ID or UPDATED_SUPPLIER_ORGANIZATION_ID
        defaultOrderShouldBeFound("supplierOrganizationId.in=" + DEFAULT_SUPPLIER_ORGANIZATION_ID + "," + UPDATED_SUPPLIER_ORGANIZATION_ID);

        // Get all the orderList where supplierOrganizationId equals to UPDATED_SUPPLIER_ORGANIZATION_ID
        defaultOrderShouldNotBeFound("supplierOrganizationId.in=" + UPDATED_SUPPLIER_ORGANIZATION_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersBySupplierOrganizationIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where supplierOrganizationId is not null
        defaultOrderShouldBeFound("supplierOrganizationId.specified=true");

        // Get all the orderList where supplierOrganizationId is null
        defaultOrderShouldNotBeFound("supplierOrganizationId.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersBySupplierOrganizationIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where supplierOrganizationId is greater than or equal to DEFAULT_SUPPLIER_ORGANIZATION_ID
        defaultOrderShouldBeFound("supplierOrganizationId.greaterThanOrEqual=" + DEFAULT_SUPPLIER_ORGANIZATION_ID);

        // Get all the orderList where supplierOrganizationId is greater than or equal to UPDATED_SUPPLIER_ORGANIZATION_ID
        defaultOrderShouldNotBeFound("supplierOrganizationId.greaterThanOrEqual=" + UPDATED_SUPPLIER_ORGANIZATION_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersBySupplierOrganizationIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where supplierOrganizationId is less than or equal to DEFAULT_SUPPLIER_ORGANIZATION_ID
        defaultOrderShouldBeFound("supplierOrganizationId.lessThanOrEqual=" + DEFAULT_SUPPLIER_ORGANIZATION_ID);

        // Get all the orderList where supplierOrganizationId is less than or equal to SMALLER_SUPPLIER_ORGANIZATION_ID
        defaultOrderShouldNotBeFound("supplierOrganizationId.lessThanOrEqual=" + SMALLER_SUPPLIER_ORGANIZATION_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersBySupplierOrganizationIdIsLessThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where supplierOrganizationId is less than DEFAULT_SUPPLIER_ORGANIZATION_ID
        defaultOrderShouldNotBeFound("supplierOrganizationId.lessThan=" + DEFAULT_SUPPLIER_ORGANIZATION_ID);

        // Get all the orderList where supplierOrganizationId is less than UPDATED_SUPPLIER_ORGANIZATION_ID
        defaultOrderShouldBeFound("supplierOrganizationId.lessThan=" + UPDATED_SUPPLIER_ORGANIZATION_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersBySupplierOrganizationIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where supplierOrganizationId is greater than DEFAULT_SUPPLIER_ORGANIZATION_ID
        defaultOrderShouldNotBeFound("supplierOrganizationId.greaterThan=" + DEFAULT_SUPPLIER_ORGANIZATION_ID);

        // Get all the orderList where supplierOrganizationId is greater than SMALLER_SUPPLIER_ORGANIZATION_ID
        defaultOrderShouldBeFound("supplierOrganizationId.greaterThan=" + SMALLER_SUPPLIER_ORGANIZATION_ID);
    }


    @Test
    @Transactional
    public void getAllOrdersByPrimarySupplierUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where primarySupplierUserId equals to DEFAULT_PRIMARY_SUPPLIER_USER_ID
        defaultOrderShouldBeFound("primarySupplierUserId.equals=" + DEFAULT_PRIMARY_SUPPLIER_USER_ID);

        // Get all the orderList where primarySupplierUserId equals to UPDATED_PRIMARY_SUPPLIER_USER_ID
        defaultOrderShouldNotBeFound("primarySupplierUserId.equals=" + UPDATED_PRIMARY_SUPPLIER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersByPrimarySupplierUserIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where primarySupplierUserId not equals to DEFAULT_PRIMARY_SUPPLIER_USER_ID
        defaultOrderShouldNotBeFound("primarySupplierUserId.notEquals=" + DEFAULT_PRIMARY_SUPPLIER_USER_ID);

        // Get all the orderList where primarySupplierUserId not equals to UPDATED_PRIMARY_SUPPLIER_USER_ID
        defaultOrderShouldBeFound("primarySupplierUserId.notEquals=" + UPDATED_PRIMARY_SUPPLIER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersByPrimarySupplierUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where primarySupplierUserId in DEFAULT_PRIMARY_SUPPLIER_USER_ID or UPDATED_PRIMARY_SUPPLIER_USER_ID
        defaultOrderShouldBeFound("primarySupplierUserId.in=" + DEFAULT_PRIMARY_SUPPLIER_USER_ID + "," + UPDATED_PRIMARY_SUPPLIER_USER_ID);

        // Get all the orderList where primarySupplierUserId equals to UPDATED_PRIMARY_SUPPLIER_USER_ID
        defaultOrderShouldNotBeFound("primarySupplierUserId.in=" + UPDATED_PRIMARY_SUPPLIER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersByPrimarySupplierUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where primarySupplierUserId is not null
        defaultOrderShouldBeFound("primarySupplierUserId.specified=true");

        // Get all the orderList where primarySupplierUserId is null
        defaultOrderShouldNotBeFound("primarySupplierUserId.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByPrimarySupplierUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where primarySupplierUserId is greater than or equal to DEFAULT_PRIMARY_SUPPLIER_USER_ID
        defaultOrderShouldBeFound("primarySupplierUserId.greaterThanOrEqual=" + DEFAULT_PRIMARY_SUPPLIER_USER_ID);

        // Get all the orderList where primarySupplierUserId is greater than or equal to UPDATED_PRIMARY_SUPPLIER_USER_ID
        defaultOrderShouldNotBeFound("primarySupplierUserId.greaterThanOrEqual=" + UPDATED_PRIMARY_SUPPLIER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersByPrimarySupplierUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where primarySupplierUserId is less than or equal to DEFAULT_PRIMARY_SUPPLIER_USER_ID
        defaultOrderShouldBeFound("primarySupplierUserId.lessThanOrEqual=" + DEFAULT_PRIMARY_SUPPLIER_USER_ID);

        // Get all the orderList where primarySupplierUserId is less than or equal to SMALLER_PRIMARY_SUPPLIER_USER_ID
        defaultOrderShouldNotBeFound("primarySupplierUserId.lessThanOrEqual=" + SMALLER_PRIMARY_SUPPLIER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersByPrimarySupplierUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where primarySupplierUserId is less than DEFAULT_PRIMARY_SUPPLIER_USER_ID
        defaultOrderShouldNotBeFound("primarySupplierUserId.lessThan=" + DEFAULT_PRIMARY_SUPPLIER_USER_ID);

        // Get all the orderList where primarySupplierUserId is less than UPDATED_PRIMARY_SUPPLIER_USER_ID
        defaultOrderShouldBeFound("primarySupplierUserId.lessThan=" + UPDATED_PRIMARY_SUPPLIER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllOrdersByPrimarySupplierUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where primarySupplierUserId is greater than DEFAULT_PRIMARY_SUPPLIER_USER_ID
        defaultOrderShouldNotBeFound("primarySupplierUserId.greaterThan=" + DEFAULT_PRIMARY_SUPPLIER_USER_ID);

        // Get all the orderList where primarySupplierUserId is greater than SMALLER_PRIMARY_SUPPLIER_USER_ID
        defaultOrderShouldBeFound("primarySupplierUserId.greaterThan=" + SMALLER_PRIMARY_SUPPLIER_USER_ID);
    }


    @Test
    @Transactional
    public void getAllOrdersByOrderUsersIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);
        OrderUser orderUsers = OrderUserResourceIT.createEntity(em);
        em.persist(orderUsers);
        em.flush();
        order.addOrderUsers(orderUsers);
        orderRepository.saveAndFlush(order);
        Long orderUsersId = orderUsers.getId();

        // Get all the orderList where orderUsers equals to orderUsersId
        defaultOrderShouldBeFound("orderUsersId.equals=" + orderUsersId);

        // Get all the orderList where orderUsers equals to orderUsersId + 1
        defaultOrderShouldNotBeFound("orderUsersId.equals=" + (orderUsersId + 1));
    }


    @Test
    @Transactional
    public void getAllOrdersByMessagesIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);
        Message messages = MessageResourceIT.createEntity(em);
        em.persist(messages);
        em.flush();
        order.addMessages(messages);
        orderRepository.saveAndFlush(order);
        Long messagesId = messages.getId();

        // Get all the orderList where messages equals to messagesId
        defaultOrderShouldBeFound("messagesId.equals=" + messagesId);

        // Get all the orderList where messages equals to messagesId + 1
        defaultOrderShouldNotBeFound("messagesId.equals=" + (messagesId + 1));
    }


    @Test
    @Transactional
    public void getAllOrdersByOrderItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);
        OrderItem orderItems = OrderItemResourceIT.createEntity(em);
        em.persist(orderItems);
        em.flush();
        order.addOrderItems(orderItems);
        orderRepository.saveAndFlush(order);
        Long orderItemsId = orderItems.getId();

        // Get all the orderList where orderItems equals to orderItemsId
        defaultOrderShouldBeFound("orderItemsId.equals=" + orderItemsId);

        // Get all the orderList where orderItems equals to orderItemsId + 1
        defaultOrderShouldNotBeFound("orderItemsId.equals=" + (orderItemsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderShouldBeFound(String filter) throws Exception {
        restOrderMockMvc.perform(get("/api/orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].buyerUserId").value(hasItem(DEFAULT_BUYER_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].buyerOrganizationId").value(hasItem(DEFAULT_BUYER_ORGANIZATION_ID.intValue())))
            .andExpect(jsonPath("$.[*].supplierOrganizationId").value(hasItem(DEFAULT_SUPPLIER_ORGANIZATION_ID.intValue())))
            .andExpect(jsonPath("$.[*].primarySupplierUserId").value(hasItem(DEFAULT_PRIMARY_SUPPLIER_USER_ID.intValue())));

        // Check, that the count call also returns 1
        restOrderMockMvc.perform(get("/api/orders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderShouldNotBeFound(String filter) throws Exception {
        restOrderMockMvc.perform(get("/api/orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderMockMvc.perform(get("/api/orders/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingOrder() throws Exception {
        // Get the order
        restOrderMockMvc.perform(get("/api/orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrder() throws Exception {
        // Initialize the database
        orderService.save(order);

        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order
        Order updatedOrder = orderRepository.findById(order.getId()).get();
        // Disconnect from session so that the updates on updatedOrder are not directly saved in db
        em.detach(updatedOrder);
        updatedOrder
            .name(UPDATED_NAME)
            .uuid(UPDATED_UUID)
            .dateCreated(UPDATED_DATE_CREATED)
            .createdBy(UPDATED_CREATED_BY)
            .dateUpdated(UPDATED_DATE_UPDATED)
            .updatedBy(UPDATED_UPDATED_BY)
            .buyerUserId(UPDATED_BUYER_USER_ID)
            .buyerOrganizationId(UPDATED_BUYER_ORGANIZATION_ID)
            .supplierOrganizationId(UPDATED_SUPPLIER_ORGANIZATION_ID)
            .primarySupplierUserId(UPDATED_PRIMARY_SUPPLIER_USER_ID);

        restOrderMockMvc.perform(put("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrder)))
            .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrder.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testOrder.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testOrder.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOrder.getDateUpdated()).isEqualTo(UPDATED_DATE_UPDATED);
        assertThat(testOrder.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testOrder.getBuyerUserId()).isEqualTo(UPDATED_BUYER_USER_ID);
        assertThat(testOrder.getBuyerOrganizationId()).isEqualTo(UPDATED_BUYER_ORGANIZATION_ID);
        assertThat(testOrder.getSupplierOrganizationId()).isEqualTo(UPDATED_SUPPLIER_ORGANIZATION_ID);
        assertThat(testOrder.getPrimarySupplierUserId()).isEqualTo(UPDATED_PRIMARY_SUPPLIER_USER_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMockMvc.perform(put("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrder() throws Exception {
        // Initialize the database
        orderService.save(order);

        int databaseSizeBeforeDelete = orderRepository.findAll().size();

        // Delete the order
        restOrderMockMvc.perform(delete("/api/orders/{id}", order.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
