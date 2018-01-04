package com.gamecraft.repository;

import com.gamecraft.domain.IrcBot;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the IrcBot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IrcBotRepository extends JpaRepository<IrcBot, Long>, JpaSpecificationExecutor<IrcBot> {

}
