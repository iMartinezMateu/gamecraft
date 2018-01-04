package com.gamecraft.repository.search;

import com.gamecraft.domain.IrcBot;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the IrcBot entity.
 */
public interface IrcBotSearchRepository extends ElasticsearchRepository<IrcBot, Long> {
}
