package com.gamecraft.repository;

import com.gamecraft.domain.EmailAccount;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the EmailAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmailAccountRepository extends JpaRepository<EmailAccount, Long>, JpaSpecificationExecutor<EmailAccount> {

}
