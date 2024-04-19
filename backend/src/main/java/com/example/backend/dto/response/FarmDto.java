package com.example.backend.dto.response;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

import com.example.backend.entities.Images;

@Getter
@Setter
public class FarmDto {
    private int id;
    private String name;
    private String Address;
    private String Description;
    private double lat;
    private double lng;
    private List<ImageDto> images = new ArrayList<>();

    public void addImage(Images image) {
        ImageDto imageDTO = new ImageDto();
        imageDTO.setId(image.getId());
        imageDTO.setImg_url("https://d13aa073e03zd2.cloudfront.net/" + image.getImg_url());

        images.add(imageDTO);

    }
}
