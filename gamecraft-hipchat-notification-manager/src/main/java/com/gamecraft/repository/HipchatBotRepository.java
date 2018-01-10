package com.gamecraft.repository;

import com.gamecraft.domain.HipchatBot;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the HipchatBot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HipchatBotRepository extends JpaRepository<HipchatBot, Long>, JpaSpecificationExecutor<HipchatBot> {

}
