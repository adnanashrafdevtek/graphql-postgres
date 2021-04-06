package com.xcordia.document.service;

import com.xcordia.document.domain.MessageRecipient;
import com.xcordia.document.repository.MessageRecipientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link MessageRecipient}.
 */
@Service
@Transactional
public class MessageRecipientService {

    private final Logger log = LoggerFactory.getLogger(MessageRecipientService.class);

    private final MessageRecipientRepository messageRecipientRepository;

    public MessageRecipientService(MessageRecipientRepository messageRecipientRepository) {
        this.messageRecipientRepository = messageRecipientRepository;
    }

    /**
     * Save a messageRecipient.
     *
     * @param messageRecipient the entity to save.
     * @return the persisted entity.
     */
    public MessageRecipient save(MessageRecipient messageRecipient) {
        log.debug("Request to save MessageRecipient : {}", messageRecipient);
        return messageRecipientRepository.save(messageRecipient);
    }

    /**
     * Get all the messageRecipients.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MessageRecipient> findAll(Pageable pageable) {
        log.debug("Request to get all MessageRecipients");
        return messageRecipientRepository.findAll(pageable);
    }


    /**
     * Get one messageRecipient by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MessageRecipient> findOne(Long id) {
        log.debug("Request to get MessageRecipient : {}", id);
        return messageRecipientRepository.findById(id);
    }

    /**
     * Delete the messageRecipient by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MessageRecipient : {}", id);

        messageRecipientRepository.deleteById(id);
    }
}
