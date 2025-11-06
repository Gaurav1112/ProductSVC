package org.responsive.productsvc.service.impl;

import org.responsive.productsvc.cache.CompositeKeyRecommendationCache;
import org.responsive.productsvc.cache.ProductCache;
import org.responsive.productsvc.cache.RecommendationCache;
import org.responsive.productsvc.util.RecommendationKeyUtil;
import org.responsive.productsvc.dto.ProductRecord;
import org.responsive.productsvc.repository.ProductDocument;
import org.responsive.productsvc.repository.ProductRepository;
import org.responsive.productsvc.service.ProductRecommendationService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Simple recommendation implementation:
 * - Uses recommendationCache (composite key) to store results
 * - Otherwise fetches candidates from repo (by type or all), applies filters, caches result
 */
@Service
public class ProductRecommendationServiceImpl implements ProductRecommendationService {

    private final ProductRepository repository;
    private final RecommendationCache recommendationCache;
    // also use main product cache for quick id lookup/populate
    private final ProductCache productCache;

    public ProductRecommendationServiceImpl(ProductRepository repository,
                                            RecommendationCache recommendationCache,
                                            ProductCache productCache) {
        this.repository = repository;
        this.recommendationCache = recommendationCache;
        this.productCache = productCache;
    }

    @Override
    public List<ProductRecord> recommend(Long minPrice, Long maxPrice, String type, String category, Integer age) {
        String key = RecommendationKeyUtil.buildKey(minPrice, maxPrice, type, category, age);
        var cached = recommendationCache.get(key);
        if (cached.isPresent()) {
            return cached.get();
        }

        // Fetch candidates: if type provided, use repository.findByType, otherwise fetch all (could be optimized)
        List<ProductDocument> candidates;
        if (type != null && !type.isBlank()) {
            candidates = repository.findByType(type);
        } else if (category != null && !category.isBlank()) {
            candidates = repository.findByCategory(category);
        } else {
            candidates = repository.findAll();
        }

        // filter by price & age & category/type if provided
        List<ProductRecord> results = candidates.stream()
                                                .filter(d -> matchesPrice(d, minPrice, maxPrice))
                                                .filter(d -> matchesAge(d, age))
                                                .filter(d -> matchesCategory(d, category))
                                                .map(this::toRecord)
                                                .collect(Collectors.toList());

        // cache result and return
        recommendationCache.put(key, results);
        // also populate productCache for faster subsequent ID lookups
        results.forEach(productCache::put);
        return results;
    }

    @Override
    public List<ProductRecord> recommendSimilarProducts(String productId, int limit) {
        var optionalProduct = repository.findById(productId);
        if (optionalProduct.isEmpty()) return List.of();

        ProductDocument base = optionalProduct.get();

        return repository.findAll().stream()
                         .filter(p -> !p.getId().equals(productId)) // exclude same product
                         .filter(p -> p.getType().equalsIgnoreCase(base.getType())
                             || p.getCategory().equalsIgnoreCase(base.getCategory()))
                         .sorted(Comparator.comparingLong(
                             p -> Math.abs(p.getPrice() - base.getPrice())
                         )) // price proximity sorting
                         .limit(limit)
                         .map(this::toRecord)
                         .collect(Collectors.toList());
    }

    private boolean matchesPrice(ProductDocument d, Long minPrice, Long maxPrice) {
        long p = d.getPrice();
        if (minPrice != null && p < minPrice) return false;
        if (maxPrice != null && p > maxPrice) return false;
        return true;
    }

    private boolean matchesCategory(ProductDocument d, String category) {
        if (category == null || category.isBlank()) return true;
        return category.equalsIgnoreCase(d.getCategory());
    }

    private boolean matchesAge(ProductDocument d, Integer age) {
        if (age == null) return true;
        String range = d.getRecommendedAgeGroup();
        if (range == null || range.isBlank()) return true;
        try {
            // range like "5-10" or "18+" (handle approximate)
            if (range.contains("-")) {
                String[] parts = range.split("-");
                int low = Integer.parseInt(parts[0].trim());
                int high = Integer.parseInt(parts[1].trim());
                return age >= low && age <= high;
            } else if (range.endsWith("+")) {
                int low = Integer.parseInt(range.replace("+", "").trim());
                return age >= low;
            } else {
                // single number
                int val = Integer.parseInt(range.trim());
                return age == val;
            }
        } catch (Exception e) {
            // if parse fails, be permissive
            return true;
        }
    }

    private ProductRecord toRecord(ProductDocument d) {
        return new ProductRecord(
            d.getId(),
            d.getName(),
            d.getType(),
            d.getCategory(),
            d.getPrice(),
            d.getRecommendedAgeGroup(),
            d.getAttributes()
        );
    }
}
