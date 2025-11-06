package org.responsive.productsvc.dto;

import jakarta.validation.constraints.*;
import java.util.Map;

public record ProductRecord(
    @NotBlank(message = "ID is required") String id,
    @NotBlank(message = "Name is required") String name,
    @NotBlank(message = "Type is required") String type,
    @NotBlank(message = "Category is required") String category,
    @Positive(message = "Price must be greater than zero") long price,
    @NotBlank(message = "Recommended age group is required") String recommendedAgeGroup,
    @NotNull(message = "Attributes cannot be null") Map<String, String> attributes
) {}
