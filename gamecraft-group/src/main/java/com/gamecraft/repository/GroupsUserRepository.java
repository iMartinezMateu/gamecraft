package com.gamecraft.repository;

import com.gamecraft.domain.GroupsUser;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the GroupsUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupsUserRepository extends JpaRepository<GroupsUser, Long>, JpaSpecificationExecutor<GroupsUser> {

}
