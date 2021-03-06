package com.gamecraft.repository;

import com.gamecraft.domain.Engine;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Engine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EngineRepository extends JpaRepository<Engine, Long>, JpaSpecificationExecutor<Engine> {

}
