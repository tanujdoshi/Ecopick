package com.example.backend.dto.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HomeResponse {
    private List<FarmDto> farms = new ArrayList<>();
    private List<ProductDto> products = new ArrayList<>();
}
