package com.gamecraft.repository.search;

import com.gamecraft.domain.TwitterBot;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TwitterBot entity.
 */
public interface TwitterBotSearchRepository extends ElasticsearchRepository<TwitterBot, Long> {
}
