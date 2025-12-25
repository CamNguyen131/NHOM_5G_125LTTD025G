package com.example.uyen_ck;

import android.os.Bundle;
import android.widget.Button;
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

public class CartActivity extends AppCompatActivity
        implements CartAdapter.OnCartUpdateListener {

    private RecyclerView rvCart;
    private CartAdapter adapter;
    private List<CartDetail> itemList = new ArrayList<>();

    private TextView txtTongTien, txtTamTinh;
    private Button btnCheckout;

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
        loadCartData();
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

        String price = String.format("%,.0fđ", total);
        txtTamTinh.setText(price);
        txtTongTien.setText(price);
        btnCheckout.setText("Thanh toán (" + totalQty + " sản phẩm)");
    }

    @Override
    public void onUpdate(List<CartDetail> newList) {
        db.collection("carts")
                .document("cart_user_" + currentUserId)
                .update("items", newList,
                        "updatedAt", System.currentTimeMillis())
                .addOnSuccessListener(unused -> calculateTotalPrice());
    }
}
