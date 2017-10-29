package com.gamecraft.repository.search;

import com.gamecraft.domain.TeamUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TeamUser entity.
 */
public interface TeamUserSearchRepository extends ElasticsearchRepository<TeamUser, Long> {
}
