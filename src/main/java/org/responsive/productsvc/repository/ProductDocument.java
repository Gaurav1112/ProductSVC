package org.responsive.productsvc.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Map;

@Document(collection = "products")
public class ProductDocument {

    @Id
    private String id;
    private String name;
    private String type;
    private String category;
    private long price;
    private String recommendedAgeGroup;
    private Map<String, String> attributes;

    public ProductDocument() {}

    public ProductDocument(String id, String name, String type, String category, long price,
                           String recommendedAgeGroup, Map<String, String> attributes) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.category = category;
        this.price = price;
        this.recommendedAgeGroup = recommendedAgeGroup;
        this.attributes = attributes;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public long getPrice() { return price; }
    public void setPrice(long price) { this.price = price; }

    public String getRecommendedAgeGroup() { return recommendedAgeGroup; }
    public void setRecommendedAgeGroup(String recommendedAgeGroup) { this.recommendedAgeGroup = recommendedAgeGroup; }

    public Map<String, String> getAttributes() { return attributes; }
    public void setAttributes(Map<String, String> attributes) { this.attributes = attributes; }
}
