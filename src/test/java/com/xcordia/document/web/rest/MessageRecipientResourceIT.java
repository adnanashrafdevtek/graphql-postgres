package com.xcordia.document.web.rest;

import com.xcordia.document.JhipsterApp;
import com.xcordia.document.domain.MessageRecipient;
import com.xcordia.document.domain.Message;
import com.xcordia.document.repository.MessageRecipientRepository;
import com.xcordia.document.service.MessageRecipientService;
import com.xcordia.document.service.dto.MessageRecipientCriteria;
import com.xcordia.document.service.MessageRecipientQueryService;

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
 * Integration tests for the {@link MessageRecipientResource} REST controller.
 */
@SpringBootTest(classes = JhipsterApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class MessageRecipientResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;
    private static final Long SMALLER_USER_ID = 1L - 1L;

    private static final Boolean DEFAULT_READ = false;
    private static final Boolean UPDATED_READ = true;

    private static final Instant DEFAULT_DATE_MESSAGE_READ = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_MESSAGE_READ = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private MessageRecipientRepository messageRecipientRepository;

    @Autowired
    private MessageRecipientService messageRecipientService;

    @Autowired
    private MessageRecipientQueryService messageRecipientQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMessageRecipientMockMvc;

    private MessageRecipient messageRecipient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MessageRecipient createEntity(EntityManager em) {
        MessageRecipient messageRecipient = new MessageRecipient()
            .userId(DEFAULT_USER_ID)
            .read(DEFAULT_READ)
            .dateMessageRead(DEFAULT_DATE_MESSAGE_READ);
        return messageRecipient;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MessageRecipient createUpdatedEntity(EntityManager em) {
        MessageRecipient messageRecipient = new MessageRecipient()
            .userId(UPDATED_USER_ID)
            .read(UPDATED_READ)
            .dateMessageRead(UPDATED_DATE_MESSAGE_READ);
        return messageRecipient;
    }

    @BeforeEach
    public void initTest() {
        messageRecipient = createEntity(em);
    }

    @Test
    @Transactional
    public void createMessageRecipient() throws Exception {
        int databaseSizeBeforeCreate = messageRecipientRepository.findAll().size();
        // Create the MessageRecipient
        restMessageRecipientMockMvc.perform(post("/api/message-recipients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(messageRecipient)))
            .andExpect(status().isCreated());

        // Validate the MessageRecipient in the database
        List<MessageRecipient> messageRecipientList = messageRecipientRepository.findAll();
        assertThat(messageRecipientList).hasSize(databaseSizeBeforeCreate + 1);
        MessageRecipient testMessageRecipient = messageRecipientList.get(messageRecipientList.size() - 1);
        assertThat(testMessageRecipient.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testMessageRecipient.isRead()).isEqualTo(DEFAULT_READ);
        assertThat(testMessageRecipient.getDateMessageRead()).isEqualTo(DEFAULT_DATE_MESSAGE_READ);
    }

    @Test
    @Transactional
    public void createMessageRecipientWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = messageRecipientRepository.findAll().size();

        // Create the MessageRecipient with an existing ID
        messageRecipient.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMessageRecipientMockMvc.perform(post("/api/message-recipients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(messageRecipient)))
            .andExpect(status().isBadRequest());

        // Validate the MessageRecipient in the database
        List<MessageRecipient> messageRecipientList = messageRecipientRepository.findAll();
        assertThat(messageRecipientList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageRecipientRepository.findAll().size();
        // set the field null
        messageRecipient.setUserId(null);

        // Create the MessageRecipient, which fails.


        restMessageRecipientMockMvc.perform(post("/api/message-recipients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(messageRecipient)))
            .andExpect(status().isBadRequest());

        List<MessageRecipient> messageRecipientList = messageRecipientRepository.findAll();
        assertThat(messageRecipientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReadIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageRecipientRepository.findAll().size();
        // set the field null
        messageRecipient.setRead(null);

        // Create the MessageRecipient, which fails.


        restMessageRecipientMockMvc.perform(post("/api/message-recipients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(messageRecipient)))
            .andExpect(status().isBadRequest());

        List<MessageRecipient> messageRecipientList = messageRecipientRepository.findAll();
        assertThat(messageRecipientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMessageRecipients() throws Exception {
        // Initialize the database
        messageRecipientRepository.saveAndFlush(messageRecipient);

        // Get all the messageRecipientList
        restMessageRecipientMockMvc.perform(get("/api/message-recipients?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messageRecipient.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].read").value(hasItem(DEFAULT_READ.booleanValue())))
            .andExpect(jsonPath("$.[*].dateMessageRead").value(hasItem(DEFAULT_DATE_MESSAGE_READ.toString())));
    }
    
    @Test
    @Transactional
    public void getMessageRecipient() throws Exception {
        // Initialize the database
        messageRecipientRepository.saveAndFlush(messageRecipient);

        // Get the messageRecipient
        restMessageRecipientMockMvc.perform(get("/api/message-recipients/{id}", messageRecipient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(messageRecipient.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.read").value(DEFAULT_READ.booleanValue()))
            .andExpect(jsonPath("$.dateMessageRead").value(DEFAULT_DATE_MESSAGE_READ.toString()));
    }


    @Test
    @Transactional
    public void getMessageRecipientsByIdFiltering() throws Exception {
        // Initialize the database
        messageRecipientRepository.saveAndFlush(messageRecipient);

        Long id = messageRecipient.getId();

        defaultMessageRecipientShouldBeFound("id.equals=" + id);
        defaultMessageRecipientShouldNotBeFound("id.notEquals=" + id);

        defaultMessageRecipientShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMessageRecipientShouldNotBeFound("id.greaterThan=" + id);

        defaultMessageRecipientShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMessageRecipientShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllMessageRecipientsByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        messageRecipientRepository.saveAndFlush(messageRecipient);

        // Get all the messageRecipientList where userId equals to DEFAULT_USER_ID
        defaultMessageRecipientShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the messageRecipientList where userId equals to UPDATED_USER_ID
        defaultMessageRecipientShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllMessageRecipientsByUserIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        messageRecipientRepository.saveAndFlush(messageRecipient);

        // Get all the messageRecipientList where userId not equals to DEFAULT_USER_ID
        defaultMessageRecipientShouldNotBeFound("userId.notEquals=" + DEFAULT_USER_ID);

        // Get all the messageRecipientList where userId not equals to UPDATED_USER_ID
        defaultMessageRecipientShouldBeFound("userId.notEquals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllMessageRecipientsByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        messageRecipientRepository.saveAndFlush(messageRecipient);

        // Get all the messageRecipientList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultMessageRecipientShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the messageRecipientList where userId equals to UPDATED_USER_ID
        defaultMessageRecipientShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllMessageRecipientsByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageRecipientRepository.saveAndFlush(messageRecipient);

        // Get all the messageRecipientList where userId is not null
        defaultMessageRecipientShouldBeFound("userId.specified=true");

        // Get all the messageRecipientList where userId is null
        defaultMessageRecipientShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    public void getAllMessageRecipientsByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        messageRecipientRepository.saveAndFlush(messageRecipient);

        // Get all the messageRecipientList where userId is greater than or equal to DEFAULT_USER_ID
        defaultMessageRecipientShouldBeFound("userId.greaterThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the messageRecipientList where userId is greater than or equal to UPDATED_USER_ID
        defaultMessageRecipientShouldNotBeFound("userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllMessageRecipientsByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        messageRecipientRepository.saveAndFlush(messageRecipient);

        // Get all the messageRecipientList where userId is less than or equal to DEFAULT_USER_ID
        defaultMessageRecipientShouldBeFound("userId.lessThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the messageRecipientList where userId is less than or equal to SMALLER_USER_ID
        defaultMessageRecipientShouldNotBeFound("userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllMessageRecipientsByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        messageRecipientRepository.saveAndFlush(messageRecipient);

        // Get all the messageRecipientList where userId is less than DEFAULT_USER_ID
        defaultMessageRecipientShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the messageRecipientList where userId is less than UPDATED_USER_ID
        defaultMessageRecipientShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllMessageRecipientsByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        messageRecipientRepository.saveAndFlush(messageRecipient);

        // Get all the messageRecipientList where userId is greater than DEFAULT_USER_ID
        defaultMessageRecipientShouldNotBeFound("userId.greaterThan=" + DEFAULT_USER_ID);

        // Get all the messageRecipientList where userId is greater than SMALLER_USER_ID
        defaultMessageRecipientShouldBeFound("userId.greaterThan=" + SMALLER_USER_ID);
    }


    @Test
    @Transactional
    public void getAllMessageRecipientsByReadIsEqualToSomething() throws Exception {
        // Initialize the database
        messageRecipientRepository.saveAndFlush(messageRecipient);

        // Get all the messageRecipientList where read equals to DEFAULT_READ
        defaultMessageRecipientShouldBeFound("read.equals=" + DEFAULT_READ);

        // Get all the messageRecipientList where read equals to UPDATED_READ
        defaultMessageRecipientShouldNotBeFound("read.equals=" + UPDATED_READ);
    }

    @Test
    @Transactional
    public void getAllMessageRecipientsByReadIsNotEqualToSomething() throws Exception {
        // Initialize the database
        messageRecipientRepository.saveAndFlush(messageRecipient);

        // Get all the messageRecipientList where read not equals to DEFAULT_READ
        defaultMessageRecipientShouldNotBeFound("read.notEquals=" + DEFAULT_READ);

        // Get all the messageRecipientList where read not equals to UPDATED_READ
        defaultMessageRecipientShouldBeFound("read.notEquals=" + UPDATED_READ);
    }

    @Test
    @Transactional
    public void getAllMessageRecipientsByReadIsInShouldWork() throws Exception {
        // Initialize the database
        messageRecipientRepository.saveAndFlush(messageRecipient);

        // Get all the messageRecipientList where read in DEFAULT_READ or UPDATED_READ
        defaultMessageRecipientShouldBeFound("read.in=" + DEFAULT_READ + "," + UPDATED_READ);

        // Get all the messageRecipientList where read equals to UPDATED_READ
        defaultMessageRecipientShouldNotBeFound("read.in=" + UPDATED_READ);
    }

    @Test
    @Transactional
    public void getAllMessageRecipientsByReadIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageRecipientRepository.saveAndFlush(messageRecipient);

        // Get all the messageRecipientList where read is not null
        defaultMessageRecipientShouldBeFound("read.specified=true");

        // Get all the messageRecipientList where read is null
        defaultMessageRecipientShouldNotBeFound("read.specified=false");
    }

    @Test
    @Transactional
    public void getAllMessageRecipientsByDateMessageReadIsEqualToSomething() throws Exception {
        // Initialize the database
        messageRecipientRepository.saveAndFlush(messageRecipient);

        // Get all the messageRecipientList where dateMessageRead equals to DEFAULT_DATE_MESSAGE_READ
        defaultMessageRecipientShouldBeFound("dateMessageRead.equals=" + DEFAULT_DATE_MESSAGE_READ);

        // Get all the messageRecipientList where dateMessageRead equals to UPDATED_DATE_MESSAGE_READ
        defaultMessageRecipientShouldNotBeFound("dateMessageRead.equals=" + UPDATED_DATE_MESSAGE_READ);
    }

    @Test
    @Transactional
    public void getAllMessageRecipientsByDateMessageReadIsNotEqualToSomething() throws Exception {
        // Initialize the database
        messageRecipientRepository.saveAndFlush(messageRecipient);

        // Get all the messageRecipientList where dateMessageRead not equals to DEFAULT_DATE_MESSAGE_READ
        defaultMessageRecipientShouldNotBeFound("dateMessageRead.notEquals=" + DEFAULT_DATE_MESSAGE_READ);

        // Get all the messageRecipientList where dateMessageRead not equals to UPDATED_DATE_MESSAGE_READ
        defaultMessageRecipientShouldBeFound("dateMessageRead.notEquals=" + UPDATED_DATE_MESSAGE_READ);
    }

    @Test
    @Transactional
    public void getAllMessageRecipientsByDateMessageReadIsInShouldWork() throws Exception {
        // Initialize the database
        messageRecipientRepository.saveAndFlush(messageRecipient);

        // Get all the messageRecipientList where dateMessageRead in DEFAULT_DATE_MESSAGE_READ or UPDATED_DATE_MESSAGE_READ
        defaultMessageRecipientShouldBeFound("dateMessageRead.in=" + DEFAULT_DATE_MESSAGE_READ + "," + UPDATED_DATE_MESSAGE_READ);

        // Get all the messageRecipientList where dateMessageRead equals to UPDATED_DATE_MESSAGE_READ
        defaultMessageRecipientShouldNotBeFound("dateMessageRead.in=" + UPDATED_DATE_MESSAGE_READ);
    }

    @Test
    @Transactional
    public void getAllMessageRecipientsByDateMessageReadIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageRecipientRepository.saveAndFlush(messageRecipient);

        // Get all the messageRecipientList where dateMessageRead is not null
        defaultMessageRecipientShouldBeFound("dateMessageRead.specified=true");

        // Get all the messageRecipientList where dateMessageRead is null
        defaultMessageRecipientShouldNotBeFound("dateMessageRead.specified=false");
    }

    @Test
    @Transactional
    public void getAllMessageRecipientsByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        messageRecipientRepository.saveAndFlush(messageRecipient);
        Message message = MessageResourceIT.createEntity(em);
        em.persist(message);
        em.flush();
        messageRecipient.setMessage(message);
        messageRecipientRepository.saveAndFlush(messageRecipient);
        Long messageId = message.getId();

        // Get all the messageRecipientList where message equals to messageId
        defaultMessageRecipientShouldBeFound("messageId.equals=" + messageId);

        // Get all the messageRecipientList where message equals to messageId + 1
        defaultMessageRecipientShouldNotBeFound("messageId.equals=" + (messageId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMessageRecipientShouldBeFound(String filter) throws Exception {
        restMessageRecipientMockMvc.perform(get("/api/message-recipients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messageRecipient.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].read").value(hasItem(DEFAULT_READ.booleanValue())))
            .andExpect(jsonPath("$.[*].dateMessageRead").value(hasItem(DEFAULT_DATE_MESSAGE_READ.toString())));

        // Check, that the count call also returns 1
        restMessageRecipientMockMvc.perform(get("/api/message-recipients/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMessageRecipientShouldNotBeFound(String filter) throws Exception {
        restMessageRecipientMockMvc.perform(get("/api/message-recipients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMessageRecipientMockMvc.perform(get("/api/message-recipients/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingMessageRecipient() throws Exception {
        // Get the messageRecipient
        restMessageRecipientMockMvc.perform(get("/api/message-recipients/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMessageRecipient() throws Exception {
        // Initialize the database
        messageRecipientService.save(messageRecipient);

        int databaseSizeBeforeUpdate = messageRecipientRepository.findAll().size();

        // Update the messageRecipient
        MessageRecipient updatedMessageRecipient = messageRecipientRepository.findById(messageRecipient.getId()).get();
        // Disconnect from session so that the updates on updatedMessageRecipient are not directly saved in db
        em.detach(updatedMessageRecipient);
        updatedMessageRecipient
            .userId(UPDATED_USER_ID)
            .read(UPDATED_READ)
            .dateMessageRead(UPDATED_DATE_MESSAGE_READ);

        restMessageRecipientMockMvc.perform(put("/api/message-recipients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedMessageRecipient)))
            .andExpect(status().isOk());

        // Validate the MessageRecipient in the database
        List<MessageRecipient> messageRecipientList = messageRecipientRepository.findAll();
        assertThat(messageRecipientList).hasSize(databaseSizeBeforeUpdate);
        MessageRecipient testMessageRecipient = messageRecipientList.get(messageRecipientList.size() - 1);
        assertThat(testMessageRecipient.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testMessageRecipient.isRead()).isEqualTo(UPDATED_READ);
        assertThat(testMessageRecipient.getDateMessageRead()).isEqualTo(UPDATED_DATE_MESSAGE_READ);
    }

    @Test
    @Transactional
    public void updateNonExistingMessageRecipient() throws Exception {
        int databaseSizeBeforeUpdate = messageRecipientRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessageRecipientMockMvc.perform(put("/api/message-recipients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(messageRecipient)))
            .andExpect(status().isBadRequest());

        // Validate the MessageRecipient in the database
        List<MessageRecipient> messageRecipientList = messageRecipientRepository.findAll();
        assertThat(messageRecipientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMessageRecipient() throws Exception {
        // Initialize the database
        messageRecipientService.save(messageRecipient);

        int databaseSizeBeforeDelete = messageRecipientRepository.findAll().size();

        // Delete the messageRecipient
        restMessageRecipientMockMvc.perform(delete("/api/message-recipients/{id}", messageRecipient.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MessageRecipient> messageRecipientList = messageRecipientRepository.findAll();
        assertThat(messageRecipientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
