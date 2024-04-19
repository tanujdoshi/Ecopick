package com.example.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import com.example.backend.dto.request.OrderRequest;
import com.example.backend.dto.response.OrderDto;
import com.example.backend.services.OrderService;
import java.util.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    /**
     * Endpoint to place order
     * @param orderRequest information needed for the order
     * @param principal user token
     * @return String indicating success or failure
     */
    @PostMapping("/place-order")
    public ResponseEntity<String> placeOrder(@RequestBody @Valid OrderRequest orderRequest, Principal principal) {
        orderService.placeOrder(orderRequest, principal);
        return ResponseEntity.ok("Order placed successfully");
    }

    /**
     * Endpoint to retrieve order history
     * @param principal user token
     * @return order history of the user
     */
    @GetMapping("/orderHistory")
    public ResponseEntity<List<OrderDto>> orderHistory(Principal principal) {
        List<OrderDto>orderHistory = orderService.orderHistory(principal);
        return ResponseEntity.ok(orderHistory);
    }
    
}
