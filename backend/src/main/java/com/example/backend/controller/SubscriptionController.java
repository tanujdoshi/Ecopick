package com.example.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.dto.request.ProductSubscribeRequest;
import com.example.backend.dto.response.GetSubscriptionResponse;
import com.example.backend.services.SubscriptionService;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api/subscribe")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    /**
     * Endpoint to subscribe to a product
     * @param request contains the information needed to create a new subscription
     * @param principal user token
     * @return String indicating success or failure
     */
    @PostMapping("/product")
    public ResponseEntity<String> subscribeProduct(@RequestBody ProductSubscribeRequest request, Principal principal) {
        subscriptionService.subscribeProduct(request, principal);
        return ResponseEntity.ok("Subscribed to product successfully!");
    }

    /**
     * Endpoint to override the CRON Job
     * @return String indicating success or failure
     */
    @GetMapping("/run-cron")
    public ResponseEntity<String> runCron() {
        subscriptionService.runCron();
        return ResponseEntity.ok("cronjob run successfully!");
    }

    /**
     * Endpoint to get the subscriptions of a user
     * @param principal user token
     * @return Subscriptions of the user
     */
    @GetMapping("/my-subscription")
    public ResponseEntity<List<GetSubscriptionResponse>> getOwnSubscription(Principal principal) {
        List<GetSubscriptionResponse> response = subscriptionService.getOwnSubscription(principal);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to get the products that an user is subscibed to
     * @param principal user token
     * @return Products that a user is subscribed to
     */
    @GetMapping("/my-subscribed-products")
    public ResponseEntity<List<GetSubscriptionResponse>> getMySubscribedProduct(Principal principal) {
        List<GetSubscriptionResponse> response = subscriptionService.getMySubscribedProduct(principal);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/unSubscribe/{productId}")
    public ResponseEntity<String> unSubscribe(Principal principal, @PathVariable int productId) {
        String response = subscriptionService.cancelSubscription(principal, productId);
        return ResponseEntity.ok(response);
    }
}
