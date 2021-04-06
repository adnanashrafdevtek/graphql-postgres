package com.xcordia.document.web.rest;

import com.xcordia.document.domain.Lookup;
import com.xcordia.document.service.LookupService;
import com.xcordia.document.web.rest.errors.BadRequestAlertException;
import com.xcordia.document.service.dto.LookupCriteria;
import com.xcordia.document.service.LookupQueryService;

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
 * REST controller for managing {@link com.xcordia.document.domain.Lookup}.
 */
@RestController
@RequestMapping("/api")
public class LookupResource {

    private final Logger log = LoggerFactory.getLogger(LookupResource.class);

    private static final String ENTITY_NAME = "lookup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LookupService lookupService;

    private final LookupQueryService lookupQueryService;

    public LookupResource(LookupService lookupService, LookupQueryService lookupQueryService) {
        this.lookupService = lookupService;
        this.lookupQueryService = lookupQueryService;
    }

    /**
     * {@code POST  /lookups} : Create a new lookup.
     *
     * @param lookup the lookup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lookup, or with status {@code 400 (Bad Request)} if the lookup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lookups")
    public ResponseEntity<Lookup> createLookup(@Valid @RequestBody Lookup lookup) throws URISyntaxException {
        log.debug("REST request to save Lookup : {}", lookup);
        if (lookup.getId() != null) {
            throw new BadRequestAlertException("A new lookup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Lookup result = lookupService.save(lookup);
        return ResponseEntity.created(new URI("/api/lookups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lookups} : Updates an existing lookup.
     *
     * @param lookup the lookup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lookup,
     * or with status {@code 400 (Bad Request)} if the lookup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lookup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lookups")
    public ResponseEntity<Lookup> updateLookup(@Valid @RequestBody Lookup lookup) throws URISyntaxException {
        log.debug("REST request to update Lookup : {}", lookup);
        if (lookup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Lookup result = lookupService.save(lookup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lookup.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /lookups} : get all the lookups.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lookups in body.
     */
    @GetMapping("/lookups")
    public ResponseEntity<List<Lookup>> getAllLookups(LookupCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Lookups by criteria: {}", criteria);
        Page<Lookup> page = lookupQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lookups/count} : count all the lookups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lookups/count")
    public ResponseEntity<Long> countLookups(LookupCriteria criteria) {
        log.debug("REST request to count Lookups by criteria: {}", criteria);
        return ResponseEntity.ok().body(lookupQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lookups/:id} : get the "id" lookup.
     *
     * @param id the id of the lookup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lookup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lookups/{id}")
    public ResponseEntity<Lookup> getLookup(@PathVariable Long id) {
        log.debug("REST request to get Lookup : {}", id);
        Optional<Lookup> lookup = lookupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lookup);
    }

    /**
     * {@code DELETE  /lookups/:id} : delete the "id" lookup.
     *
     * @param id the id of the lookup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lookups/{id}")
    public ResponseEntity<Void> deleteLookup(@PathVariable Long id) {
        log.debug("REST request to delete Lookup : {}", id);

        lookupService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
