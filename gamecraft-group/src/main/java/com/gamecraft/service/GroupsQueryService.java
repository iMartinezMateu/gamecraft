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

import com.gamecraft.domain.Groups;
import com.gamecraft.domain.*; // for static metamodels
import com.gamecraft.repository.GroupsRepository;
import com.gamecraft.repository.search.GroupsSearchRepository;
import com.gamecraft.service.dto.GroupsCriteria;

import com.gamecraft.domain.enumeration.Role;

/**
 * Service for executing complex queries for Groups entities in the database.
 * The main input is a {@link GroupsCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Groups} or a {@link Page} of {@link Groups} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GroupsQueryService extends QueryService<Groups> {

    private final Logger log = LoggerFactory.getLogger(GroupsQueryService.class);


    private final GroupsRepository groupsRepository;

    private final GroupsSearchRepository groupsSearchRepository;

    public GroupsQueryService(GroupsRepository groupsRepository, GroupsSearchRepository groupsSearchRepository) {
        this.groupsRepository = groupsRepository;
        this.groupsSearchRepository = groupsSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Groups} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Groups> findByCriteria(GroupsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Groups> specification = createSpecification(criteria);
        return groupsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Groups} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Groups> findByCriteria(GroupsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Groups> specification = createSpecification(criteria);
        return groupsRepository.findAll(specification, page);
    }

    /**
     * Function to convert GroupsCriteria to a {@link Specifications}
     */
    private Specifications<Groups> createSpecification(GroupsCriteria criteria) {
        Specifications<Groups> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Groups_.id));
            }
            if (criteria.getGroupName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGroupName(), Groups_.groupName));
            }
            if (criteria.getGroupDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGroupDescription(), Groups_.groupDescription));
            }
            if (criteria.getGroupRole() != null) {
                specification = specification.and(buildSpecification(criteria.getGroupRole(), Groups_.groupRole));
            }
            if (criteria.getGroupEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getGroupEnabled(), Groups_.groupEnabled));
            }
        }
        return specification;
    }

}
