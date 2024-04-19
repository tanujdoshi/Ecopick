package com.example.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
public class EditProductRequest {
    @NotNull(message = "Product ID is required")
    private int id;
    private String productName;
    private String productDescription;
    private double price;
    private int stock;
    private String unit;
}