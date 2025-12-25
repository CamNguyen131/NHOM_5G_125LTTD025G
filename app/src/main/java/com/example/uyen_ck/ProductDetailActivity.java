package com.example.uyen_ck;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.uyen_ck.models.Products;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imgProductLarge;
    private TextView tvProductName, tvProductPrice, tvOldPrice,
            tvProductDescription, tvQuantity;
    private ImageButton btnBack, btnMinus, btnPlus;
    private Button btnAddCart, btnBuyNow;

    private FirebaseFirestore db;
    private String productId;
    private Products currentProduct;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initViews();
        setupEvents();

        db = FirebaseFirestore.getInstance();

        productId = getIntent().getStringExtra("product_id");
        if (productId == null || productId.isEmpty()) {
            Toast.makeText(this, "Không nhận được ID sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadProductDetail();
    }

    // ================= INIT VIEW =================
    private void initViews() {
        imgProductLarge = findViewById(R.id.imgProductLarge);
        tvProductName = findViewById(R.id.tvProductName);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        tvOldPrice = findViewById(R.id.tvOldPrice);
        tvProductDescription = findViewById(R.id.tvProductDescription);
        tvQuantity = findViewById(R.id.tvQuantity);

        btnBack = findViewById(R.id.btnBack);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        btnAddCart = findViewById(R.id.btnAddCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);
    }

    // ================= EVENTS =================
    private void setupEvents() {

        btnBack.setOnClickListener(v -> finish());

        btnPlus.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
        });

        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });

        btnAddCart.setOnClickListener(v -> openVariantSheet("add_to_cart"));

        btnBuyNow.setOnClickListener(v -> openVariantSheet("buy_now"));
    }

    // ================= LOAD PRODUCT =================
    private void loadProductDetail() {
        db.collection("products")
                .document(productId)
                .get()
                .addOnSuccessListener(this::handleProductResult)
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi tải sản phẩm", Toast.LENGTH_SHORT).show()
                );
    }

    private void handleProductResult(DocumentSnapshot doc) {
        if (!doc.exists()) {
            Toast.makeText(this, "Sản phẩm không tồn tại", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentProduct = doc.toObject(Products.class);
        if (currentProduct == null) return;

        currentProduct.setProductId(doc.getId());
        displayData();
    }

    // ================= DISPLAY =================
    private void displayData() {
        tvProductName.setText(currentProduct.getName());
        tvProductPrice.setText(String.format("%,.0fđ", currentProduct.getSalePrice()));
        tvOldPrice.setText(String.format("%,.0fđ", currentProduct.getOriginalPrice()));
        tvProductDescription.setText(currentProduct.getDescription());

        Glide.with(this)
                .load(currentProduct.getImageUrl())
                .placeholder(R.drawable.lo_roche_posay)
                .error(R.drawable.lo_roche_posay)
                .into(imgProductLarge);
    }

    // ================= VARIANT =================
    private void openVariantSheet(String actionType) {
        VariantBottomSheet sheet = VariantBottomSheet.newInstance(
                currentProduct,
                quantity,
                actionType
        );
        sheet.show(getSupportFragmentManager(), "VariantBottomSheet");
    }

    // ================= ADD TO CART (được gọi từ BottomSheet) =================
    public void addToCart(Products product, int quantity, String variant) {

        String userId = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : "user_03";

        String cartId = "cart_user_" + userId;

        Map<String, Object> item = new HashMap<>();
        item.put("productId", product.getProductId());
        item.put("productName", product.getName());
        item.put("productImage", product.getImageUrl());
        item.put("variant", variant);
        item.put("quantity", quantity);
        item.put("price", product.getSalePrice());

        db.collection("carts")
                .document(cartId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        db.collection("carts")
                                .document(cartId)
                                .update(
                                        "items", FieldValue.arrayUnion(item),
                                        "updatedAt", System.currentTimeMillis()
                                );
                    } else {
                        List<Map<String, Object>> items = new ArrayList<>();
                        items.add(item);

                        Map<String, Object> cart = new HashMap<>();
                        cart.put("cartId", cartId);
                        cart.put("userId", userId);
                        cart.put("items", items);
                        cart.put("updatedAt", System.currentTimeMillis());

                        db.collection("carts")
                                .document(cartId)
                                .set(cart);
                    }

                    Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, CartActivity.class));
                });
    }
}
