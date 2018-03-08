package com.gamecraft.repository.search;

import com.gamecraft.domain.SonarInstance;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SonarInstance entity.
 */
public interface SonarInstanceSearchRepository extends ElasticsearchRepository<SonarInstance, Long> {
}
