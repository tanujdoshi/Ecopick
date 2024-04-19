package com.example.backend.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.entities.Farms;
import com.example.backend.entities.Images;
import com.example.backend.entities.Product;

import lombok.Data;

@Data
public class OrderDto {
    private int id;
    private Product product;
    private String productName;
    private String productDescription;
    private Farms farm;
    private String farmName;
    private int quantity;
    private LocalDateTime orderDate;
    private double orderValue;
    private String orderPaymentMethod;
    private String orderType;
    private List<ImageDto> images = new ArrayList<>();

    public void addImage(Images image) {
        ImageDto imageDTO = new ImageDto();
        imageDTO.setId(image.getId());
        imageDTO.setImg_url("https://d13aa073e03zd2.cloudfront.net/" + image.getImg_url());

        images.add(imageDTO);

    }
}
