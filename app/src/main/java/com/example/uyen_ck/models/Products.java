package com.example.uyen_ck.models;

public class Products {
    private String productId;
    private String name;
    private String brand;
    private double salePrice;
    private double originalPrice;
    private double discountPercentage;
    private String categoryId;
    private String imageUrl;
    private double rating;
    private String sellerId;
    private String description;
    private double stock;
    private String status;

    // Constructor mặc định cho Firestore
    public Products() {}

    // Constructor đầy đủ tham số
    public Products(String productId, String name, String brand, double salePrice, double originalPrice,
                    int discountPercentage, String categoryId, String imageUrl, double rating,
                    String sellerId, String description, int stock, String status) {
        this.productId = productId;
        this.name = name;
        this.brand = brand;
        this.salePrice = salePrice;
        this.originalPrice = originalPrice;
        this.discountPercentage = discountPercentage;
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.sellerId = sellerId;
        this.description = description;
        this.stock = stock;
        this.status = status;
    }

    // --- BẮT BUỘC PHẢI CÓ GETTER VÀ SETTER ---
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public double getSalePrice() { return salePrice; }
    public void setSalePrice(double salePrice) { this.salePrice = salePrice; }

    public double getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(double originalPrice) { this.originalPrice = originalPrice; }

    public double getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(int discountPercentage) { this.discountPercentage = discountPercentage; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

}