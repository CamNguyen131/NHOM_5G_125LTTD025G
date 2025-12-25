package com.example.uyen_ck.models;

import java.io.Serializable;

public class CartDetail implements Serializable {
    private String productId;
    private String productName;
    private String productImage;
    private String variant;
    private int quantity;
    private double price;

    // 1. Constructor mặc định (BẮT BUỘC cho Firebase Firestore)
    public CartDetail() {
    }

    // 2. Constructor có tham số để khởi tạo nhanh
    public CartDetail(String productId, String productName, String productImage, String variant, int quantity, double price) {
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.variant = variant;
        this.quantity = quantity;
        this.price = price;
    }

    // 3. Phương thức tính tổng tiền của một món hàng (Giá * Số lượng)
    public double getSubTotal() {
        return price * quantity;
    }

    // --- Getter & Setter ---

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

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}