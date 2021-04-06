package com.xcordia.document.service;

import com.xcordia.document.domain.Lookup;
import com.xcordia.document.repository.LookupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Lookup}.
 */
@Service
@Transactional
public class LookupService {

    private final Logger log = LoggerFactory.getLogger(LookupService.class);

    private final LookupRepository lookupRepository;

    public LookupService(LookupRepository lookupRepository) {
        this.lookupRepository = lookupRepository;
    }

    /**
     * Save a lookup.
     *
     * @param lookup the entity to save.
     * @return the persisted entity.
     */
    public Lookup save(Lookup lookup) {
        log.debug("Request to save Lookup : {}", lookup);
        return lookupRepository.save(lookup);
    }

    /**
     * Get all the lookups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Lookup> findAll(Pageable pageable) {
        log.debug("Request to get all Lookups");
        return lookupRepository.findAll(pageable);
    }


    /**
     * Get one lookup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Lookup> findOne(Long id) {
        log.debug("Request to get Lookup : {}", id);
        return lookupRepository.findById(id);
    }

    /**
     * Delete the lookup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Lookup : {}", id);

        lookupRepository.deleteById(id);
    }
}
