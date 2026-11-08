package com.shayan.inventory.service;

import com.shayan.inventory.dto.CategoryRequest;
import com.shayan.inventory.entity.Category;
import com.shayan.inventory.exception.DuplicateResourceException;
import com.shayan.inventory.exception.ResourceNotFoundException;
import com.shayan.inventory.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category sampleCategory;

    @BeforeEach
    void setUp() {
        sampleCategory = new Category("Electronics", "Electronic devices and components");
        sampleCategory.setId(1L);
    }

    @Test
    void getAllCategories_returnsPageOfCategories() {
        Page<Category> page = new PageImpl<>(List.of(sampleCategory));
        when(categoryRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Category> result = categoryService.getAllCategories(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Electronics", result.getContent().get(0).getName());
    }

    @Test
    void getCategoryById_existingId_returnsCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(sampleCategory));

        Category result = categoryService.getCategoryById(1L);

        assertEquals("Electronics", result.getName());
    }

    @Test
    void getCategoryById_nonExistingId_throwsException() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategoryById(99L));
    }

    @Test
    void createCategory_validRequest_returnsCreatedCategory() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Books");
        request.setDescription("Physical and digital books");

        when(categoryRepository.existsByNameIgnoreCase("Books")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Category result = categoryService.createCategory(request);

        assertEquals("Books", result.getName());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void createCategory_duplicateName_throwsException() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Electronics");

        when(categoryRepository.existsByNameIgnoreCase("Electronics")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> categoryService.createCategory(request));
    }

    @Test
    void deleteCategory_existingId_deletesCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(sampleCategory));

        categoryService.deleteCategory(1L);

        verify(categoryRepository).delete(sampleCategory);
    }
}
