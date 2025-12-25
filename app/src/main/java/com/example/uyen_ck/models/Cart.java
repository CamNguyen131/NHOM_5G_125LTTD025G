package com.example.uyen_ck.models;

import java.util.List;

public class Cart {
    private String cartId;
    private String userId;
    private long updatedAt;
    private List<CartDetail> items;

    // Constructor rỗng (Bắt buộc cho Firebase)
    public Cart() {
    }

    // --- CÁC HÀM GETTER & SETTER (BẮT BUỘC PHẢI CÓ) ---

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) { // Đây là hàm đang bị thiếu
        this.cartId = cartId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) { // Đây là hàm đang bị thiếu
        this.userId = userId;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) { // Đây là hàm đang bị thiếu
        this.updatedAt = updatedAt;
    }

    public List<CartDetail> getItems() {
        return items;
    }

    public void setItems(List<CartDetail> items) { // Đây là hàm đang bị thiếu
        this.items = items;
    }
}