package com.example.backend.dto.response;

import java.util.ArrayList;
import java.util.List;
import com.example.backend.entities.Images;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSubscriptionResponse {
    private int id;
    private String productName;
    private String productDescription;
    private double price;
    private int stock;
    private String unit;
    private List<ImageDto> images = new ArrayList<>();

    public void addImage(Images image) {
        ImageDto imageDTO = new ImageDto();
        imageDTO.setId(image.getId());
        imageDTO.setImg_url("https://d13aa073e03zd2.cloudfront.net/" + image.getImg_url());

        images.add(imageDTO);

    }
}
