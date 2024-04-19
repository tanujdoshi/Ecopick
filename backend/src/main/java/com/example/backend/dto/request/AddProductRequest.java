package com.example.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
public class AddProductRequest {
    @NotBlank(message = "name is required")
    private String productName;

    @NotBlank(message = "productDescription is required")
    private String productDescription;

    @NotNull(message = "price is required")
    private double price;

    @NotNull(message = "stock is required")
    private int stock;

    @NotBlank(message = "unit is required")
    private String unit;

    @Value("${prebook:false}")
    private boolean prebook;

    @NotNull(message = "category_id is required")
    private int category_id;

    @NotNull(message = "farm_id is required")
    private int farm_id;
}
