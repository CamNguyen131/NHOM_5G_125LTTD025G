package com.example.uyen_ck;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.uyen_ck.models.Order;
import com.example.uyen_ck.models.OrderDetails;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderDetailActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private String orderId;
    private Order currentOrder; // Lưu đơn hàng hiện tại để dùng cho nút Mua lại

    // View elements
    private TextView tvCode, tvDate, tvStatus, tvTimelineDate;
    private TextView tvBuyerName, tvPhone, tvAddress, tvNote;
    private TextView tvSubTotal, tvTotal;
    private LinearLayout llProductContainer;
    private LinearLayout btnBuyAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        db = FirebaseFirestore.getInstance();

        if (getIntent() != null) {
            orderId = getIntent().getStringExtra("orderId");
        }

        initViews();
        setupEvents();

        if (orderId != null) {
            loadOrderDetails();
        } else {
            Toast.makeText(this, "Không tìm thấy đơn hàng", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        tvCode = findViewById(R.id.tvDetailCode);
        tvDate = findViewById(R.id.tvDetailDate);
        tvStatus = findViewById(R.id.tvDetailStatus);
        tvTimelineDate = findViewById(R.id.tvDetailTimelineDate);
        tvBuyerName = findViewById(R.id.tvDetailBuyerName);
        tvPhone = findViewById(R.id.tvDetailPhone);
        tvAddress = findViewById(R.id.tvDetailAddress);
        tvNote = findViewById(R.id.tvDetailNote);
        llProductContainer = findViewById(R.id.llDetailProducts);
        tvSubTotal = findViewById(R.id.tvDetailSubTotal);
        tvTotal = findViewById(R.id.tvDetailTotal);
        btnBuyAgain = findViewById(R.id.btnDetailBuyAgain);
    }

    private void loadOrderDetails() {
        db.collection("orders").document(orderId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        currentOrder = documentSnapshot.toObject(Order.class); // Lưu vào biến toàn cục
                        if (currentOrder != null) {
                            displayData(currentOrder);
                        }
                    } else {
                        Toast.makeText(this, "Đơn hàng không tồn tại", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi kết nối: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void displayData(Order order) {
        tvCode.setText("Mã đơn: " + order.getOrderCode());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("vi", "VN"));
        String dateStr = sdf.format(new Date(order.getCreatedAt()));
        tvDate.setText(dateStr);
        tvTimelineDate.setText(dateStr);

        String status = order.getStatus();
        String displayStatus = status;
        int color = Color.BLACK;

        if ("pending".equalsIgnoreCase(status) || "Pending".equalsIgnoreCase(status)) {
            displayStatus = "Chờ xác nhận"; color = Color.parseColor("#FF9800");
        } else if ("shipping".equalsIgnoreCase(status)) {
            displayStatus = "Đang giao"; color = Color.parseColor("#2196F3");
        } else if ("completed".equalsIgnoreCase(status)) {
            displayStatus = "Đã giao"; color = Color.parseColor("#4CAF50");
        }
        tvStatus.setText(displayStatus);
        tvStatus.setTextColor(color);

        tvBuyerName.setText(order.getBuyerName());
        tvPhone.setText(order.getPhoneNumber());
        tvAddress.setText(order.getDeliveryAddress());
        tvNote.setText(order.getNote() != null && !order.getNote().isEmpty() ? order.getNote() : "Không có");

        // --- HIỂN THỊ DANH SÁCH SẢN PHẨM ---
        llProductContainer.removeAllViews();
        List<OrderDetails> items = order.getItems();

        if (items != null) {
            for (Object itemObj : items) {
                View itemView = LayoutInflater.from(this).inflate(R.layout.item_order_product_row, llProductContainer, false);

                ImageView imgItem = itemView.findViewById(R.id.imgProductItem);
                TextView tvName = itemView.findViewById(R.id.tvProductItemName);
                TextView tvQty = itemView.findViewById(R.id.tvProductItemQty);

                String name = "";
                String imgUrl = "";
                int qty = 1;
                String currentProductId = ""; // ID sản phẩm để click

                if (itemObj instanceof OrderDetails) {
                    OrderDetails detail = (OrderDetails) itemObj;
                    name = detail.getProductName();
                    imgUrl = detail.getProductImage();
                    qty = detail.getQuantity();
                    currentProductId = detail.getProductId(); // Lấy ID
                } else if (itemObj instanceof Map) {
                    Map<String, Object> map = (Map<String, Object>) itemObj;
                    if (map.containsKey("productName")) name = (String) map.get("productName");
                    if (map.containsKey("productImage")) imgUrl = (String) map.get("productImage");
                    if (map.containsKey("quantity")) qty = Integer.parseInt(String.valueOf(map.get("quantity")));
                    if (map.containsKey("productId")) currentProductId = (String) map.get("productId");
                }

                tvName.setText(name);
                tvQty.setText("x" + qty);

                if (imgUrl != null && !imgUrl.isEmpty()) {
                    Glide.with(this).load(imgUrl).into(imgItem);
                }

                // --- SỰ KIỆN: CLICK VÀO 1 SẢN PHẨM TRONG LIST -> MỞ CHI TIẾT ---
                String finalProductId = currentProductId;
                itemView.setOnClickListener(v -> {
                    if (finalProductId != null && !finalProductId.isEmpty()) {
                        navigateToProductDetail(finalProductId);
                    } else {
                        Toast.makeText(OrderDetailActivity.this, "Sản phẩm không còn tồn tại", Toast.LENGTH_SHORT).show();
                    }
                });

                llProductContainer.addView(itemView);
            }
        }

        tvSubTotal.setText(formatCurrency(order.getTotalAmount()));
        tvTotal.setText(formatCurrency(order.getTotalAmount()));
    }

    private String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(amount);
    }

    private void setupEvents() {
        findViewById(R.id.back_button).setOnClickListener(v -> finish());

        // --- SỰ KIỆN NÚT "MUA LẠI" (FOOTER) ---
        if (btnBuyAgain != null) {
            btnBuyAgain.setOnClickListener(v -> {
                if (currentOrder != null && currentOrder.getItems() != null && !currentOrder.getItems().isEmpty()) {
                    // Logic: Lấy sản phẩm đầu tiên trong đơn hàng để mở trang chi tiết
                    // Vì ProductDetailActivity chỉ hiển thị 1 sản phẩm
                    Object firstItem = currentOrder.getItems().get(0);
                    String firstProductId = "";

                    if (firstItem instanceof OrderDetails) {
                        firstProductId = ((OrderDetails) firstItem).getProductId();
                    } else if (firstItem instanceof Map) {
                        firstProductId = (String) ((Map) firstItem).get("productId");
                    }

                    if (firstProductId != null && !firstProductId.isEmpty()) {
                        navigateToProductDetail(firstProductId);
                    } else {
                        Toast.makeText(this, "Không thể xác định sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Đơn hàng trống", Toast.LENGTH_SHORT).show();
                }
            });
        }

        setupBottomNavigation();
    }

    // Hàm chuyển trang chung
    private void navigateToProductDetail(String productId) {
        Intent intent = new Intent(OrderDetailActivity.this, ProductDetailActivity.class);
        // Truyền đúng key "product_id" mà ProductDetailActivity đang hứng
        intent.putExtra("product_id", productId);
        startActivity(intent);
    }

    private void setupBottomNavigation() {
        LinearLayout tabAccount = findViewById(R.id.tabAccount);
        LinearLayout tabHome = findViewById(R.id.tabHome); // Thêm các tab khác nếu cần

        if (tabAccount != null) {
            tabAccount.setOnClickListener(v -> {
                startActivity(new Intent(this, MainActivity.class));
            });
        }
        if (tabHome != null) {
            tabHome.setOnClickListener(v -> {
                startActivity(new Intent(this, HomeActivity.class));
            });
        }
    }
}