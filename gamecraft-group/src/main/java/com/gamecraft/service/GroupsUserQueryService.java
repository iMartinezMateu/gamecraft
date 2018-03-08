package com.gamecraft.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.gamecraft.domain.GroupsUser;
import com.gamecraft.domain.*; // for static metamodels
import com.gamecraft.repository.GroupsUserRepository;
import com.gamecraft.repository.search.GroupsUserSearchRepository;
import com.gamecraft.service.dto.GroupsUserCriteria;


/**
 * Service for executing complex queries for GroupsUser entities in the database.
 * The main input is a {@link GroupsUserCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GroupsUser} or a {@link Page} of {@link GroupsUser} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GroupsUserQueryService extends QueryService<GroupsUser> {

    private final Logger log = LoggerFactory.getLogger(GroupsUserQueryService.class);


    private final GroupsUserRepository groupsUserRepository;

    private final GroupsUserSearchRepository groupsUserSearchRepository;

    public GroupsUserQueryService(GroupsUserRepository groupsUserRepository, GroupsUserSearchRepository groupsUserSearchRepository) {
        this.groupsUserRepository = groupsUserRepository;
        this.groupsUserSearchRepository = groupsUserSearchRepository;
    }

    /**
     * Return a {@link List} of {@link GroupsUser} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GroupsUser> findByCriteria(GroupsUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<GroupsUser> specification = createSpecification(criteria);
        return groupsUserRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link GroupsUser} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GroupsUser> findByCriteria(GroupsUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<GroupsUser> specification = createSpecification(criteria);
        return groupsUserRepository.findAll(specification, page);
    }

    /**
     * Function to convert GroupsUserCriteria to a {@link Specifications}
     */
    private Specifications<GroupsUser> createSpecification(GroupsUserCriteria criteria) {
        Specifications<GroupsUser> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), GroupsUser_.id));
            }
            if (criteria.getGroupId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGroupId(), GroupsUser_.groupId));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), GroupsUser_.userId));
            }
        }
        return specification;
    }

}
