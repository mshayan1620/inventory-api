package com.shayan.inventory.service;

import com.shayan.inventory.dto.ProductRequest;
import com.shayan.inventory.entity.Category;
import com.shayan.inventory.entity.Product;
import com.shayan.inventory.exception.DuplicateResourceException;
import com.shayan.inventory.exception.ResourceNotFoundException;
import com.shayan.inventory.repository.CategoryRepository;
import com.shayan.inventory.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    private Product sampleProduct;
    private Category sampleCategory;

    @BeforeEach
    void setUp() {
        sampleCategory = new Category("Electronics", "Electronic devices");
        sampleCategory.setId(1L);

        sampleProduct = new Product("Test Product", "A test product", new BigDecimal("29.99"), 100, "TEST-001", sampleCategory);
        sampleProduct.setId(1L);
    }

    @Test
    void getAllProducts_returnsPageOfProducts() {
        Page<Product> page = new PageImpl<>(List.of(sampleProduct));
        when(productRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Product> result = productService.getAllProducts(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Test Product", result.getContent().get(0).getName());
    }

    @Test
    void getProductById_existingId_returnsProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        Product result = productService.getProductById(1L);

        assertEquals("Test Product", result.getName());
        assertEquals("TEST-001", result.getSku());
    }

    @Test
    void getProductById_nonExistingId_throwsException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(99L));
    }

    @Test
    void createProduct_validRequest_returnsCreatedProduct() {
        ProductRequest request = new ProductRequest();
        request.setName("New Product");
        request.setDescription("A new product");
        request.setPrice(new BigDecimal("19.99"));
        request.setStockQuantity(50);
        request.setSku("NEW-001");
        request.setCategoryId(1L);

        when(productRepository.findBySku("NEW-001")).thenReturn(Optional.empty());
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(sampleCategory));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product result = productService.createProduct(request);

        assertEquals("New Product", result.getName());
        assertEquals(new BigDecimal("19.99"), result.getPrice());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void createProduct_duplicateSku_throwsException() {
        ProductRequest request = new ProductRequest();
        request.setName("Duplicate");
        request.setPrice(new BigDecimal("10.00"));
        request.setStockQuantity(10);
        request.setSku("TEST-001");

        when(productRepository.findBySku("TEST-001")).thenReturn(Optional.of(sampleProduct));

        assertThrows(DuplicateResourceException.class, () -> productService.createProduct(request));
    }

    @Test
    void updateStock_validRequest_updatesQuantity() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product result = productService.updateStock(1L, 200);

        assertEquals(200, result.getStockQuantity());
    }

    @Test
    void deleteProduct_existingId_deletesProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        productService.deleteProduct(1L);

        verify(productRepository).delete(sampleProduct);
    }
}
