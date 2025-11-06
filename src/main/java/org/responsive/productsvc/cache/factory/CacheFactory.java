package org.responsive.productsvc.cache.factory;

import org.responsive.productsvc.cache.CompositeKeyRecommendationCache;
import org.responsive.productsvc.cache.KeyBasedLRUCache;
import org.responsive.productsvc.cache.ProductCache;
import org.responsive.productsvc.cache.TypeBasedLRUCache;
import org.springframework.stereotype.Component;

/**
 * Keeps factory methods for the various cache strategies.
 */
@Component
public class CacheFactory {

    public ProductCache create(String strategy, int capacity) {
        return switch (strategy == null ? "KEY_BASED" : strategy.toUpperCase()) {
            case "TYPE_BASED" -> new TypeBasedLRUCache(capacity);
            case "KEY_BASED" -> new KeyBasedLRUCache(capacity);
            default -> new KeyBasedLRUCache(capacity);
        };
    }

    public CompositeKeyRecommendationCache createRecommendationCache(int capacity) {
        return new CompositeKeyRecommendationCache(capacity);
    }
}
