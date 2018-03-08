package com.gamecraft.repository.search;

import com.gamecraft.domain.GroupsUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the GroupsUser entity.
 */
public interface GroupsUserSearchRepository extends ElasticsearchRepository<GroupsUser, Long> {
}
