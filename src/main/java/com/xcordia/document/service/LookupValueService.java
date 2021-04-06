package com.xcordia.document.service;

import com.xcordia.document.domain.LookupValue;
import com.xcordia.document.repository.LookupValueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link LookupValue}.
 */
@Service
@Transactional
public class LookupValueService {

    private final Logger log = LoggerFactory.getLogger(LookupValueService.class);

    private final LookupValueRepository lookupValueRepository;

    public LookupValueService(LookupValueRepository lookupValueRepository) {
        this.lookupValueRepository = lookupValueRepository;
    }

    /**
     * Save a lookupValue.
     *
     * @param lookupValue the entity to save.
     * @return the persisted entity.
     */
    public LookupValue save(LookupValue lookupValue) {
        log.debug("Request to save LookupValue : {}", lookupValue);
        return lookupValueRepository.save(lookupValue);
    }

    /**
     * Get all the lookupValues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LookupValue> findAll(Pageable pageable) {
        log.debug("Request to get all LookupValues");
        return lookupValueRepository.findAll(pageable);
    }


    /**
     * Get one lookupValue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LookupValue> findOne(Long id) {
        log.debug("Request to get LookupValue : {}", id);
        return lookupValueRepository.findById(id);
    }

    /**
     * Delete the lookupValue by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LookupValue : {}", id);

        lookupValueRepository.deleteById(id);
    }
}
