package com.example.uyen_ck.models;

import java.util.List;

public class Cart {
    private String cartId;          // thường = userId
    private String userId;
    private List<CartDetail> items;
    private long updatedAt;

    public Cart() {}

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CartDetail> getItems() {
        return items;
    }

    public void setItems(List<CartDetail> items) {
        this.items = items;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
