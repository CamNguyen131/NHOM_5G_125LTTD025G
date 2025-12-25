package com.example.uyen_ck;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.uyen_ck.adapters.CartAdapter;
import com.example.uyen_ck.models.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartUpdateListener {
    private RecyclerView rvCart;
    private CartAdapter adapter;
    private List<CartDetail> itemList = new ArrayList<>();
    private TextView txtTongTien, txtTamTinh, tvHeaderTitle;
    private Button btnCheckout;
    private FirebaseFirestore db;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        db = FirebaseFirestore.getInstance();
        // Lấy ID người dùng thực tế từ Auth hoặc mặc định user_03 để test
        currentUserId = (FirebaseAuth.getInstance().getCurrentUser() != null)
                ? FirebaseAuth.getInstance().getCurrentUser().getUid() : "user_03";

        initViews();
        loadCartData();
        setupBottomNavigation();
    }

    private void initViews() {
        rvCart = findViewById(R.id.rvCart);
        txtTongTien = findViewById(R.id.txtTongTien);
        txtTamTinh = findViewById(R.id.txtTamTinh);
        btnCheckout = findViewById(R.id.btnCheckout);

        rvCart.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(itemList, this);
        rvCart.setAdapter(adapter);
    }

    private void loadCartData() {
        db.collection("carts")
                .document("cart_user_" + currentUserId)
                .addSnapshotListener((value, error) -> {

                    if (error != null) return;

                    if (value != null && value.exists()) {
                        Cart cart = value.toObject(Cart.class);

                        if (cart != null && cart.getItems() != null) {
                            itemList.clear();
                            itemList.addAll(cart.getItems());
                            adapter.notifyDataSetChanged();
                            calculateTotalPrice();
                        }
                    }
                });
    }


    // Hàm tính tổng tiền cộng dồn của toàn bộ giỏ hàng
    private void calculateTotalPrice() {
        double grandTotal = 0;
        int totalProducts = 0;

        for (CartDetail item : itemList) {
            grandTotal += item.getSubTotal(); // Cộng dồn: (Giá * Số lượng) của từng món
            totalProducts += item.getQuantity();
        }

        String formattedPrice = String.format("%,.0fđ", grandTotal);
        txtTamTinh.setText(formattedPrice);
        txtTongTien.setText(formattedPrice);

        // Cập nhật số lượng hiển thị trên nút thanh toán và tiêu đề
        btnCheckout.setText("Thanh toán (" + itemList.size() + " sản phẩm)");
    }

    @Override
    public void onUpdate(List<CartDetail> newList) {
        db.collection("carts")
                .document("cart_user_" + currentUserId)
                .update(
                        "items", newList,
                        "updatedAt", System.currentTimeMillis()
                )
                .addOnSuccessListener(aVoid -> calculateTotalPrice());
    }
    private void setupBottomNavigation() {
        LinearLayout tabHome = findViewById(R.id.tabHome);
        LinearLayout tabCart = findViewById(R.id.tabCart);
        LinearLayout tabOrder = findViewById(R.id.tabOrder);
        LinearLayout tabAccount = findViewById(R.id.tabAccount);

        // Trang chủ
        if (tabHome != null) {
            tabHome.setOnClickListener(v -> {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            });
        }

        // Giỏ hàng (đang ở đây → KHÔNG chuyển)
        if (tabCart != null) {
            tabCart.setOnClickListener(v -> {
                // Không làm gì
            });
        }

        // Đơn hàng
        if (tabOrder != null) {
            tabOrder.setOnClickListener(v -> {
                startActivity(new Intent(this, ListOrderActivity.class));
                finish();
            });
        }

        // Tài khoản
        if (tabAccount != null) {
            tabAccount.setOnClickListener(v -> {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            });
        }
    }

}