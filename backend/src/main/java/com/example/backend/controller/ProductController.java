package com.example.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.request.*;
import com.example.backend.dto.response.*;
import com.example.backend.entities.Product;
import com.example.backend.services.ProductService;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import java.util.Map;
import java.security.Principal;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    /**
     * Endpoint to get the products of a farmer
     * @param principal user token
     * @return returns the products of a farmer
     */
    @GetMapping("/farmer-products")
    public ResponseEntity<List<ProductDto>> getFarmerProducts(@RequestParam(name = "searchTerm", required = false) String searchTerm, Principal principal) {
        List<ProductDto> userProducts = productService.getFarmerProducts(principal, searchTerm);
        return ResponseEntity.ok(userProducts);
    }

    /**
     * end point to get all products that match an infix
     * @param searchTerm infix to filter search
     * @return all products that match the infix
     */
    @GetMapping("/all-products")
    public ResponseEntity<List<ProductDto>> getAllProducts(@RequestParam(name = "searchTerm", required = false) String searchTerm
            ) {
        List<ProductDto> allProducts = productService.getAllProducts(searchTerm);
        return ResponseEntity.ok(allProducts);
    }

    /**
     * Endpoint to `add new products
     * @param product all information regarding a product
     * @param files images of the product
     * @param principal user token
     * @return the product info after it is inserted into the database
     */
    @PostMapping("/addproduct")
    public ResponseEntity<Map> addProduct(@ModelAttribute @Valid AddProductRequest product,
            @RequestPart(value = "files") MultipartFile[] files, Principal principal) {
        List<ProductDto> AllFarmProducts = productService.addProduct(product, files,
                principal);
        Map<String, Object> response = new HashMap<>();

        response.put("message", principal.getName());
        response.put("Products", AllFarmProducts);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Endpoint to edit product information
     * @param product product information.
     * @param files new images for the product
     * @param principal user token
     * @return Edited product
     */
    @PostMapping("/editproduct")
    public ResponseEntity<Product> editProduct(@ModelAttribute @Valid EditProductRequest product,
            @RequestPart(value = "files") MultipartFile[] files, Principal principal) {
        Product response = productService.editProduct(product, files, principal);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Endpoint to delete a product
     * @param id product id
     * @return String indicating success or failure
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        ApiResponse response = new ApiResponse();
        response.setMessage("product deleted successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to Retrieve product by its ID
     * @param productId id of the product
     * @return Product object
     */
    @GetMapping("/getProduct/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable int productId) {
        ProductDto response = productService.getProductById(productId);
        return ResponseEntity.ok(response);
    }
}