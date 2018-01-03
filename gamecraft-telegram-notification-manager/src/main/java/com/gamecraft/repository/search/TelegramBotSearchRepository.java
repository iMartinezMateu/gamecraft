package com.gamecraft.repository.search;

import com.gamecraft.domain.TelegramBot;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TelegramBot entity.
 */
public interface TelegramBotSearchRepository extends ElasticsearchRepository<TelegramBot, Long> {
}
