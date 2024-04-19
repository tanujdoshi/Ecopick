package com.example.backend.dto.response;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import com.example.backend.entities.Category;
import com.example.backend.entities.Farms;
import com.example.backend.entities.Images;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
    private int id;
    private String productName;
    private String productDescription;
    private double price;
    private int stock;
    private String unit;
    private CategoryDto category;
    private Category productCategory;
    private Farms farm;

    @Value("${prebook:false}")
    private boolean prebook;

    private List<ImageDto> images = new ArrayList<>();

    public void addImage(Images image) {
        ImageDto imageDTO = new ImageDto();
        imageDTO.setId(image.getId());
        imageDTO.setImg_url("https://d13aa073e03zd2.cloudfront.net/" + image.getImg_url());

        images.add(imageDTO);

    }

    // public void editImage(Ima)

}
