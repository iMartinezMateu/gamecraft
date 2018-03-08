package com.gamecraft.repository;

import com.gamecraft.domain.Groups;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Groups entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupsRepository extends JpaRepository<Groups, Long>, JpaSpecificationExecutor<Groups> {

}
