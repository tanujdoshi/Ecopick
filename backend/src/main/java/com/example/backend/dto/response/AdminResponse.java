package com.example.backend.dto.response;

import com.example.backend.entities.Order;
import com.example.backend.entities.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AdminResponse {
    private List<FarmDto> farms = new ArrayList<>();
    private List<ProductDto> products = new ArrayList<>();
    private List<OrderDto> orders = new ArrayList<>();
    private List<UserDTO> users = new ArrayList<>();
    private SalesDTO Sales;
}
