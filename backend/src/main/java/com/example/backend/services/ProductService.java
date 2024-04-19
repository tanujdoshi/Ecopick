package com.example.backend.services;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.security.Principal;
import com.example.backend.dto.request.AddProductRequest;
import com.example.backend.dto.request.EditProductRequest;
import com.example.backend.dto.response.ProductDto;
import com.example.backend.entities.Product;

public interface ProductService {
    List<ProductDto> addProduct(AddProductRequest product, MultipartFile[] files, Principal principal);
    Product editProduct(EditProductRequest editProductRequest, MultipartFile[] files, Principal principal);
    void deleteProduct(int id);
    ProductDto getProductById(int id);

    List<ProductDto> getFarmerProducts(Principal principal, String searchTerm);
    List<ProductDto> getAllProducts(String searchTerm);
}
