package com.xcordia.document.web.rest;

import com.xcordia.document.JhipsterApp;
import com.xcordia.document.domain.OrderItem;
import com.xcordia.document.domain.Order;
import com.xcordia.document.repository.OrderItemRepository;
import com.xcordia.document.service.OrderItemService;
import com.xcordia.document.service.dto.OrderItemCriteria;
import com.xcordia.document.service.OrderItemQueryService;

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
 * Integration tests for the {@link OrderItemResource} REST controller.
 */
@SpringBootTest(classes = JhipsterApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class OrderItemResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_CATALOG_ITEM_ID = 1L;
    private static final Long UPDATED_CATALOG_ITEM_ID = 2L;
    private static final Long SMALLER_CATALOG_ITEM_ID = 1L - 1L;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderItemQueryService orderItemQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderItemMockMvc;

    private OrderItem orderItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItem createEntity(EntityManager em) {
        OrderItem orderItem = new OrderItem()
            .name(DEFAULT_NAME)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateUpdated(DEFAULT_DATE_UPDATED)
            .catalogItemId(DEFAULT_CATALOG_ITEM_ID);
        return orderItem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderItem createUpdatedEntity(EntityManager em) {
        OrderItem orderItem = new OrderItem()
            .name(UPDATED_NAME)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED)
            .catalogItemId(UPDATED_CATALOG_ITEM_ID);
        return orderItem;
    }

    @BeforeEach
    public void initTest() {
        orderItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrderItem() throws Exception {
        int databaseSizeBeforeCreate = orderItemRepository.findAll().size();
        // Create the OrderItem
        restOrderItemMockMvc.perform(post("/api/order-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderItem)))
            .andExpect(status().isCreated());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeCreate + 1);
        OrderItem testOrderItem = orderItemList.get(orderItemList.size() - 1);
        assertThat(testOrderItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrderItem.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testOrderItem.getDateUpdated()).isEqualTo(DEFAULT_DATE_UPDATED);
        assertThat(testOrderItem.getCatalogItemId()).isEqualTo(DEFAULT_CATALOG_ITEM_ID);
    }

    @Test
    @Transactional
    public void createOrderItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orderItemRepository.findAll().size();

        // Create the OrderItem with an existing ID
        orderItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderItemMockMvc.perform(post("/api/order-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderItem)))
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();
        // set the field null
        orderItem.setName(null);

        // Create the OrderItem, which fails.


        restOrderItemMockMvc.perform(post("/api/order-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderItem)))
            .andExpect(status().isBadRequest());

        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCatalogItemIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderItemRepository.findAll().size();
        // set the field null
        orderItem.setCatalogItemId(null);

        // Create the OrderItem, which fails.


        restOrderItemMockMvc.perform(post("/api/order-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderItem)))
            .andExpect(status().isBadRequest());

        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrderItems() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList
        restOrderItemMockMvc.perform(get("/api/order-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].catalogItemId").value(hasItem(DEFAULT_CATALOG_ITEM_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getOrderItem() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get the orderItem
        restOrderItemMockMvc.perform(get("/api/order-items/{id}", orderItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderItem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateUpdated").value(DEFAULT_DATE_UPDATED.toString()))
            .andExpect(jsonPath("$.catalogItemId").value(DEFAULT_CATALOG_ITEM_ID.intValue()));
    }


    @Test
    @Transactional
    public void getOrderItemsByIdFiltering() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        Long id = orderItem.getId();

        defaultOrderItemShouldBeFound("id.equals=" + id);
        defaultOrderItemShouldNotBeFound("id.notEquals=" + id);

        defaultOrderItemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrderItemShouldNotBeFound("id.greaterThan=" + id);

        defaultOrderItemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrderItemShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllOrderItemsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where name equals to DEFAULT_NAME
        defaultOrderItemShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the orderItemList where name equals to UPDATED_NAME
        defaultOrderItemShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrderItemsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where name not equals to DEFAULT_NAME
        defaultOrderItemShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the orderItemList where name not equals to UPDATED_NAME
        defaultOrderItemShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrderItemsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOrderItemShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the orderItemList where name equals to UPDATED_NAME
        defaultOrderItemShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrderItemsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where name is not null
        defaultOrderItemShouldBeFound("name.specified=true");

        // Get all the orderItemList where name is null
        defaultOrderItemShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllOrderItemsByNameContainsSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where name contains DEFAULT_NAME
        defaultOrderItemShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the orderItemList where name contains UPDATED_NAME
        defaultOrderItemShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrderItemsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where name does not contain DEFAULT_NAME
        defaultOrderItemShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the orderItemList where name does not contain UPDATED_NAME
        defaultOrderItemShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllOrderItemsByDateCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where dateCreated equals to DEFAULT_DATE_CREATED
        defaultOrderItemShouldBeFound("dateCreated.equals=" + DEFAULT_DATE_CREATED);

        // Get all the orderItemList where dateCreated equals to UPDATED_DATE_CREATED
        defaultOrderItemShouldNotBeFound("dateCreated.equals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllOrderItemsByDateCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where dateCreated not equals to DEFAULT_DATE_CREATED
        defaultOrderItemShouldNotBeFound("dateCreated.notEquals=" + DEFAULT_DATE_CREATED);

        // Get all the orderItemList where dateCreated not equals to UPDATED_DATE_CREATED
        defaultOrderItemShouldBeFound("dateCreated.notEquals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllOrderItemsByDateCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where dateCreated in DEFAULT_DATE_CREATED or UPDATED_DATE_CREATED
        defaultOrderItemShouldBeFound("dateCreated.in=" + DEFAULT_DATE_CREATED + "," + UPDATED_DATE_CREATED);

        // Get all the orderItemList where dateCreated equals to UPDATED_DATE_CREATED
        defaultOrderItemShouldNotBeFound("dateCreated.in=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllOrderItemsByDateCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where dateCreated is not null
        defaultOrderItemShouldBeFound("dateCreated.specified=true");

        // Get all the orderItemList where dateCreated is null
        defaultOrderItemShouldNotBeFound("dateCreated.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrderItemsByDateUpdatedIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where dateUpdated equals to DEFAULT_DATE_UPDATED
        defaultOrderItemShouldBeFound("dateUpdated.equals=" + DEFAULT_DATE_UPDATED);

        // Get all the orderItemList where dateUpdated equals to UPDATED_DATE_UPDATED
        defaultOrderItemShouldNotBeFound("dateUpdated.equals=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllOrderItemsByDateUpdatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where dateUpdated not equals to DEFAULT_DATE_UPDATED
        defaultOrderItemShouldNotBeFound("dateUpdated.notEquals=" + DEFAULT_DATE_UPDATED);

        // Get all the orderItemList where dateUpdated not equals to UPDATED_DATE_UPDATED
        defaultOrderItemShouldBeFound("dateUpdated.notEquals=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllOrderItemsByDateUpdatedIsInShouldWork() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where dateUpdated in DEFAULT_DATE_UPDATED or UPDATED_DATE_UPDATED
        defaultOrderItemShouldBeFound("dateUpdated.in=" + DEFAULT_DATE_UPDATED + "," + UPDATED_DATE_UPDATED);

        // Get all the orderItemList where dateUpdated equals to UPDATED_DATE_UPDATED
        defaultOrderItemShouldNotBeFound("dateUpdated.in=" + UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void getAllOrderItemsByDateUpdatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where dateUpdated is not null
        defaultOrderItemShouldBeFound("dateUpdated.specified=true");

        // Get all the orderItemList where dateUpdated is null
        defaultOrderItemShouldNotBeFound("dateUpdated.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrderItemsByCatalogItemIdIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where catalogItemId equals to DEFAULT_CATALOG_ITEM_ID
        defaultOrderItemShouldBeFound("catalogItemId.equals=" + DEFAULT_CATALOG_ITEM_ID);

        // Get all the orderItemList where catalogItemId equals to UPDATED_CATALOG_ITEM_ID
        defaultOrderItemShouldNotBeFound("catalogItemId.equals=" + UPDATED_CATALOG_ITEM_ID);
    }

    @Test
    @Transactional
    public void getAllOrderItemsByCatalogItemIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where catalogItemId not equals to DEFAULT_CATALOG_ITEM_ID
        defaultOrderItemShouldNotBeFound("catalogItemId.notEquals=" + DEFAULT_CATALOG_ITEM_ID);

        // Get all the orderItemList where catalogItemId not equals to UPDATED_CATALOG_ITEM_ID
        defaultOrderItemShouldBeFound("catalogItemId.notEquals=" + UPDATED_CATALOG_ITEM_ID);
    }

    @Test
    @Transactional
    public void getAllOrderItemsByCatalogItemIdIsInShouldWork() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where catalogItemId in DEFAULT_CATALOG_ITEM_ID or UPDATED_CATALOG_ITEM_ID
        defaultOrderItemShouldBeFound("catalogItemId.in=" + DEFAULT_CATALOG_ITEM_ID + "," + UPDATED_CATALOG_ITEM_ID);

        // Get all the orderItemList where catalogItemId equals to UPDATED_CATALOG_ITEM_ID
        defaultOrderItemShouldNotBeFound("catalogItemId.in=" + UPDATED_CATALOG_ITEM_ID);
    }

    @Test
    @Transactional
    public void getAllOrderItemsByCatalogItemIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where catalogItemId is not null
        defaultOrderItemShouldBeFound("catalogItemId.specified=true");

        // Get all the orderItemList where catalogItemId is null
        defaultOrderItemShouldNotBeFound("catalogItemId.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrderItemsByCatalogItemIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where catalogItemId is greater than or equal to DEFAULT_CATALOG_ITEM_ID
        defaultOrderItemShouldBeFound("catalogItemId.greaterThanOrEqual=" + DEFAULT_CATALOG_ITEM_ID);

        // Get all the orderItemList where catalogItemId is greater than or equal to UPDATED_CATALOG_ITEM_ID
        defaultOrderItemShouldNotBeFound("catalogItemId.greaterThanOrEqual=" + UPDATED_CATALOG_ITEM_ID);
    }

    @Test
    @Transactional
    public void getAllOrderItemsByCatalogItemIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where catalogItemId is less than or equal to DEFAULT_CATALOG_ITEM_ID
        defaultOrderItemShouldBeFound("catalogItemId.lessThanOrEqual=" + DEFAULT_CATALOG_ITEM_ID);

        // Get all the orderItemList where catalogItemId is less than or equal to SMALLER_CATALOG_ITEM_ID
        defaultOrderItemShouldNotBeFound("catalogItemId.lessThanOrEqual=" + SMALLER_CATALOG_ITEM_ID);
    }

    @Test
    @Transactional
    public void getAllOrderItemsByCatalogItemIdIsLessThanSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where catalogItemId is less than DEFAULT_CATALOG_ITEM_ID
        defaultOrderItemShouldNotBeFound("catalogItemId.lessThan=" + DEFAULT_CATALOG_ITEM_ID);

        // Get all the orderItemList where catalogItemId is less than UPDATED_CATALOG_ITEM_ID
        defaultOrderItemShouldBeFound("catalogItemId.lessThan=" + UPDATED_CATALOG_ITEM_ID);
    }

    @Test
    @Transactional
    public void getAllOrderItemsByCatalogItemIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);

        // Get all the orderItemList where catalogItemId is greater than DEFAULT_CATALOG_ITEM_ID
        defaultOrderItemShouldNotBeFound("catalogItemId.greaterThan=" + DEFAULT_CATALOG_ITEM_ID);

        // Get all the orderItemList where catalogItemId is greater than SMALLER_CATALOG_ITEM_ID
        defaultOrderItemShouldBeFound("catalogItemId.greaterThan=" + SMALLER_CATALOG_ITEM_ID);
    }


    @Test
    @Transactional
    public void getAllOrderItemsByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        orderItemRepository.saveAndFlush(orderItem);
        Order order = OrderResourceIT.createEntity(em);
        em.persist(order);
        em.flush();
        orderItem.setOrder(order);
        orderItemRepository.saveAndFlush(orderItem);
        Long orderId = order.getId();

        // Get all the orderItemList where order equals to orderId
        defaultOrderItemShouldBeFound("orderId.equals=" + orderId);

        // Get all the orderItemList where order equals to orderId + 1
        defaultOrderItemShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderItemShouldBeFound(String filter) throws Exception {
        restOrderItemMockMvc.perform(get("/api/order-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].catalogItemId").value(hasItem(DEFAULT_CATALOG_ITEM_ID.intValue())));

        // Check, that the count call also returns 1
        restOrderItemMockMvc.perform(get("/api/order-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderItemShouldNotBeFound(String filter) throws Exception {
        restOrderItemMockMvc.perform(get("/api/order-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderItemMockMvc.perform(get("/api/order-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingOrderItem() throws Exception {
        // Get the orderItem
        restOrderItemMockMvc.perform(get("/api/order-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderItem() throws Exception {
        // Initialize the database
        orderItemService.save(orderItem);

        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();

        // Update the orderItem
        OrderItem updatedOrderItem = orderItemRepository.findById(orderItem.getId()).get();
        // Disconnect from session so that the updates on updatedOrderItem are not directly saved in db
        em.detach(updatedOrderItem);
        updatedOrderItem
            .name(UPDATED_NAME)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED)
            .catalogItemId(UPDATED_CATALOG_ITEM_ID);

        restOrderItemMockMvc.perform(put("/api/order-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrderItem)))
            .andExpect(status().isOk());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
        OrderItem testOrderItem = orderItemList.get(orderItemList.size() - 1);
        assertThat(testOrderItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrderItem.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testOrderItem.getDateUpdated()).isEqualTo(UPDATED_DATE_UPDATED);
        assertThat(testOrderItem.getCatalogItemId()).isEqualTo(UPDATED_CATALOG_ITEM_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = orderItemRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderItemMockMvc.perform(put("/api/order-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(orderItem)))
            .andExpect(status().isBadRequest());

        // Validate the OrderItem in the database
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrderItem() throws Exception {
        // Initialize the database
        orderItemService.save(orderItem);

        int databaseSizeBeforeDelete = orderItemRepository.findAll().size();

        // Delete the orderItem
        restOrderItemMockMvc.perform(delete("/api/order-items/{id}", orderItem.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        assertThat(orderItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
