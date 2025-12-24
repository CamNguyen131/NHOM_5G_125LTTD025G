package com.example.uyen_ck;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.uyen_ck.models.Products;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imgProductLarge;
    private TextView tvProductName, tvProductPrice, tvOldPrice, tvProductDescription, tvQuantity;
    private ImageButton btnBack, btnMinus, btnPlus, btnChat, btnHeart;
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
        db = FirebaseFirestore.getInstance();

        // Lấy productId từ Intent - Đảm bảo bên gửi dùng đúng key "product_id"
        productId = getIntent().getStringExtra("product_id");
        Log.d("DEBUG_DETAIL", "Received Product ID: " + productId);
        Log.d("DEBUG_ID", "ID nhan duoc: " + productId);
        if (productId != null && !productId.isEmpty()) {
            loadProductDetail(productId);
        } else {
            Toast.makeText(this, "Lỗi: Không nhận được ID sản phẩm!", Toast.LENGTH_SHORT).show();
            finish();
        }

        setupEvents();
    }

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
        btnChat = findViewById(R.id.btnChat);
        btnHeart = findViewById(R.id.btnHeart);
        btnAddCart = findViewById(R.id.btnAddCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);
    }

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

        // Sự kiện Thêm vào giỏ (actionType = "add_to_cart")
        btnAddCart.setOnClickListener(v -> {
            if (currentProduct != null) {
                VariantBottomSheet variantSheet = VariantBottomSheet.newInstance(
                        currentProduct.getName(),
                        quantity,
                        (long) currentProduct.getSalePrice(),
                        "add_to_cart"
                );
                variantSheet.show(getSupportFragmentManager(), "VariantBottomSheet");
            }
        });

        // Trong hàm setupEvents() của ProductDetailActivity.java
        btnBuyNow.setOnClickListener(v -> {
            if (currentProduct != null) {
                VariantBottomSheet variantSheet = VariantBottomSheet.newInstance(
                        currentProduct.getName(),
                        quantity, // Số lượng hiện tại trên UI
                        (long) currentProduct.getSalePrice(),
                        "buy_now" // Chỉ định hành động là mua ngay
                );
                variantSheet.show(getSupportFragmentManager(), "VariantBottomSheet");
            }
        });
    }

    // Hàm dùng chung để mở BottomSheet chọn màu/size
    private void openVariantSheet(String actionType) {
        if (currentProduct != null) {
            VariantBottomSheet variantSheet = VariantBottomSheet.newInstance(
                    currentProduct.getName(),
                    quantity,
                    (long) currentProduct.getSalePrice(),
                    actionType // Truyền "add_to_cart" hoặc "buy_now"
            );
            variantSheet.show(getSupportFragmentManager(), "VariantBottomSheet");
        }
    }


    private void loadProductDetail(String id) {
        // Truy vấn vào collection "products" với Document ID nhận được
        db.collection("products").document(id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Chuyển đổi dữ liệu từ Firestore sang Object Products
                        currentProduct = documentSnapshot.toObject(Products.class);
                        if (currentProduct != null) {
                            currentProduct.setProductId(documentSnapshot.getId());
                            displayData(currentProduct);
                        }
                    } else {
                        Log.e("FIRESTORE", "Sản phẩm không tồn tại với ID: " + id);
                        Toast.makeText(this, "Không tìm thấy thông tin sản phẩm!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi kết nối: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void displayData(Products product) {
        // Đổ dữ liệu vào giao diện đã ánh xạ trong initViews()
        tvProductName.setText(product.getName());

        // Định dạng giá tiền (Ví dụ: 850000 -> 850.000đ)
        tvProductPrice.setText(String.format("%,.0fđ", product.getSalePrice()));
        tvOldPrice.setText(String.format("%,.0fđ", product.getOriginalPrice()));

        // Hiển thị mô tả sản phẩm
        if (product.getDescription() != null && !product.getDescription().isEmpty()) {
            tvProductDescription.setText(product.getDescription());
        }

        // Sử dụng Glide để tải ảnh sản phẩm từ URL
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.lo_roche_posay) // Ảnh tạm trong khi chờ tải
                    .error(R.drawable.lo_roche_posay)      // Ảnh khi lỗi tải
                    .into(imgProductLarge);
        }
    }
}