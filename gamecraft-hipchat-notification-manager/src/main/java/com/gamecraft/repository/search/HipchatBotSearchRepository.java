package com.gamecraft.repository.search;

import com.gamecraft.domain.HipchatBot;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the HipchatBot entity.
 */
public interface HipchatBotSearchRepository extends ElasticsearchRepository<HipchatBot, Long> {
}
