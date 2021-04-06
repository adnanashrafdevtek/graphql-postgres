package com.xcordia.document.web.rest;

import com.xcordia.document.JhipsterApp;
import com.xcordia.document.domain.Message;
import com.xcordia.document.domain.MessageRecipient;
import com.xcordia.document.domain.Order;
import com.xcordia.document.repository.MessageRepository;
import com.xcordia.document.service.MessageService;
import com.xcordia.document.service.dto.MessageCriteria;
import com.xcordia.document.service.MessageQueryService;

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
 * Integration tests for the {@link MessageResource} REST controller.
 */
@SpringBootTest(classes = JhipsterApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class MessageResourceIT {

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final Long DEFAULT_LKP_MESSAGE_TYPE_ID = 1L;
    private static final Long UPDATED_LKP_MESSAGE_TYPE_ID = 2L;
    private static final Long SMALLER_LKP_MESSAGE_TYPE_ID = 1L - 1L;

    private static final Long DEFAULT_SENDER_USER_ID = 1L;
    private static final Long UPDATED_SENDER_USER_ID = 2L;
    private static final Long SMALLER_SENDER_USER_ID = 1L - 1L;

    private static final String DEFAULT_SENDER_ALIAS = "AAAAAAAAAA";
    private static final String UPDATED_SENDER_ALIAS = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageQueryService messageQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMessageMockMvc;

    private Message message;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Message createEntity(EntityManager em) {
        Message message = new Message()
            .message(DEFAULT_MESSAGE)
            .lkpMessageTypeId(DEFAULT_LKP_MESSAGE_TYPE_ID)
            .senderUserId(DEFAULT_SENDER_USER_ID)
            .senderAlias(DEFAULT_SENDER_ALIAS)
            .createdBy(DEFAULT_CREATED_BY)
            .dateCreated(DEFAULT_DATE_CREATED);
        return message;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Message createUpdatedEntity(EntityManager em) {
        Message message = new Message()
            .message(UPDATED_MESSAGE)
            .lkpMessageTypeId(UPDATED_LKP_MESSAGE_TYPE_ID)
            .senderUserId(UPDATED_SENDER_USER_ID)
            .senderAlias(UPDATED_SENDER_ALIAS)
            .createdBy(UPDATED_CREATED_BY)
            .dateCreated(UPDATED_DATE_CREATED);
        return message;
    }

    @BeforeEach
    public void initTest() {
        message = createEntity(em);
    }

    @Test
    @Transactional
    public void createMessage() throws Exception {
        int databaseSizeBeforeCreate = messageRepository.findAll().size();
        // Create the Message
        restMessageMockMvc.perform(post("/api/messages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(message)))
            .andExpect(status().isCreated());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeCreate + 1);
        Message testMessage = messageList.get(messageList.size() - 1);
        assertThat(testMessage.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testMessage.getLkpMessageTypeId()).isEqualTo(DEFAULT_LKP_MESSAGE_TYPE_ID);
        assertThat(testMessage.getSenderUserId()).isEqualTo(DEFAULT_SENDER_USER_ID);
        assertThat(testMessage.getSenderAlias()).isEqualTo(DEFAULT_SENDER_ALIAS);
        assertThat(testMessage.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testMessage.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
    }

    @Test
    @Transactional
    public void createMessageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = messageRepository.findAll().size();

        // Create the Message with an existing ID
        message.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMessageMockMvc.perform(post("/api/messages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(message)))
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageRepository.findAll().size();
        // set the field null
        message.setMessage(null);

        // Create the Message, which fails.


        restMessageMockMvc.perform(post("/api/messages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(message)))
            .andExpect(status().isBadRequest());

        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLkpMessageTypeIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageRepository.findAll().size();
        // set the field null
        message.setLkpMessageTypeId(null);

        // Create the Message, which fails.


        restMessageMockMvc.perform(post("/api/messages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(message)))
            .andExpect(status().isBadRequest());

        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSenderUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageRepository.findAll().size();
        // set the field null
        message.setSenderUserId(null);

        // Create the Message, which fails.


        restMessageMockMvc.perform(post("/api/messages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(message)))
            .andExpect(status().isBadRequest());

        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSenderAliasIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageRepository.findAll().size();
        // set the field null
        message.setSenderAlias(null);

        // Create the Message, which fails.


        restMessageMockMvc.perform(post("/api/messages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(message)))
            .andExpect(status().isBadRequest());

        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageRepository.findAll().size();
        // set the field null
        message.setCreatedBy(null);

        // Create the Message, which fails.


        restMessageMockMvc.perform(post("/api/messages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(message)))
            .andExpect(status().isBadRequest());

        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageRepository.findAll().size();
        // set the field null
        message.setDateCreated(null);

        // Create the Message, which fails.


        restMessageMockMvc.perform(post("/api/messages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(message)))
            .andExpect(status().isBadRequest());

        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMessages() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList
        restMessageMockMvc.perform(get("/api/messages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(message.getId().intValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].lkpMessageTypeId").value(hasItem(DEFAULT_LKP_MESSAGE_TYPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].senderUserId").value(hasItem(DEFAULT_SENDER_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].senderAlias").value(hasItem(DEFAULT_SENDER_ALIAS)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())));
    }
    
    @Test
    @Transactional
    public void getMessage() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get the message
        restMessageMockMvc.perform(get("/api/messages/{id}", message.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(message.getId().intValue()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.lkpMessageTypeId").value(DEFAULT_LKP_MESSAGE_TYPE_ID.intValue()))
            .andExpect(jsonPath("$.senderUserId").value(DEFAULT_SENDER_USER_ID.intValue()))
            .andExpect(jsonPath("$.senderAlias").value(DEFAULT_SENDER_ALIAS))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()));
    }


    @Test
    @Transactional
    public void getMessagesByIdFiltering() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        Long id = message.getId();

        defaultMessageShouldBeFound("id.equals=" + id);
        defaultMessageShouldNotBeFound("id.notEquals=" + id);

        defaultMessageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMessageShouldNotBeFound("id.greaterThan=" + id);

        defaultMessageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMessageShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllMessagesByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where message equals to DEFAULT_MESSAGE
        defaultMessageShouldBeFound("message.equals=" + DEFAULT_MESSAGE);

        // Get all the messageList where message equals to UPDATED_MESSAGE
        defaultMessageShouldNotBeFound("message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllMessagesByMessageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where message not equals to DEFAULT_MESSAGE
        defaultMessageShouldNotBeFound("message.notEquals=" + DEFAULT_MESSAGE);

        // Get all the messageList where message not equals to UPDATED_MESSAGE
        defaultMessageShouldBeFound("message.notEquals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllMessagesByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where message in DEFAULT_MESSAGE or UPDATED_MESSAGE
        defaultMessageShouldBeFound("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE);

        // Get all the messageList where message equals to UPDATED_MESSAGE
        defaultMessageShouldNotBeFound("message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllMessagesByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where message is not null
        defaultMessageShouldBeFound("message.specified=true");

        // Get all the messageList where message is null
        defaultMessageShouldNotBeFound("message.specified=false");
    }
                @Test
    @Transactional
    public void getAllMessagesByMessageContainsSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where message contains DEFAULT_MESSAGE
        defaultMessageShouldBeFound("message.contains=" + DEFAULT_MESSAGE);

        // Get all the messageList where message contains UPDATED_MESSAGE
        defaultMessageShouldNotBeFound("message.contains=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllMessagesByMessageNotContainsSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where message does not contain DEFAULT_MESSAGE
        defaultMessageShouldNotBeFound("message.doesNotContain=" + DEFAULT_MESSAGE);

        // Get all the messageList where message does not contain UPDATED_MESSAGE
        defaultMessageShouldBeFound("message.doesNotContain=" + UPDATED_MESSAGE);
    }


    @Test
    @Transactional
    public void getAllMessagesByLkpMessageTypeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where lkpMessageTypeId equals to DEFAULT_LKP_MESSAGE_TYPE_ID
        defaultMessageShouldBeFound("lkpMessageTypeId.equals=" + DEFAULT_LKP_MESSAGE_TYPE_ID);

        // Get all the messageList where lkpMessageTypeId equals to UPDATED_LKP_MESSAGE_TYPE_ID
        defaultMessageShouldNotBeFound("lkpMessageTypeId.equals=" + UPDATED_LKP_MESSAGE_TYPE_ID);
    }

    @Test
    @Transactional
    public void getAllMessagesByLkpMessageTypeIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where lkpMessageTypeId not equals to DEFAULT_LKP_MESSAGE_TYPE_ID
        defaultMessageShouldNotBeFound("lkpMessageTypeId.notEquals=" + DEFAULT_LKP_MESSAGE_TYPE_ID);

        // Get all the messageList where lkpMessageTypeId not equals to UPDATED_LKP_MESSAGE_TYPE_ID
        defaultMessageShouldBeFound("lkpMessageTypeId.notEquals=" + UPDATED_LKP_MESSAGE_TYPE_ID);
    }

    @Test
    @Transactional
    public void getAllMessagesByLkpMessageTypeIdIsInShouldWork() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where lkpMessageTypeId in DEFAULT_LKP_MESSAGE_TYPE_ID or UPDATED_LKP_MESSAGE_TYPE_ID
        defaultMessageShouldBeFound("lkpMessageTypeId.in=" + DEFAULT_LKP_MESSAGE_TYPE_ID + "," + UPDATED_LKP_MESSAGE_TYPE_ID);

        // Get all the messageList where lkpMessageTypeId equals to UPDATED_LKP_MESSAGE_TYPE_ID
        defaultMessageShouldNotBeFound("lkpMessageTypeId.in=" + UPDATED_LKP_MESSAGE_TYPE_ID);
    }

    @Test
    @Transactional
    public void getAllMessagesByLkpMessageTypeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where lkpMessageTypeId is not null
        defaultMessageShouldBeFound("lkpMessageTypeId.specified=true");

        // Get all the messageList where lkpMessageTypeId is null
        defaultMessageShouldNotBeFound("lkpMessageTypeId.specified=false");
    }

    @Test
    @Transactional
    public void getAllMessagesByLkpMessageTypeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where lkpMessageTypeId is greater than or equal to DEFAULT_LKP_MESSAGE_TYPE_ID
        defaultMessageShouldBeFound("lkpMessageTypeId.greaterThanOrEqual=" + DEFAULT_LKP_MESSAGE_TYPE_ID);

        // Get all the messageList where lkpMessageTypeId is greater than or equal to UPDATED_LKP_MESSAGE_TYPE_ID
        defaultMessageShouldNotBeFound("lkpMessageTypeId.greaterThanOrEqual=" + UPDATED_LKP_MESSAGE_TYPE_ID);
    }

    @Test
    @Transactional
    public void getAllMessagesByLkpMessageTypeIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where lkpMessageTypeId is less than or equal to DEFAULT_LKP_MESSAGE_TYPE_ID
        defaultMessageShouldBeFound("lkpMessageTypeId.lessThanOrEqual=" + DEFAULT_LKP_MESSAGE_TYPE_ID);

        // Get all the messageList where lkpMessageTypeId is less than or equal to SMALLER_LKP_MESSAGE_TYPE_ID
        defaultMessageShouldNotBeFound("lkpMessageTypeId.lessThanOrEqual=" + SMALLER_LKP_MESSAGE_TYPE_ID);
    }

    @Test
    @Transactional
    public void getAllMessagesByLkpMessageTypeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where lkpMessageTypeId is less than DEFAULT_LKP_MESSAGE_TYPE_ID
        defaultMessageShouldNotBeFound("lkpMessageTypeId.lessThan=" + DEFAULT_LKP_MESSAGE_TYPE_ID);

        // Get all the messageList where lkpMessageTypeId is less than UPDATED_LKP_MESSAGE_TYPE_ID
        defaultMessageShouldBeFound("lkpMessageTypeId.lessThan=" + UPDATED_LKP_MESSAGE_TYPE_ID);
    }

    @Test
    @Transactional
    public void getAllMessagesByLkpMessageTypeIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where lkpMessageTypeId is greater than DEFAULT_LKP_MESSAGE_TYPE_ID
        defaultMessageShouldNotBeFound("lkpMessageTypeId.greaterThan=" + DEFAULT_LKP_MESSAGE_TYPE_ID);

        // Get all the messageList where lkpMessageTypeId is greater than SMALLER_LKP_MESSAGE_TYPE_ID
        defaultMessageShouldBeFound("lkpMessageTypeId.greaterThan=" + SMALLER_LKP_MESSAGE_TYPE_ID);
    }


    @Test
    @Transactional
    public void getAllMessagesBySenderUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where senderUserId equals to DEFAULT_SENDER_USER_ID
        defaultMessageShouldBeFound("senderUserId.equals=" + DEFAULT_SENDER_USER_ID);

        // Get all the messageList where senderUserId equals to UPDATED_SENDER_USER_ID
        defaultMessageShouldNotBeFound("senderUserId.equals=" + UPDATED_SENDER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllMessagesBySenderUserIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where senderUserId not equals to DEFAULT_SENDER_USER_ID
        defaultMessageShouldNotBeFound("senderUserId.notEquals=" + DEFAULT_SENDER_USER_ID);

        // Get all the messageList where senderUserId not equals to UPDATED_SENDER_USER_ID
        defaultMessageShouldBeFound("senderUserId.notEquals=" + UPDATED_SENDER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllMessagesBySenderUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where senderUserId in DEFAULT_SENDER_USER_ID or UPDATED_SENDER_USER_ID
        defaultMessageShouldBeFound("senderUserId.in=" + DEFAULT_SENDER_USER_ID + "," + UPDATED_SENDER_USER_ID);

        // Get all the messageList where senderUserId equals to UPDATED_SENDER_USER_ID
        defaultMessageShouldNotBeFound("senderUserId.in=" + UPDATED_SENDER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllMessagesBySenderUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where senderUserId is not null
        defaultMessageShouldBeFound("senderUserId.specified=true");

        // Get all the messageList where senderUserId is null
        defaultMessageShouldNotBeFound("senderUserId.specified=false");
    }

    @Test
    @Transactional
    public void getAllMessagesBySenderUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where senderUserId is greater than or equal to DEFAULT_SENDER_USER_ID
        defaultMessageShouldBeFound("senderUserId.greaterThanOrEqual=" + DEFAULT_SENDER_USER_ID);

        // Get all the messageList where senderUserId is greater than or equal to UPDATED_SENDER_USER_ID
        defaultMessageShouldNotBeFound("senderUserId.greaterThanOrEqual=" + UPDATED_SENDER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllMessagesBySenderUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where senderUserId is less than or equal to DEFAULT_SENDER_USER_ID
        defaultMessageShouldBeFound("senderUserId.lessThanOrEqual=" + DEFAULT_SENDER_USER_ID);

        // Get all the messageList where senderUserId is less than or equal to SMALLER_SENDER_USER_ID
        defaultMessageShouldNotBeFound("senderUserId.lessThanOrEqual=" + SMALLER_SENDER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllMessagesBySenderUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where senderUserId is less than DEFAULT_SENDER_USER_ID
        defaultMessageShouldNotBeFound("senderUserId.lessThan=" + DEFAULT_SENDER_USER_ID);

        // Get all the messageList where senderUserId is less than UPDATED_SENDER_USER_ID
        defaultMessageShouldBeFound("senderUserId.lessThan=" + UPDATED_SENDER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllMessagesBySenderUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where senderUserId is greater than DEFAULT_SENDER_USER_ID
        defaultMessageShouldNotBeFound("senderUserId.greaterThan=" + DEFAULT_SENDER_USER_ID);

        // Get all the messageList where senderUserId is greater than SMALLER_SENDER_USER_ID
        defaultMessageShouldBeFound("senderUserId.greaterThan=" + SMALLER_SENDER_USER_ID);
    }


    @Test
    @Transactional
    public void getAllMessagesBySenderAliasIsEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where senderAlias equals to DEFAULT_SENDER_ALIAS
        defaultMessageShouldBeFound("senderAlias.equals=" + DEFAULT_SENDER_ALIAS);

        // Get all the messageList where senderAlias equals to UPDATED_SENDER_ALIAS
        defaultMessageShouldNotBeFound("senderAlias.equals=" + UPDATED_SENDER_ALIAS);
    }

    @Test
    @Transactional
    public void getAllMessagesBySenderAliasIsNotEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where senderAlias not equals to DEFAULT_SENDER_ALIAS
        defaultMessageShouldNotBeFound("senderAlias.notEquals=" + DEFAULT_SENDER_ALIAS);

        // Get all the messageList where senderAlias not equals to UPDATED_SENDER_ALIAS
        defaultMessageShouldBeFound("senderAlias.notEquals=" + UPDATED_SENDER_ALIAS);
    }

    @Test
    @Transactional
    public void getAllMessagesBySenderAliasIsInShouldWork() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where senderAlias in DEFAULT_SENDER_ALIAS or UPDATED_SENDER_ALIAS
        defaultMessageShouldBeFound("senderAlias.in=" + DEFAULT_SENDER_ALIAS + "," + UPDATED_SENDER_ALIAS);

        // Get all the messageList where senderAlias equals to UPDATED_SENDER_ALIAS
        defaultMessageShouldNotBeFound("senderAlias.in=" + UPDATED_SENDER_ALIAS);
    }

    @Test
    @Transactional
    public void getAllMessagesBySenderAliasIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where senderAlias is not null
        defaultMessageShouldBeFound("senderAlias.specified=true");

        // Get all the messageList where senderAlias is null
        defaultMessageShouldNotBeFound("senderAlias.specified=false");
    }
                @Test
    @Transactional
    public void getAllMessagesBySenderAliasContainsSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where senderAlias contains DEFAULT_SENDER_ALIAS
        defaultMessageShouldBeFound("senderAlias.contains=" + DEFAULT_SENDER_ALIAS);

        // Get all the messageList where senderAlias contains UPDATED_SENDER_ALIAS
        defaultMessageShouldNotBeFound("senderAlias.contains=" + UPDATED_SENDER_ALIAS);
    }

    @Test
    @Transactional
    public void getAllMessagesBySenderAliasNotContainsSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where senderAlias does not contain DEFAULT_SENDER_ALIAS
        defaultMessageShouldNotBeFound("senderAlias.doesNotContain=" + DEFAULT_SENDER_ALIAS);

        // Get all the messageList where senderAlias does not contain UPDATED_SENDER_ALIAS
        defaultMessageShouldBeFound("senderAlias.doesNotContain=" + UPDATED_SENDER_ALIAS);
    }


    @Test
    @Transactional
    public void getAllMessagesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where createdBy equals to DEFAULT_CREATED_BY
        defaultMessageShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the messageList where createdBy equals to UPDATED_CREATED_BY
        defaultMessageShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllMessagesByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where createdBy not equals to DEFAULT_CREATED_BY
        defaultMessageShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the messageList where createdBy not equals to UPDATED_CREATED_BY
        defaultMessageShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllMessagesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultMessageShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the messageList where createdBy equals to UPDATED_CREATED_BY
        defaultMessageShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllMessagesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where createdBy is not null
        defaultMessageShouldBeFound("createdBy.specified=true");

        // Get all the messageList where createdBy is null
        defaultMessageShouldNotBeFound("createdBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllMessagesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where createdBy contains DEFAULT_CREATED_BY
        defaultMessageShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the messageList where createdBy contains UPDATED_CREATED_BY
        defaultMessageShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllMessagesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where createdBy does not contain DEFAULT_CREATED_BY
        defaultMessageShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the messageList where createdBy does not contain UPDATED_CREATED_BY
        defaultMessageShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }


    @Test
    @Transactional
    public void getAllMessagesByDateCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where dateCreated equals to DEFAULT_DATE_CREATED
        defaultMessageShouldBeFound("dateCreated.equals=" + DEFAULT_DATE_CREATED);

        // Get all the messageList where dateCreated equals to UPDATED_DATE_CREATED
        defaultMessageShouldNotBeFound("dateCreated.equals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllMessagesByDateCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where dateCreated not equals to DEFAULT_DATE_CREATED
        defaultMessageShouldNotBeFound("dateCreated.notEquals=" + DEFAULT_DATE_CREATED);

        // Get all the messageList where dateCreated not equals to UPDATED_DATE_CREATED
        defaultMessageShouldBeFound("dateCreated.notEquals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllMessagesByDateCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where dateCreated in DEFAULT_DATE_CREATED or UPDATED_DATE_CREATED
        defaultMessageShouldBeFound("dateCreated.in=" + DEFAULT_DATE_CREATED + "," + UPDATED_DATE_CREATED);

        // Get all the messageList where dateCreated equals to UPDATED_DATE_CREATED
        defaultMessageShouldNotBeFound("dateCreated.in=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllMessagesByDateCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where dateCreated is not null
        defaultMessageShouldBeFound("dateCreated.specified=true");

        // Get all the messageList where dateCreated is null
        defaultMessageShouldNotBeFound("dateCreated.specified=false");
    }

    @Test
    @Transactional
    public void getAllMessagesByMessageRecipientsIsEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);
        MessageRecipient messageRecipients = MessageRecipientResourceIT.createEntity(em);
        em.persist(messageRecipients);
        em.flush();
        message.addMessageRecipients(messageRecipients);
        messageRepository.saveAndFlush(message);
        Long messageRecipientsId = messageRecipients.getId();

        // Get all the messageList where messageRecipients equals to messageRecipientsId
        defaultMessageShouldBeFound("messageRecipientsId.equals=" + messageRecipientsId);

        // Get all the messageList where messageRecipients equals to messageRecipientsId + 1
        defaultMessageShouldNotBeFound("messageRecipientsId.equals=" + (messageRecipientsId + 1));
    }


    @Test
    @Transactional
    public void getAllMessagesByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);
        Order order = OrderResourceIT.createEntity(em);
        em.persist(order);
        em.flush();
        message.setOrder(order);
        messageRepository.saveAndFlush(message);
        Long orderId = order.getId();

        // Get all the messageList where order equals to orderId
        defaultMessageShouldBeFound("orderId.equals=" + orderId);

        // Get all the messageList where order equals to orderId + 1
        defaultMessageShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMessageShouldBeFound(String filter) throws Exception {
        restMessageMockMvc.perform(get("/api/messages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(message.getId().intValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].lkpMessageTypeId").value(hasItem(DEFAULT_LKP_MESSAGE_TYPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].senderUserId").value(hasItem(DEFAULT_SENDER_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].senderAlias").value(hasItem(DEFAULT_SENDER_ALIAS)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())));

        // Check, that the count call also returns 1
        restMessageMockMvc.perform(get("/api/messages/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMessageShouldNotBeFound(String filter) throws Exception {
        restMessageMockMvc.perform(get("/api/messages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMessageMockMvc.perform(get("/api/messages/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingMessage() throws Exception {
        // Get the message
        restMessageMockMvc.perform(get("/api/messages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMessage() throws Exception {
        // Initialize the database
        messageService.save(message);

        int databaseSizeBeforeUpdate = messageRepository.findAll().size();

        // Update the message
        Message updatedMessage = messageRepository.findById(message.getId()).get();
        // Disconnect from session so that the updates on updatedMessage are not directly saved in db
        em.detach(updatedMessage);
        updatedMessage
            .message(UPDATED_MESSAGE)
            .lkpMessageTypeId(UPDATED_LKP_MESSAGE_TYPE_ID)
            .senderUserId(UPDATED_SENDER_USER_ID)
            .senderAlias(UPDATED_SENDER_ALIAS)
            .createdBy(UPDATED_CREATED_BY)
            .dateCreated(UPDATED_DATE_CREATED);

        restMessageMockMvc.perform(put("/api/messages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedMessage)))
            .andExpect(status().isOk());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
        Message testMessage = messageList.get(messageList.size() - 1);
        assertThat(testMessage.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testMessage.getLkpMessageTypeId()).isEqualTo(UPDATED_LKP_MESSAGE_TYPE_ID);
        assertThat(testMessage.getSenderUserId()).isEqualTo(UPDATED_SENDER_USER_ID);
        assertThat(testMessage.getSenderAlias()).isEqualTo(UPDATED_SENDER_ALIAS);
        assertThat(testMessage.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testMessage.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void updateNonExistingMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessageMockMvc.perform(put("/api/messages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(message)))
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMessage() throws Exception {
        // Initialize the database
        messageService.save(message);

        int databaseSizeBeforeDelete = messageRepository.findAll().size();

        // Delete the message
        restMessageMockMvc.perform(delete("/api/messages/{id}", message.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
