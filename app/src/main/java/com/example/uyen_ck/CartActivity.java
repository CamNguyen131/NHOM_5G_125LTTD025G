package com.example.uyen_ck;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uyen_ck.adapters.CartAdapter;
import com.example.uyen_ck.models.Cart;
import com.example.uyen_ck.models.CartDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity
        implements CartAdapter.OnCartUpdateListener {

    private RecyclerView rvCart;
    private CartAdapter adapter;
    private List<CartDetail> itemList;

    private TextView txtTongTien, txtTamTinh;
    private Button btnCheckout;

    private LinearLayout tabHome, tabCart, tabOrder, tabAccount;

    private FirebaseFirestore db;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        db = FirebaseFirestore.getInstance();

        currentUserId = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : "user_03";

        initViews();
        setupBottomMenu();
        loadCartData();
    }

    private void initViews() {
        rvCart = findViewById(R.id.rvCart);
        txtTongTien = findViewById(R.id.txtTongTien);
        txtTamTinh = findViewById(R.id.txtTamTinh);
        btnCheckout = findViewById(R.id.btnCheckout);

        tabHome = findViewById(R.id.tabHome);
        tabCart = findViewById(R.id.tabCart);
        tabOrder = findViewById(R.id.tabOrder);
        tabAccount = findViewById(R.id.tabAccount);

        itemList = new ArrayList<>();
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(itemList, this);
        rvCart.setAdapter(adapter);

        btnCheckout.setOnClickListener(v -> {
            if (itemList.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng của bạn đang trống", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Tiến hành thanh toán", Toast.LENGTH_SHORT).show();
            }
        });

        // Highlight tab Giỏ hàng
        tabCart.setBackgroundResource(R.drawable.bg_pink_light);
    }

    private void setupBottomMenu() {

        tabHome.setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });

        tabCart.setOnClickListener(v -> {
            // Đang ở trang Giỏ hàng
        });

        tabOrder.setOnClickListener(v -> {
            startActivity(new Intent(this, ListOrderActivity.class));
            finish();
        });

        tabAccount.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    private void loadCartData() {
        db.collection("carts")
                .document("cart_user_" + currentUserId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

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

    private void calculateTotalPrice() {
        double total = 0;
        int totalQty = 0;

        for (CartDetail item : itemList) {
            total += item.getSubTotal();
            totalQty += item.getQuantity();
        }

        String priceFormatted = String.format("%,.0fđ", total);
        txtTamTinh.setText(priceFormatted);
        txtTongTien.setText(priceFormatted);
        btnCheckout.setText("Thanh toán (" + totalQty + " sản phẩm)");
    }

    @Override
    public void onUpdate(List<CartDetail> newList) {
        db.collection("carts")
                .document("cart_user_" + currentUserId)
                .update("items", newList,
                        "updatedAt", System.currentTimeMillis())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Cập nhật giỏ hàng thất bại", Toast.LENGTH_SHORT).show()
                );
    }
}
