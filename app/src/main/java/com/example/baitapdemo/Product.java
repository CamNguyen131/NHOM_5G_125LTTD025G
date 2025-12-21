package com.example.baitapdemo;
/**
 * Lớp mô hình (Model) đại diện cho một sản phẩm trong cửa hàng.
 */
public class Product {
    private String id;
    private String name;
    private String description;
    private double price;
    private float rating;
    private String imageUrl;
    private boolean isFavorite;

    // Constructor mặc định (cần thiết cho Firebase/JSON deserialization)
    public Product() {
    }

    // Constructor đầy đủ
    public Product(String id, String name, String description, double price, float rating, String imageUrl, boolean isFavorite) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.isFavorite = isFavorite;
    }

    // --- Getters ---

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public float getRating() {
        return rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    // --- Setters ---

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}