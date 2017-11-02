package com.gamecraft.repository.search;

import com.gamecraft.domain.Engine;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Engine entity.
 */
public interface EngineSearchRepository extends ElasticsearchRepository<Engine, Long> {
}
