package com.example.uyen_ck.models;

public class OrderDetails {
    private String orderDetailsId;
    private String orderId;      // FK -> Order.orderId
    private String productId;    // FK -> Product
    private String productName;  // Tên SP tại thời điểm mua
    private String productImage; // Ảnh SP
    private double price;        // Giá tại thời điểm mua
    private int quantity;
    private double subTotal;
    private String variant;

    public OrderDetails() {}

    public OrderDetails(String productName, int quantity, double price, String variant) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.variant = variant;
        this.subTotal = price * quantity; // Tự động tính tổng tiền cho món này
    }

    // --- Getter và Setter ---
    public String getOrderDetailsId() { return orderDetailsId; }
    public void setOrderDetailsId(String orderDetailsId) { this.orderDetailsId = orderDetailsId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductImage() { return productImage; }
    public void setProductImage(String productImage) { this.productImage = productImage; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getSubTotal() { return subTotal; }
    public void setSubTotal(double subTotal) { this.subTotal = subTotal; }

    public String getVariant() { return variant; }
    public void setVariant(String variant) { this.variant = variant; }
}