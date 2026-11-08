package com.shayan.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shayan.inventory.dto.ProductRequest;
import com.shayan.inventory.entity.Category;
import com.shayan.inventory.entity.Product;
import com.shayan.inventory.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllProducts_returns200() throws Exception {
        Category category = new Category("Electronics", "Devices");
        Product product = new Product("Mouse", "Wireless mouse", new BigDecimal("29.99"), 100, "ELEC-001", category);

        when(productService.getAllProducts(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(product)));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Mouse"))
                .andExpect(jsonPath("$.content[0].price").value(29.99));
    }

    @Test
    void getProductById_returns200() throws Exception {
        Product product = new Product("Mouse", "Wireless mouse", new BigDecimal("29.99"), 100, "ELEC-001", null);
        product.setId(1L);

        when(productService.getProductById(1L)).thenReturn(product);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mouse"));
    }

    @Test
    void createProduct_validRequest_returns201() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setName("Keyboard");
        request.setDescription("Mechanical keyboard");
        request.setPrice(new BigDecimal("89.99"));
        request.setStockQuantity(50);

        Product created = new Product("Keyboard", "Mechanical keyboard", new BigDecimal("89.99"), 50, null, null);
        when(productService.createProduct(any(ProductRequest.class))).thenReturn(created);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Keyboard"));
    }

    @Test
    void createProduct_invalidRequest_returns400() throws Exception {
        ProductRequest request = new ProductRequest();
        // missing required fields

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
