package com.example.uyen_ck.models;

public class CartDetail {
    private String productId;
    private String productName;
    private String productImage;
    private String variant;
    private int quantity;
    private double price;

    // BẮT BUỘC cho Firebase
    public CartDetail() {}

    public double getSubTotal() {
        return price * quantity;
    }

    // Getters
    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getProductImage() { return productImage; }
    public String getVariant() { return variant; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }

    // Setters
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
