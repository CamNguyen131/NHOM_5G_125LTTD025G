package com.example.uyen_ck.models;

public class Category {
    private String categoryId;
    private String name;
    private String description;
    private String imageUrl;
    private boolean active;
    private long createdAt;

    public Category() {}

    // THÊM: Constructor có tham số để tạo item "Tất cả" hoặc khởi tạo nhanh
    public Category(String categoryId, String name, String imageUrl) {
        this.categoryId = categoryId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.active = true;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
