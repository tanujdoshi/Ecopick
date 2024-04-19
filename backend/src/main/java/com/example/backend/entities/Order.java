package com.example.backend.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @ToString.Exclude
    @JsonBackReference
    private Product product;

    @ManyToOne
    @JoinColumn(name = "farm_id")
    @ToString.Exclude
    @JsonBackReference
    private Farms farm;

    private int quantity;

    private LocalDateTime orderDate;
    private double orderValue;
    private String orderPaymentMethod;

    @Column(columnDefinition = "varchar(255) default 'ORDER'")
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    @ToString.Exclude
    @JsonBackReference
    private Subscription subscription;
}
