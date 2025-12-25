package com.example.uyen_ck.models;

import java.util.List;

public class Cart {

    private String cartId;
    private String userId;
    private long updatedAt;
    private List<CartDetail> items;

    // BẮT BUỘC cho Firestore
    public Cart() {}

    // ===== Getter & Setter =====
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

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<CartDetail> getItems() {
        return items;
    }

    public void setItems(List<CartDetail> items) {
        this.items = items;
    }

    // ===== HỖ TRỢ TÍNH TOÁN (KHÔNG lưu Firebase) =====
    public double getTotalPrice() {
        double total = 0;
        if (items != null) {
            for (CartDetail item : items) {
                total += item.getSubTotal();
            }
        }
        return total;
    }

    public int getTotalQuantity() {
        int count = 0;
        if (items != null) {
            for (CartDetail item : items) {
                count += item.getQuantity();
            }
        }
        return count;
    }
}
