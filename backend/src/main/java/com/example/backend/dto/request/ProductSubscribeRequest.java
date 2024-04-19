package com.example.backend.dto.request;

import com.example.backend.entities.SubscriptionName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSubscribeRequest {
    private SubscriptionName name;
    private int mon;
    private int tue;
    private int wed;
    private int thu;
    private int fri;
    private int sat;
    private int sun;
    private int farm_id;
    private int product_id;
}
