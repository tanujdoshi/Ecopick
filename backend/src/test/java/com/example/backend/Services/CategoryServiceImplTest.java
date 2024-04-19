package com.example.backend.Services;

import com.example.backend.entities.Category;
import com.example.backend.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import com.example.backend.services.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CategoryServiceImplTest {
    static final int EXPECTED_LIST_CATEGORIES = 2;
    @Mock
    private CategoryRepository categoryRepositoryMock;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListCategories() {
        List<Category> mockCategories = new ArrayList<>();
        mockCategories.add(new Category());
        mockCategories.add(new Category());
        when(categoryRepositoryMock.findAll()).thenReturn(mockCategories);
        List<Category> categories = categoryService.listcategories();
        assertEquals(EXPECTED_LIST_CATEGORIES, categories.size());
    }
}
