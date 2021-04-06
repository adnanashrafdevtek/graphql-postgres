package com.xcordia.document.web.rest;

import com.xcordia.document.domain.LookupValue;
import com.xcordia.document.service.LookupValueService;
import com.xcordia.document.web.rest.errors.BadRequestAlertException;
import com.xcordia.document.service.dto.LookupValueCriteria;
import com.xcordia.document.service.LookupValueQueryService;

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
 * REST controller for managing {@link com.xcordia.document.domain.LookupValue}.
 */
@RestController
@RequestMapping("/api")
public class LookupValueResource {

    private final Logger log = LoggerFactory.getLogger(LookupValueResource.class);

    private static final String ENTITY_NAME = "lookupValue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LookupValueService lookupValueService;

    private final LookupValueQueryService lookupValueQueryService;

    public LookupValueResource(LookupValueService lookupValueService, LookupValueQueryService lookupValueQueryService) {
        this.lookupValueService = lookupValueService;
        this.lookupValueQueryService = lookupValueQueryService;
    }

    /**
     * {@code POST  /lookup-values} : Create a new lookupValue.
     *
     * @param lookupValue the lookupValue to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lookupValue, or with status {@code 400 (Bad Request)} if the lookupValue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lookup-values")
    public ResponseEntity<LookupValue> createLookupValue(@Valid @RequestBody LookupValue lookupValue) throws URISyntaxException {
        log.debug("REST request to save LookupValue : {}", lookupValue);
        if (lookupValue.getId() != null) {
            throw new BadRequestAlertException("A new lookupValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LookupValue result = lookupValueService.save(lookupValue);
        return ResponseEntity.created(new URI("/api/lookup-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lookup-values} : Updates an existing lookupValue.
     *
     * @param lookupValue the lookupValue to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lookupValue,
     * or with status {@code 400 (Bad Request)} if the lookupValue is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lookupValue couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lookup-values")
    public ResponseEntity<LookupValue> updateLookupValue(@Valid @RequestBody LookupValue lookupValue) throws URISyntaxException {
        log.debug("REST request to update LookupValue : {}", lookupValue);
        if (lookupValue.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LookupValue result = lookupValueService.save(lookupValue);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lookupValue.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /lookup-values} : get all the lookupValues.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lookupValues in body.
     */
    @GetMapping("/lookup-values")
    public ResponseEntity<List<LookupValue>> getAllLookupValues(LookupValueCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LookupValues by criteria: {}", criteria);
        Page<LookupValue> page = lookupValueQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lookup-values/count} : count all the lookupValues.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lookup-values/count")
    public ResponseEntity<Long> countLookupValues(LookupValueCriteria criteria) {
        log.debug("REST request to count LookupValues by criteria: {}", criteria);
        return ResponseEntity.ok().body(lookupValueQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lookup-values/:id} : get the "id" lookupValue.
     *
     * @param id the id of the lookupValue to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lookupValue, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lookup-values/{id}")
    public ResponseEntity<LookupValue> getLookupValue(@PathVariable Long id) {
        log.debug("REST request to get LookupValue : {}", id);
        Optional<LookupValue> lookupValue = lookupValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lookupValue);
    }

    /**
     * {@code DELETE  /lookup-values/:id} : delete the "id" lookupValue.
     *
     * @param id the id of the lookupValue to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lookup-values/{id}")
    public ResponseEntity<Void> deleteLookupValue(@PathVariable Long id) {
        log.debug("REST request to delete LookupValue : {}", id);

        lookupValueService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
