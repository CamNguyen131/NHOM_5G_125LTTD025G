package com.example.uyen_ck.models;

import java.util.List;

public class Cart {
    private String cartId;
    private String userId;
    private long updatedAt;
    private List<CartDetail> items;

    public Cart() {}

    public String getCartId() { return cartId; }
    public String getUserId() { return userId; }
    public long getUpdatedAt() { return updatedAt; }
    public List<CartDetail> getItems() { return items; }
}
