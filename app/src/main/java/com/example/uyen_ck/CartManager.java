package com.example.uyen_ck;

public class CartManager {
    private static CartManager instance;
    private long totalPrice = 0;
    private int itemCount = 0;

    private CartManager() {}

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addItem(String name, int quantity, long pricePerItem) {
        totalPrice += pricePerItem * quantity;
        itemCount += quantity;
    }

    public long getTotalPrice() { return totalPrice; }
    public int getItemCount() { return itemCount; }

    public void clearCart() {
        totalPrice = 0;
        itemCount = 0;
    }
}