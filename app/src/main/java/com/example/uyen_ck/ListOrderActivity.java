package com.example.uyen_ck;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uyen_ck.adapters.OrderAdapter;
import com.example.uyen_ck.models.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ListOrderActivity extends AppCompatActivity {

    private RecyclerView rcvOrders;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;
    private FirebaseFirestore db;
    private LinearLayout tabHome, tabCart, tabOrder, tabAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_order);

        db = FirebaseFirestore.getInstance();

        initViews();
        loadOrdersFromFirebase();
        setupBottomNavigation();
    }

    private void initViews() {
        rcvOrders = findViewById(R.id.rcvOrders);
        rcvOrders.setLayoutManager(new LinearLayoutManager(this));

        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(this, orderList);
        rcvOrders.setAdapter(orderAdapter);

        // Bottom Nav
        tabHome = findViewById(R.id.tabHome);
        tabCart = findViewById(R.id.tabCart);
        tabOrder = findViewById(R.id.tabOrder);
        tabAccount = findViewById(R.id.tabAccount);
    }

    private void loadOrdersFromFirebase() {
        // Cách sửa nhanh: Lấy TẤT CẢ đơn hàng (Bỏ qua lọc và sắp xếp)
        // Để kiểm tra xem dữ liệu có đổ về được không
        db.collection("orders")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    orderList.clear();
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            Order order = doc.toObject(Order.class);
                            if (order != null) {
                                order.setOrderId(doc.getId());
                                orderList.add(order);
                            }
                        }
                        orderAdapter.notifyDataSetChanged();
                        Toast.makeText(this, "Đã tải " + orderList.size() + " đơn hàng", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Kho dữ liệu trống rỗng!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("ListOrder", "Lỗi: " + e.getMessage());
                    Toast.makeText(this, "Lỗi kết nối: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void setupBottomNavigation() {
        if (tabHome != null) {
            tabHome.setOnClickListener(v -> {
                startActivity(new Intent(ListOrderActivity.this, HomeActivity.class));
                finish(); // Đóng trang hiện tại để không bị chồng Activity
            });
        }
        if (tabCart != null) {
            tabCart.setOnClickListener(v -> {
                startActivity(new Intent(ListOrderActivity.this, CartActivity.class));
                finish();
            });
        }
        if (tabAccount != null) {
            tabAccount.setOnClickListener(v -> {
                startActivity(new Intent(ListOrderActivity.this, MainActivity.class));
                finish();
            });
        }
        // Tab Order đang ở trang này nên không cần xử lý
    }
}