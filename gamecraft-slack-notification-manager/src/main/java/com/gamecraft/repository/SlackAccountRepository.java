package com.gamecraft.repository;

import com.gamecraft.domain.SlackAccount;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SlackAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SlackAccountRepository extends JpaRepository<SlackAccount, Long>, JpaSpecificationExecutor<SlackAccount> {

}
