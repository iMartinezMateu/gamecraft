package com.gamecraft.repository;

import com.gamecraft.domain.TwitterBot;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TwitterBot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TwitterBotRepository extends JpaRepository<TwitterBot, Long>, JpaSpecificationExecutor<TwitterBot> {

}
