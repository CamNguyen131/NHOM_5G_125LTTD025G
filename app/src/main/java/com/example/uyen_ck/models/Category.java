package com.example.uyen_ck.models;

public class Category {
    private String id;
    private String name;
    private String imageUrl;

    public Category() {}

    // Bổ sung các hàm này để sửa lỗi :56 trong hình 6
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}