package com.example.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Objects;

@Getter
@Setter
public class SalesDTO {
    private HashMap<String, Double> orderSales = new HashMap<>();
    private HashMap<String, Double> subscriptionSales = new HashMap<>();
    public void addValue(String month, Double value, String type){
        if(Objects.equals(type, "SUBSCRIPTION")){
            subscriptionSales.put(month, value);
        }
        else{
            orderSales.put(month,value);
        }
    }
}
