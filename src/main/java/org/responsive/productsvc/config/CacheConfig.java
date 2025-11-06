package org.responsive.productsvc.config;

import org.responsive.productsvc.cache.CompositeKeyRecommendationCache;
import org.responsive.productsvc.cache.ProductCache;
import org.responsive.productsvc.cache.RecommendationCache;
import org.responsive.productsvc.cache.factory.CacheFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    private final CacheFactory cacheFactory;

    @Value("${product.cache.strategy:KEY_BASED}")
    private String strategy;

    @Value("${product.cache.capacity:1000}")
    private int capacity;

    @Value("${product.cache.recommendation.capacity:200}")
    private int recommendationCapacity;

    public CacheConfig(CacheFactory cacheFactory) {
        this.cacheFactory = cacheFactory;
    }

    @Bean
    public ProductCache productCache() {
        return cacheFactory.create(strategy, capacity);
    }

    @Bean
    public RecommendationCache recommendationCache() {
        // Use a smaller capacity by default for recommendation queries
        return cacheFactory.createRecommendationCache(recommendationCapacity);
    }
}
