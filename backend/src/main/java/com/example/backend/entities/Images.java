package com.example.backend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "images")
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String img_url;

    @ManyToOne
    @JoinColumn(name = "farm_id")
    @ToString.Exclude
    @JsonBackReference
    private Farms farm;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @ToString.Exclude
    @JsonBackReference
    private Product product;
}
