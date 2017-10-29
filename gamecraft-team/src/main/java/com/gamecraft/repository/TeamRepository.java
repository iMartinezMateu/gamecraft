package com.gamecraft.repository;

import com.gamecraft.domain.Team;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Team entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamRepository extends JpaRepository<Team, Long>, JpaSpecificationExecutor<Team> {
    @Query("select distinct team from Team team left join fetch team.teamUsers")
    List<Team> findAllWithEagerRelationships();

    @Query("select team from Team team left join fetch team.teamUsers where team.id =:id")
    Team findOneWithEagerRelationships(@Param("id") Long id);

}
