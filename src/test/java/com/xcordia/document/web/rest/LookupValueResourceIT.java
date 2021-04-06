package com.xcordia.document.web.rest;

import com.xcordia.document.JhipsterApp;
import com.xcordia.document.domain.LookupValue;
import com.xcordia.document.domain.Lookup;
import com.xcordia.document.repository.LookupValueRepository;
import com.xcordia.document.service.LookupValueService;
import com.xcordia.document.service.dto.LookupValueCriteria;
import com.xcordia.document.service.LookupValueQueryService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link LookupValueResource} REST controller.
 */
@SpringBootTest(classes = JhipsterApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class LookupValueResourceIT {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private LookupValueRepository lookupValueRepository;

    @Autowired
    private LookupValueService lookupValueService;

    @Autowired
    private LookupValueQueryService lookupValueQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLookupValueMockMvc;

    private LookupValue lookupValue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LookupValue createEntity(EntityManager em) {
        LookupValue lookupValue = new LookupValue()
            .value(DEFAULT_VALUE);
        return lookupValue;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LookupValue createUpdatedEntity(EntityManager em) {
        LookupValue lookupValue = new LookupValue()
            .value(UPDATED_VALUE);
        return lookupValue;
    }

    @BeforeEach
    public void initTest() {
        lookupValue = createEntity(em);
    }

    @Test
    @Transactional
    public void createLookupValue() throws Exception {
        int databaseSizeBeforeCreate = lookupValueRepository.findAll().size();
        // Create the LookupValue
        restLookupValueMockMvc.perform(post("/api/lookup-values")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lookupValue)))
            .andExpect(status().isCreated());

        // Validate the LookupValue in the database
        List<LookupValue> lookupValueList = lookupValueRepository.findAll();
        assertThat(lookupValueList).hasSize(databaseSizeBeforeCreate + 1);
        LookupValue testLookupValue = lookupValueList.get(lookupValueList.size() - 1);
        assertThat(testLookupValue.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createLookupValueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lookupValueRepository.findAll().size();

        // Create the LookupValue with an existing ID
        lookupValue.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLookupValueMockMvc.perform(post("/api/lookup-values")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lookupValue)))
            .andExpect(status().isBadRequest());

        // Validate the LookupValue in the database
        List<LookupValue> lookupValueList = lookupValueRepository.findAll();
        assertThat(lookupValueList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = lookupValueRepository.findAll().size();
        // set the field null
        lookupValue.setValue(null);

        // Create the LookupValue, which fails.


        restLookupValueMockMvc.perform(post("/api/lookup-values")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lookupValue)))
            .andExpect(status().isBadRequest());

        List<LookupValue> lookupValueList = lookupValueRepository.findAll();
        assertThat(lookupValueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLookupValues() throws Exception {
        // Initialize the database
        lookupValueRepository.saveAndFlush(lookupValue);

        // Get all the lookupValueList
        restLookupValueMockMvc.perform(get("/api/lookup-values?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lookupValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }
    
    @Test
    @Transactional
    public void getLookupValue() throws Exception {
        // Initialize the database
        lookupValueRepository.saveAndFlush(lookupValue);

        // Get the lookupValue
        restLookupValueMockMvc.perform(get("/api/lookup-values/{id}", lookupValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lookupValue.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }


    @Test
    @Transactional
    public void getLookupValuesByIdFiltering() throws Exception {
        // Initialize the database
        lookupValueRepository.saveAndFlush(lookupValue);

        Long id = lookupValue.getId();

        defaultLookupValueShouldBeFound("id.equals=" + id);
        defaultLookupValueShouldNotBeFound("id.notEquals=" + id);

        defaultLookupValueShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLookupValueShouldNotBeFound("id.greaterThan=" + id);

        defaultLookupValueShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLookupValueShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllLookupValuesByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        lookupValueRepository.saveAndFlush(lookupValue);

        // Get all the lookupValueList where value equals to DEFAULT_VALUE
        defaultLookupValueShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the lookupValueList where value equals to UPDATED_VALUE
        defaultLookupValueShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllLookupValuesByValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lookupValueRepository.saveAndFlush(lookupValue);

        // Get all the lookupValueList where value not equals to DEFAULT_VALUE
        defaultLookupValueShouldNotBeFound("value.notEquals=" + DEFAULT_VALUE);

        // Get all the lookupValueList where value not equals to UPDATED_VALUE
        defaultLookupValueShouldBeFound("value.notEquals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllLookupValuesByValueIsInShouldWork() throws Exception {
        // Initialize the database
        lookupValueRepository.saveAndFlush(lookupValue);

        // Get all the lookupValueList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultLookupValueShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the lookupValueList where value equals to UPDATED_VALUE
        defaultLookupValueShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllLookupValuesByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        lookupValueRepository.saveAndFlush(lookupValue);

        // Get all the lookupValueList where value is not null
        defaultLookupValueShouldBeFound("value.specified=true");

        // Get all the lookupValueList where value is null
        defaultLookupValueShouldNotBeFound("value.specified=false");
    }
                @Test
    @Transactional
    public void getAllLookupValuesByValueContainsSomething() throws Exception {
        // Initialize the database
        lookupValueRepository.saveAndFlush(lookupValue);

        // Get all the lookupValueList where value contains DEFAULT_VALUE
        defaultLookupValueShouldBeFound("value.contains=" + DEFAULT_VALUE);

        // Get all the lookupValueList where value contains UPDATED_VALUE
        defaultLookupValueShouldNotBeFound("value.contains=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllLookupValuesByValueNotContainsSomething() throws Exception {
        // Initialize the database
        lookupValueRepository.saveAndFlush(lookupValue);

        // Get all the lookupValueList where value does not contain DEFAULT_VALUE
        defaultLookupValueShouldNotBeFound("value.doesNotContain=" + DEFAULT_VALUE);

        // Get all the lookupValueList where value does not contain UPDATED_VALUE
        defaultLookupValueShouldBeFound("value.doesNotContain=" + UPDATED_VALUE);
    }


    @Test
    @Transactional
    public void getAllLookupValuesByLookupIsEqualToSomething() throws Exception {
        // Initialize the database
        lookupValueRepository.saveAndFlush(lookupValue);
        Lookup lookup = LookupResourceIT.createEntity(em);
        em.persist(lookup);
        em.flush();
        lookupValue.setLookup(lookup);
        lookupValueRepository.saveAndFlush(lookupValue);
        Long lookupId = lookup.getId();

        // Get all the lookupValueList where lookup equals to lookupId
        defaultLookupValueShouldBeFound("lookupId.equals=" + lookupId);

        // Get all the lookupValueList where lookup equals to lookupId + 1
        defaultLookupValueShouldNotBeFound("lookupId.equals=" + (lookupId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLookupValueShouldBeFound(String filter) throws Exception {
        restLookupValueMockMvc.perform(get("/api/lookup-values?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lookupValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));

        // Check, that the count call also returns 1
        restLookupValueMockMvc.perform(get("/api/lookup-values/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLookupValueShouldNotBeFound(String filter) throws Exception {
        restLookupValueMockMvc.perform(get("/api/lookup-values?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLookupValueMockMvc.perform(get("/api/lookup-values/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingLookupValue() throws Exception {
        // Get the lookupValue
        restLookupValueMockMvc.perform(get("/api/lookup-values/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLookupValue() throws Exception {
        // Initialize the database
        lookupValueService.save(lookupValue);

        int databaseSizeBeforeUpdate = lookupValueRepository.findAll().size();

        // Update the lookupValue
        LookupValue updatedLookupValue = lookupValueRepository.findById(lookupValue.getId()).get();
        // Disconnect from session so that the updates on updatedLookupValue are not directly saved in db
        em.detach(updatedLookupValue);
        updatedLookupValue
            .value(UPDATED_VALUE);

        restLookupValueMockMvc.perform(put("/api/lookup-values")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedLookupValue)))
            .andExpect(status().isOk());

        // Validate the LookupValue in the database
        List<LookupValue> lookupValueList = lookupValueRepository.findAll();
        assertThat(lookupValueList).hasSize(databaseSizeBeforeUpdate);
        LookupValue testLookupValue = lookupValueList.get(lookupValueList.size() - 1);
        assertThat(testLookupValue.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingLookupValue() throws Exception {
        int databaseSizeBeforeUpdate = lookupValueRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLookupValueMockMvc.perform(put("/api/lookup-values")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lookupValue)))
            .andExpect(status().isBadRequest());

        // Validate the LookupValue in the database
        List<LookupValue> lookupValueList = lookupValueRepository.findAll();
        assertThat(lookupValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLookupValue() throws Exception {
        // Initialize the database
        lookupValueService.save(lookupValue);

        int databaseSizeBeforeDelete = lookupValueRepository.findAll().size();

        // Delete the lookupValue
        restLookupValueMockMvc.perform(delete("/api/lookup-values/{id}", lookupValue.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LookupValue> lookupValueList = lookupValueRepository.findAll();
        assertThat(lookupValueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
