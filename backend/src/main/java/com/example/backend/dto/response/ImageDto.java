package com.example.backend.dto.response;

public class ImageDto {
    private int id;
    private String img_url;

    public ImageDto() {
    }

    public int getId() {
        return id;
    }

    // Setter for id
    public void setId(int id) {
        this.id = id;
    }

    // Getter for img_url
    public String getImg_url() {
        return img_url;
    }

    // Setter for img_url
    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

}
