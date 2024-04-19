package com.example.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {

    @NotNull(message = "customer id is required")
    private int customer_id;
    @NotNull(message = "farm id is required")
    private int farm_id;
    @NotNull(message = "product id is required")
    private int product_id;
    @NotNull(message = "Quantity is required")
    private int quantity;
}
