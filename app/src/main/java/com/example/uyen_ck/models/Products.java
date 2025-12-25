package com.example.uyen_ck.models;

import java.io.Serializable;

public class Products implements Serializable {
    private String productId;
    private String name;
    private String description;
    private double originalPrice;
    private double salePrice;
    private float rating;
    private String imageUrl;
    private boolean isFavorite;


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }
}