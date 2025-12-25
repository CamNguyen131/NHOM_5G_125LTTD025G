package com.example.uyen_ck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class activity_seller_dashboard extends AppCompatActivity {

    // 1. Khai báo 4 nút Menu dưới đáy (Theo hình bạn gửi)
    private LinearLayout tabOverview, tabProducts, tabMessages, tabAccount;

    // Các biến thống kê (Giữ nguyên)
    private TextView tvRevenue, tvNewOrders, tvTotalProducts, tvTotalCustomers, tvLowStockCount;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);

        db = FirebaseFirestore.getInstance();

        initViews();
        setupMenuEvents(); // Hàm xử lý click menu
        loadDashboardData();
    }

    private void initViews() {
        // --- ÁNH XẠ MENU DƯỚI ĐÁY ---
        // Bạn cần vào file activity_seller_dashboard.xml đặt lại ID cho đúng các tên này:
        tabOverview = findViewById(R.id.tab_overview); // Nút Tổng quan (Trang hiện tại)
        tabProducts = findViewById(R.id.tab_products); // Nút Sản phẩm
        tabMessages = findViewById(R.id.tab_messages); // Nút Tin nhắn
        tabAccount = findViewById(R.id.tabAccount);   // Nút Cá nhân

        // --- ÁNH XẠ CÁC SỐ LIỆU THỐNG KÊ ---
        tvRevenue = findViewById(R.id.tvRevenue);
        tvNewOrders = findViewById(R.id.tvNewOrders);
        tvTotalProducts = findViewById(R.id.tvTotalProducts);
        tvTotalCustomers = findViewById(R.id.tvTotalCustomers);
        tvLowStockCount = findViewById(R.id.tvLowStockCount);
    }

    private void setupMenuEvents() {
        // 1. Nút Sản phẩm -> Chuyển sang ProductList
        if (tabProducts != null) {
            tabProducts.setOnClickListener(v -> {
                Intent intent = new Intent(activity_seller_dashboard.this, ProductListActivity.class);
                startActivity(intent);
                // Không finish() để người dùng có thể back lại Dashboard
            });
        }

        // 2. Nút Tin nhắn -> Chuyển sang MessagesList
        if (tabMessages != null) {
            tabMessages.setOnClickListener(v -> {
                Intent intent = new Intent(activity_seller_dashboard.this, MessageListActivity.class);
                startActivity(intent);
            });
        }

        // 3. Nút Cá nhân -> Chuyển sang MainActivity
        if (tabAccount != null) {
            tabAccount.setOnClickListener(v -> {
                Intent intent = new Intent(activity_seller_dashboard.this, MainActivity.class);
                startActivity(intent);
            });
        }

        // 4. Nút Tổng quan (Đang ở trang này nên không cần làm gì, hoặc reload)
        if (tabOverview != null) {
            tabOverview.setOnClickListener(v -> {
                // Tùy chọn: Scroll lên đầu hoặc reload
            });
        }
    }

    private void loadDashboardData() {
        // --- GIỮ NGUYÊN PHẦN LOGIC TÍNH TOÁN CỦA BẠN ---

        // 1. Tính doanh thu và đơn hàng
        db.collection("orders").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshots = task.getResult();
                if (snapshots != null) {
                    long totalOrders = snapshots.size();
                    double totalRevenue = 0;
                    Set<String> uniqueCustomers = new HashSet<>();

                    for (DocumentSnapshot doc : snapshots.getDocuments()) {
                        String buyerId = doc.getString("buyerId");
                        if (buyerId != null) uniqueCustomers.add(buyerId);

                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> items = (List<Map<String, Object>>) doc.get("items");

                        if (items != null) {
                            for (Map<String, Object> item : items) {
                                try {
                                    double price = Double.parseDouble(String.valueOf(item.get("price")));
                                    int qty = Integer.parseInt(String.valueOf(item.get("quantity")));
                                    totalRevenue += (price * qty);
                                } catch (Exception e) {}
                            }
                        }
                    }
                    tvNewOrders.setText(String.valueOf(totalOrders));
                    tvTotalCustomers.setText(String.valueOf(uniqueCustomers.size()));

                    NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
                    tvRevenue.setText(formatter.format(totalRevenue));
                }
            }
        });

        // 2. Tính tồn kho sản phẩm
        db.collection("products").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshots = task.getResult();
                if (snapshots != null) {
                    int totalProducts = snapshots.size();
                    int lowStockCount = 0;

                    for (DocumentSnapshot doc : snapshots.getDocuments()) {
                        Long stock = doc.getLong("stock");
                        if (stock != null && stock < 10) {
                            lowStockCount++;
                        }
                    }
                    tvTotalProducts.setText(String.valueOf(totalProducts));
                    tvLowStockCount.setText("Sản phẩm sắp hết hàng (" + lowStockCount + ")");
                }
            }
        });
    }
}