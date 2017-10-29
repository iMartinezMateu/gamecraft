package com.gamecraft.repository.search;

import com.gamecraft.domain.TeamProject;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TeamProject entity.
 */
public interface TeamProjectSearchRepository extends ElasticsearchRepository<TeamProject, Long> {
}
