package com.gamecraft.repository.search;

import com.gamecraft.domain.SlackAccount;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SlackAccount entity.
 */
public interface SlackAccountSearchRepository extends ElasticsearchRepository<SlackAccount, Long> {
}
