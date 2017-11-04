package com.gamecraft.repository.search;

import com.gamecraft.domain.EmailAccount;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the EmailAccount entity.
 */
public interface EmailAccountSearchRepository extends ElasticsearchRepository<EmailAccount, Long> {
}
