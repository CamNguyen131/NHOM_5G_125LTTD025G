package com.example.uyen_ck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    // 1. Khai báo thêm các nút chức năng nhanh
    private LinearLayout tabOrder, tabCart, tabHome, tabAccount;
    private LinearLayout btnQuickOrder, btnQuickProduct, btnQuickCustomer, btnQuickReport;

    private TextView tvRevenue, tvNewOrders, tvTotalProducts, tvTotalCustomers, tvLowStockCount;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);

        db = FirebaseFirestore.getInstance();

        initViews();
        loadDashboardData();
    }

    private void initViews() {
        // Tabs Bottom
        tabOrder = findViewById(R.id.tabOrder);
        tabCart = findViewById(R.id.tabCart);
        tabHome = findViewById(R.id.tabHome);
        tabAccount = findViewById(R.id.tabAccount);


        // Stats
        tvRevenue = findViewById(R.id.tvRevenue);
        tvNewOrders = findViewById(R.id.tvNewOrders);
        tvTotalProducts = findViewById(R.id.tvTotalProducts);
        tvTotalCustomers = findViewById(R.id.tvTotalCustomers);
        tvLowStockCount = findViewById(R.id.tvLowStockCount);
    }

    private void loadDashboardData() {
        // 1. Tải dữ liệu Đơn hàng (Orders) -> Tính Doanh thu & Số đơn
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

                        // SỬA LỖI 1: Thêm dòng này để tắt cảnh báo ép kiểu
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> items = (List<Map<String, Object>>) doc.get("items");

                        if (items != null) {
                            for (Map<String, Object> item : items) {
                                try {
                                    double price = Double.parseDouble(String.valueOf(item.get("price")));
                                    int qty = Integer.parseInt(String.valueOf(item.get("quantity")));
                                    totalRevenue += (price * qty);
                                } catch (Exception e) {
                                    // Bỏ qua lỗi parse số
                                }
                            }
                        }
                    }

                    tvNewOrders.setText(String.valueOf(totalOrders));
                    tvTotalCustomers.setText(String.valueOf(uniqueCustomers.size()));

                    // SỬA LỖI 2: Dùng Locale.forLanguageTag thay cho new Locale()
                    NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
                    tvRevenue.setText(formatter.format(totalRevenue));
                }
            }
        });

        // 2. Tải dữ liệu Sản phẩm (Products)
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