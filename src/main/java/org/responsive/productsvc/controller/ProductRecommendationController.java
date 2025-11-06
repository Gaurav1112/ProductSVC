package org.responsive.productsvc.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.responsive.productsvc.dto.ProductRecord;
import org.responsive.productsvc.service.ProductRecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
    name = "Product Recommendation",
    description = "APIs that recommend similar products based on type, category, and price"
)
@RestController
@RequestMapping("/api/recommendations")
public class ProductRecommendationController {

    private final ProductRecommendationService recommendationService;

    public ProductRecommendationController(ProductRecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @Operation(
        summary = "Recommend similar products",
        description = "Fetches up to 'limit' products similar to the provided product ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Recommendations retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found or no recommendations")
        }
    )
    @GetMapping("/{productId}")
    public ResponseEntity<List<ProductRecord>> recommendSimilarProducts(
        @Parameter(description = "Base product ID to generate recommendations from", example = "p1")
        @PathVariable String productId,
        @Parameter(description = "Number of recommendations to fetch", example = "5")
        @RequestParam(defaultValue = "5") int limit) {

        var recommendations = recommendationService.recommendSimilarProducts(productId, limit);
        if (recommendations.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(recommendations);
    }
}
