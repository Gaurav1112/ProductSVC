package org.responsive.productsvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.responsive.productsvc.dto.ProductRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductRecord sampleProduct;

    @BeforeEach
    void setup() {
        sampleProduct = new ProductRecord(
            "p-integration",
            "Lego Spaceship",
            "Toy",
            "Kids",
            2999,
            "5-10",
            Map.of("brand", "Lego", "pieces", "300")
        );
    }

    // ✅ CREATE
    @Test
    void shouldCreateProductSuccessfully() throws Exception {
        mockMvc.perform(post("/api/products")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(objectMapper.writeValueAsString(sampleProduct)))
               .andExpect(status().isCreated())
               .andExpect(header().exists("Location"))
               .andExpect(jsonPath("$.id", is("p-integration")))
               .andExpect(jsonPath("$.name", is("Lego Spaceship")));
    }

    // ✅ GET BY ID
    @Test
    void shouldGetProductById() throws Exception {
        mockMvc.perform(get("/api/products/p1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is("p1")))
               .andExpect(jsonPath("$.name", not(emptyOrNullString())));
    }

    // ✅ GET ALL
    @Test
    void shouldGetAllProducts() throws Exception {
        mockMvc.perform(get("/api/products"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", not(empty())));
    }

    // ✅ UPDATE
    @Test
    void shouldUpdateExistingProduct() throws Exception {
        ProductRecord updatedProduct = new ProductRecord(
            "p1",
            "Lego Starship - Updated",
            "Toy",
            "Kids",
            2499,
            "5-12",
            Map.of("brand", "Lego", "pieces", "260")
        );

        mockMvc.perform(put("/api/products/p1")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(objectMapper.writeValueAsString(updatedProduct)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name", is("Lego Starship - Updated")))
               .andExpect(jsonPath("$.price", is(2499)));
    }

    // ✅ DELETE
    @Test
    void shouldDeleteExistingProduct() throws Exception {
        // delete
        mockMvc.perform(delete("/api/products/p1"))
               .andExpect(status().isNoContent());

        // confirm deletion
        mockMvc.perform(get("/api/products/p1"))
               .andExpect(status().isNotFound());
    }
}
