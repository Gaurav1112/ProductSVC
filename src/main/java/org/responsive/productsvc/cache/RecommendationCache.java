package org.responsive.productsvc.cache;

import org.responsive.productsvc.dto.ProductRecord;

import java.util.List;
import java.util.Optional;

/**
 * Cache for recommendation query results keyed by a normalized string.
 */
public interface RecommendationCache {
    Optional<List<ProductRecord>> get(String key);
    void put(String key, List<ProductRecord> value);
    void remove(String key);
    void clear();
}
