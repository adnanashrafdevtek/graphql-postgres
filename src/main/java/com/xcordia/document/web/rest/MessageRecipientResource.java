package com.xcordia.document.web.rest;

import com.xcordia.document.domain.MessageRecipient;
import com.xcordia.document.service.MessageRecipientService;
import com.xcordia.document.web.rest.errors.BadRequestAlertException;
import com.xcordia.document.service.dto.MessageRecipientCriteria;
import com.xcordia.document.service.MessageRecipientQueryService;

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
 * REST controller for managing {@link com.xcordia.document.domain.MessageRecipient}.
 */
@RestController
@RequestMapping("/api")
public class MessageRecipientResource {

    private final Logger log = LoggerFactory.getLogger(MessageRecipientResource.class);

    private static final String ENTITY_NAME = "messageRecipient";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MessageRecipientService messageRecipientService;

    private final MessageRecipientQueryService messageRecipientQueryService;

    public MessageRecipientResource(MessageRecipientService messageRecipientService, MessageRecipientQueryService messageRecipientQueryService) {
        this.messageRecipientService = messageRecipientService;
        this.messageRecipientQueryService = messageRecipientQueryService;
    }

    /**
     * {@code POST  /message-recipients} : Create a new messageRecipient.
     *
     * @param messageRecipient the messageRecipient to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new messageRecipient, or with status {@code 400 (Bad Request)} if the messageRecipient has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/message-recipients")
    public ResponseEntity<MessageRecipient> createMessageRecipient(@Valid @RequestBody MessageRecipient messageRecipient) throws URISyntaxException {
        log.debug("REST request to save MessageRecipient : {}", messageRecipient);
        if (messageRecipient.getId() != null) {
            throw new BadRequestAlertException("A new messageRecipient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MessageRecipient result = messageRecipientService.save(messageRecipient);
        return ResponseEntity.created(new URI("/api/message-recipients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /message-recipients} : Updates an existing messageRecipient.
     *
     * @param messageRecipient the messageRecipient to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated messageRecipient,
     * or with status {@code 400 (Bad Request)} if the messageRecipient is not valid,
     * or with status {@code 500 (Internal Server Error)} if the messageRecipient couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/message-recipients")
    public ResponseEntity<MessageRecipient> updateMessageRecipient(@Valid @RequestBody MessageRecipient messageRecipient) throws URISyntaxException {
        log.debug("REST request to update MessageRecipient : {}", messageRecipient);
        if (messageRecipient.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MessageRecipient result = messageRecipientService.save(messageRecipient);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, messageRecipient.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /message-recipients} : get all the messageRecipients.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of messageRecipients in body.
     */
    @GetMapping("/message-recipients")
    public ResponseEntity<List<MessageRecipient>> getAllMessageRecipients(MessageRecipientCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MessageRecipients by criteria: {}", criteria);
        Page<MessageRecipient> page = messageRecipientQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /message-recipients/count} : count all the messageRecipients.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/message-recipients/count")
    public ResponseEntity<Long> countMessageRecipients(MessageRecipientCriteria criteria) {
        log.debug("REST request to count MessageRecipients by criteria: {}", criteria);
        return ResponseEntity.ok().body(messageRecipientQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /message-recipients/:id} : get the "id" messageRecipient.
     *
     * @param id the id of the messageRecipient to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the messageRecipient, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/message-recipients/{id}")
    public ResponseEntity<MessageRecipient> getMessageRecipient(@PathVariable Long id) {
        log.debug("REST request to get MessageRecipient : {}", id);
        Optional<MessageRecipient> messageRecipient = messageRecipientService.findOne(id);
        return ResponseUtil.wrapOrNotFound(messageRecipient);
    }

    /**
     * {@code DELETE  /message-recipients/:id} : delete the "id" messageRecipient.
     *
     * @param id the id of the messageRecipient to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/message-recipients/{id}")
    public ResponseEntity<Void> deleteMessageRecipient(@PathVariable Long id) {
        log.debug("REST request to delete MessageRecipient : {}", id);

        messageRecipientService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
