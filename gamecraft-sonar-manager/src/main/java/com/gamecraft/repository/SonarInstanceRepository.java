package com.gamecraft.repository;

import com.gamecraft.domain.SonarInstance;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SonarInstance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SonarInstanceRepository extends JpaRepository<SonarInstance, Long>, JpaSpecificationExecutor<SonarInstance> {

}
