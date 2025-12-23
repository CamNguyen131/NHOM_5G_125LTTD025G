package com.example.uyen_ck;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
// Import tất cả các Model từ package của bạn
import com.example.uyen_ck.models.Category;
import com.example.uyen_ck.models.Order;
import com.example.uyen_ck.models.Product;
import com.example.uyen_ck.models.Shop;
import com.example.uyen_ck.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        // 1. Đổ dữ liệu sản phẩm (Hàm bạn đã viết)
        autoSeedProducts();

        // 2. Đổ các dữ liệu còn thiếu (Category, User, Shop, Order)
        seedMissingData();
    }

    private void seedMissingData() {
        Log.d("SEED", "Bắt đầu đổ dữ liệu Category, User, Shop, Order...");
        seedCategories();
        seedUsers();
        seedShops();
        seedOrders();
    }

    private void seedCategories() {
        String[][] data = {
                {"c1", "Chăm sóc da", "https://cdn-icons-png.flaticon.com/512/3107/3107118.png"},
                {"c2", "Trang điểm", "https://cdn-icons-png.flaticon.com/512/3524/3524752.png"},
                {"c3", "Chăm sóc tóc", "https://cdn-icons-png.flaticon.com/512/3107/3107101.png"},
                {"c4", "Nước hoa", "https://cdn-icons-png.flaticon.com/512/2622/2622416.png"},
                {"c5", "Dụng cụ làm đẹp", "https://cdn-icons-png.flaticon.com/512/3163/3163195.png"}
        };
        for (String[] item : data) {
            Category cat = new Category();
            cat.setId(item[0]); cat.setName(item[1]); cat.setImageUrl(item[2]);
            db.collection("categories").document(item[0]).set(cat)
                    .addOnSuccessListener(aVoid -> Log.d("SEED", "Thành công Category: " + item[1]));
        }
    }

    private void seedUsers() {
        for (int i = 1; i <= 5; i++) {
            User user = new User();
            user.setUid("user_0" + i);
            user.setDisplayName("Người dùng " + i);
            user.setEmail("user" + i + "@gmail.com");
            user.setRole(i <= 2 ? "seller" : "buyer");
            user.setCreatedAt(System.currentTimeMillis());
            db.collection("users").document(user.getUid()).set(user)
                    .addOnSuccessListener(aVoid -> Log.d("SEED", "Thành công User: " + user.getDisplayName()));
        }
    }

    private void seedShops() {
        String[] shopNames = {"Uyên Store", "Beauty Queen", "Korea Cosmetics", "Authentic Shop", "Nàng Thơ"};
        for (int i = 1; i <= 5; i++) {
            Shop shop = new Shop();
            shop.setOwnerId("user_0" + i);
            shop.setShopName(shopNames[i-1]);
            shop.setShopPhone("098765432" + i);
            shop.setRating(4.5 + (i * 0.1));
            shop.setStatus("active");
            db.collection("shops").document(shop.getOwnerId()).set(shop)
                    .addOnSuccessListener(aVoid -> Log.d("SEED", "Thành công Shop: " + shop.getShopName()));
        }
    }

    private void seedOrders() {
        for (int i = 1; i <= 5; i++) {
            Order order = new Order();
            order.setOrderId("order_" + i);
            order.setOrderCode("DH100" + i);
            order.setBuyerId("user_03");
            order.setTotalAmount(250000.0 + (i * 50000));
            order.setStatus("pending");
            order.setCreatedAt(System.currentTimeMillis());

            List<Map<String, Object>> items = new ArrayList<>();
            Map<String, Object> item = new HashMap<>();
            item.put("productId", "p1");
            item.put("name", "Sản phẩm trong đơn " + i);
            item.put("quantity", 1);
            items.add(item);
            order.setItems(items);

            db.collection("orders").document(order.getOrderId()).set(order)
                    .addOnSuccessListener(aVoid -> Log.d("SEED", "Thành công Order: " + order.getOrderCode()));
        }
    }

    private void autoSeedProducts() {
        // Giữ nguyên code tạo Product bạn đã viết ở bước trước
        // Đảm bảo có log onSuccess để theo dõi
    }
}