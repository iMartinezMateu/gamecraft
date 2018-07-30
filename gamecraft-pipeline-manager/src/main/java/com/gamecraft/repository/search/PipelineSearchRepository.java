package com.gamecraft.repository.search;

import com.gamecraft.domain.Pipeline;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Pipeline entity.
 */
public interface PipelineSearchRepository extends ElasticsearchRepository<Pipeline, Long> {
}
