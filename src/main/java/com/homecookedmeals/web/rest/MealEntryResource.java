package com.homecookedmeals.web.rest;

import com.homecookedmeals.repository.MealEntryRepository;
import com.homecookedmeals.service.MealEntryService;
import com.homecookedmeals.service.dto.MealEntryDTO;
import com.homecookedmeals.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.homecookedmeals.domain.MealEntry}.
 */
@RestController
@RequestMapping("/api")
public class MealEntryResource {

    private final Logger log = LoggerFactory.getLogger(MealEntryResource.class);

    private static final String ENTITY_NAME = "mealEntry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MealEntryService mealEntryService;

    private final MealEntryRepository mealEntryRepository;

    public MealEntryResource(MealEntryService mealEntryService, MealEntryRepository mealEntryRepository) {
        this.mealEntryService = mealEntryService;
        this.mealEntryRepository = mealEntryRepository;
    }

    /**
     * {@code POST  /meal-entries} : Create a new mealEntry.
     *
     * @param mealEntryDTO the mealEntryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mealEntryDTO, or with status {@code 400 (Bad Request)} if the mealEntry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/meal-entries")
    public ResponseEntity<MealEntryDTO> createMealEntry(@RequestBody MealEntryDTO mealEntryDTO) throws URISyntaxException {
        log.debug("REST request to save MealEntry : {}", mealEntryDTO);
        if (mealEntryDTO.getId() != null) {
            throw new BadRequestAlertException("A new mealEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MealEntryDTO result = mealEntryService.save(mealEntryDTO);
        return ResponseEntity
            .created(new URI("/api/meal-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /meal-entries/:id} : Updates an existing mealEntry.
     *
     * @param id the id of the mealEntryDTO to save.
     * @param mealEntryDTO the mealEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mealEntryDTO,
     * or with status {@code 400 (Bad Request)} if the mealEntryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mealEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/meal-entries/{id}")
    public ResponseEntity<MealEntryDTO> updateMealEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MealEntryDTO mealEntryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MealEntry : {}, {}", id, mealEntryDTO);
        if (mealEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mealEntryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mealEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MealEntryDTO result = mealEntryService.update(mealEntryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, mealEntryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /meal-entries/:id} : Partial updates given fields of an existing mealEntry, field will ignore if it is null
     *
     * @param id the id of the mealEntryDTO to save.
     * @param mealEntryDTO the mealEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mealEntryDTO,
     * or with status {@code 400 (Bad Request)} if the mealEntryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mealEntryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mealEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/meal-entries/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MealEntryDTO> partialUpdateMealEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MealEntryDTO mealEntryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MealEntry partially : {}, {}", id, mealEntryDTO);
        if (mealEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mealEntryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mealEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MealEntryDTO> result = mealEntryService.partialUpdate(mealEntryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, mealEntryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /meal-entries} : get all the mealEntries.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mealEntries in body.
     */
    @GetMapping("/meal-entries")
    public ResponseEntity<List<MealEntryDTO>> getAllMealEntries(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of MealEntries");
        Page<MealEntryDTO> page = mealEntryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /meal-entries/:id} : get the "id" mealEntry.
     *
     * @param id the id of the mealEntryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mealEntryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/meal-entries/{id}")
    public ResponseEntity<MealEntryDTO> getMealEntry(@PathVariable Long id) {
        log.debug("REST request to get MealEntry : {}", id);
        Optional<MealEntryDTO> mealEntryDTO = mealEntryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mealEntryDTO);
    }

    /**
     * {@code DELETE  /meal-entries/:id} : delete the "id" mealEntry.
     *
     * @param id the id of the mealEntryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/meal-entries/{id}")
    public ResponseEntity<Void> deleteMealEntry(@PathVariable Long id) {
        log.debug("REST request to delete MealEntry : {}", id);
        mealEntryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
