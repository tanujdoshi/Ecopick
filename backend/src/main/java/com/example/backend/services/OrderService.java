package com.example.backend.services;

import java.security.Principal;
import java.util.List;

import com.example.backend.dto.request.OrderRequest;
import com.example.backend.dto.response.OrderDto;

public interface OrderService {
    void placeOrder(OrderRequest orderRequest, Principal principal);
    List<OrderDto> orderHistory(Principal principal);
}
