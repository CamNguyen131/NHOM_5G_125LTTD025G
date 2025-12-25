package com.example.uyen_ck.models;

public class CartDetail {
    private String productId;
    private String productName;
    private String productImage;
    private String variant;
    private int quantity;
    private double price;

    public CartDetail() {} // Firebase bắt buộc

    // ===== Getter & Setter =====
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductImage() { return productImage; }
    public void setProductImage(String productImage) { this.productImage = productImage; }

    public String getVariant() { return variant; }
    public void setVariant(String variant) { this.variant = variant; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    // ===== Tính tiền =====
    public double getSubTotal() {
        return price * quantity;
    }
}
