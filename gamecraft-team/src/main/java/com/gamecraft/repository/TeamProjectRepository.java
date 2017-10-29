package com.gamecraft.repository;

import com.gamecraft.domain.TeamProject;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TeamProject entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamProjectRepository extends JpaRepository<TeamProject, Long>, JpaSpecificationExecutor<TeamProject> {

}
