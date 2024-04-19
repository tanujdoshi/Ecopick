package com.example.backend.dto.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

import com.example.backend.entities.Images;

@Getter
@Setter
public class GetFarmByIdResponse {
    private String name;
    private String Address;
    private double lat;
    private double lng;
    private String Description;
    private List<ImageDto> images = new ArrayList<>();

    public void fetchImage(Images image) {
        ImageDto imageDTO = new ImageDto();
        imageDTO.setId(image.getId());
        imageDTO.setImg_url("https://d13aa073e03zd2.cloudfront.net/" + image.getImg_url());

        images.add(imageDTO);

    }

}
