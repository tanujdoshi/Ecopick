package com.example.backend.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import com.example.backend.entities.SubscriptionName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetSubscriptionResponse {
    private SubscriptionName name;
    private ArrayList<String> days;
    private LocalDateTime subscriptionDate;
    private ProductSubscriptionResponse Product;
    private int productId;
    private int userId;
    private String customerName;
}
