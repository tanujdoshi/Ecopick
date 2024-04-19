package com.example.backend.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.backend.entities.Category;
import com.example.backend.repository.CategoryRepository;
import com.example.backend.services.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listcategories() {
        return categoryRepository.findAll();
    }
}
