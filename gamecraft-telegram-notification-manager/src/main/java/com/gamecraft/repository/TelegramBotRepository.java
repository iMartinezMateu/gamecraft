package com.gamecraft.repository;

import com.gamecraft.domain.TelegramBot;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TelegramBot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TelegramBotRepository extends JpaRepository<TelegramBot, Long>, JpaSpecificationExecutor<TelegramBot> {

}
