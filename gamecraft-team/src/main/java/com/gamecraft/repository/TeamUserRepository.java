package com.gamecraft.repository;

import com.gamecraft.domain.TeamUser;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TeamUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamUserRepository extends JpaRepository<TeamUser, Long>, JpaSpecificationExecutor<TeamUser> {

}
