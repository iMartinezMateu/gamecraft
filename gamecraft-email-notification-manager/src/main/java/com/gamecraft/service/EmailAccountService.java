package com.gamecraft.service;

import com.gamecraft.domain.EmailAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing EmailAccount.
 */
public interface EmailAccountService {

    /**
     * Save a emailAccount.
     *
     * @param emailAccount the entity to save
     * @return the persisted entity
     */
    EmailAccount save(EmailAccount emailAccount);

    /**
     *  Get all the emailAccounts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<EmailAccount> findAll(Pageable pageable);

    /**
     *  Get the "id" emailAccount.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    EmailAccount findOne(Long id);

    /**
     *  Delete the "id" emailAccount.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the emailAccount corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<EmailAccount> search(String query, Pageable pageable);
}
