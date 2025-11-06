package org.responsive.productsvc.service;

import org.responsive.productsvc.dto.ProductRecord;

import java.util.List;

/**
 * Recommendation service that returns products matching filters.
 */
public interface ProductRecommendationService {
    /**
     * Recommend products by filters.
     *
     * @param minPrice nullable min price
     * @param maxPrice nullable max price
     * @param type nullable product type
     * @param category nullable category
     * @param age nullable user age (to match recommendedAgeGroup)
     * @return list of matching product records
     */
    List<ProductRecord> recommend(Long minPrice, Long maxPrice, String type, String category, Integer age);
    List<ProductRecord> recommendSimilarProducts(String productId, int limit);
}
