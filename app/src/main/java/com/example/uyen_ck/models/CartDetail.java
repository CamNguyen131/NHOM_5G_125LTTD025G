package com.example.uyen_ck.models;

public class CartDetail {
    private String productId;
    private String productName;
    private String productImage;
    private double price;
    private int quantity;
    private double subTotal;

    public CartDetail() {}

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        calculateSubTotal();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateSubTotal();
    }

    public double getSubTotal() {
        return subTotal;
    }

    private void calculateSubTotal() {
        this.subTotal = price * quantity;
    }
}
