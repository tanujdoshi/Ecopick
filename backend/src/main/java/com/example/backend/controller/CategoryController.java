package com.example.backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.backend.entities.Category;
import com.example.backend.services.CategoryService;

import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * Endpoint to get all categories
     * @return returns a list of categories
     */
    @GetMapping("/list")
    public ResponseEntity<List<Category>> getCategories() {
        return ResponseEntity.ok(categoryService.listcategories());
    }

}
