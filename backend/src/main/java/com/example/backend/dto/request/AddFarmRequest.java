package com.example.backend.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddFarmRequest {
    private String name;
    private String Address;
    private String Description;
    private double lat;
    private double lng;
}
