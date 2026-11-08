package com.shayan.inventory.service;

import com.shayan.inventory.dto.ProductRequest;
import com.shayan.inventory.entity.Category;
import com.shayan.inventory.entity.Product;
import com.shayan.inventory.exception.DuplicateResourceException;
import com.shayan.inventory.exception.ResourceNotFoundException;
import com.shayan.inventory.repository.CategoryRepository;
import com.shayan.inventory.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public Page<Product> searchProducts(String name, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.searchProducts(name, categoryId, minPrice, maxPrice, pageable);
    }

    public Page<Product> getLowStockProducts(int threshold, Pageable pageable) {
        return productRepository.findLowStock(threshold, pageable);
    }

    @Transactional
    public Product createProduct(ProductRequest request) {
        if (request.getSku() != null) {
            productRepository.findBySku(request.getSku()).ifPresent(existing -> {
                throw new DuplicateResourceException("Product already exists with SKU: " + request.getSku());
            });
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setSku(request.getSku());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));
            product.setCategory(category);
        }

        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, ProductRequest request) {
        Product product = getProductById(id);

        if (request.getSku() != null) {
            productRepository.findBySku(request.getSku())
                    .filter(existing -> !existing.getId().equals(id))
                    .ifPresent(existing -> {
                        throw new DuplicateResourceException("Product already exists with SKU: " + request.getSku());
                    });
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setSku(request.getSku());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }

        return productRepository.save(product);
    }

    @Transactional
    public Product updateStock(Long id, int quantity) {
        Product product = getProductById(id);
        product.setStockQuantity(quantity);
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}
