package com.example.uyen_ck.models;

public class OrderDetails {
    private String orderDetailsId;
    private String orderId;      // FK -> Order.orderId
    private String productId;    // FK -> Product
    private String productName;  // Tên SP tại thời điểm mua
    private String productImage; // Ảnh SP
    private double price;        // Giá tại thời điểm mua
    private int quantity;
    private double subTotal;     // price * quantity

    public OrderDetails() {}

    public String getOrderDetailsId() {
        return orderDetailsId;
    }

    public void setOrderDetailId(String orderDetailsId) {
        this.orderDetailsId = orderDetailsId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }
}
