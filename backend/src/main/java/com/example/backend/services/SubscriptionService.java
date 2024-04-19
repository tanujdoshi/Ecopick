package com.example.backend.services;

import java.security.Principal;

import com.example.backend.dto.request.ProductSubscribeRequest;
import com.example.backend.dto.response.GetSubscriptionResponse;
import java.util.List;

public interface SubscriptionService {
    void subscribeProduct(ProductSubscribeRequest request, Principal principal);

    void runCron();

    List<GetSubscriptionResponse> getOwnSubscription(Principal principal);

    List<GetSubscriptionResponse> getMySubscribedProduct(Principal principal);

    String cancelSubscription(Principal principal, int productId);
}
