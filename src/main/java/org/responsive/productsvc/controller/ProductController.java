package org.responsive.productsvc.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.responsive.productsvc.dto.ProductRecord;
import org.responsive.productsvc.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
    name = "Product Management",
    description = "APIs for creating, retrieving, updating, and deleting products"
)
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    // -------------------- CREATE PRODUCT --------------------
    @Operation(
        summary = "Create a new product",
        description = "Adds a new product to the catalog and stores it in MongoDB",
        responses = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product data", content = @Content)
        }
    )
    @PostMapping
    public ResponseEntity<ProductRecord> createProduct(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Product details to be added",
            required = true,
            content = @Content(
                schema = @Schema(implementation = ProductRecord.class),
                examples = @ExampleObject(value = """
                                {
                                  "id": "p1",
                                  "name": "Lego Starship",
                                  "type": "Toy",
                                  "category": "Kids",
                                  "price": 1999,
                                  "recommendedAgeGroup": "5-10",
                                  "attributes": {
                                    "brand": "Lego",
                                    "pieces": "250"
                                  }
                                }
                                """)
            )
        )
        @RequestBody ProductRecord record) {

        ProductRecord saved = service.create(record);

        // ✅ Add REST-compliant Location header
        String locationUri = "/api/products/" + saved.id();
        return ResponseEntity
            .created(java.net.URI.create(locationUri))
            .body(saved);
    }

    // -------------------- GET PRODUCT BY ID --------------------
    @Operation(
        summary = "Get a product by ID",
        description = "Fetches a single product using its unique ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Product found successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProductRecord> getById(
        @Parameter(description = "Unique product ID", example = "p1")
        @PathVariable String id) {
        return ResponseEntity.of(service.getById(id));
    }

    // -------------------- GET PRODUCTS BY TYPE --------------------
    @Operation(
        summary = "Get products by type",
        description = "Retrieves all products that belong to a specific type, e.g., 'Toy', 'Book', 'Gadget'"
    )
    @GetMapping("/type/{type}")
    public ResponseEntity<List<ProductRecord>> getByType(
        @Parameter(description = "Product type", example = "Toy")
        @PathVariable String type) {
        return ResponseEntity.ok(service.getByType(type));
    }

    // -------------------- GET ALL PRODUCTS --------------------
    @Operation(
        summary = "Get all products",
        description = "Returns all products from the catalog"
    )
    @GetMapping
    public ResponseEntity<List<ProductRecord>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // -------------------- UPDATE PRODUCT --------------------
    @Operation(
        summary = "Update product details",
        description = "Updates an existing product identified by its ID"
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProductRecord> updateProduct(
        @Parameter(description = "Product ID to update", example = "p1")
        @PathVariable String id,
        @RequestBody ProductRecord record) {
        var existing = service.getById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ProductRecord updated = service.create(new ProductRecord(
            id,
            record.name(),
            record.type(),
            record.category(),
            record.price(),
            record.recommendedAgeGroup(),
            record.attributes()
        ));
        return ResponseEntity.ok(updated);
    }

    // -------------------- DELETE PRODUCT --------------------
    @Operation(
        summary = "Delete a product by ID",
        description = "Removes a product from the catalog by its ID"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
        @Parameter(description = "Product ID to delete", example = "p1")
        @PathVariable String id) {
        var existing = service.getById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
