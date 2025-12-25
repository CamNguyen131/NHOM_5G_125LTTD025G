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

    // Khai báo biến giao diện
    private ImageView imgProductLarge;
    private TextView tvProductName, tvProductPrice, tvOldPrice, tvProductDescription, tvQuantity;
    private ImageButton btnBack, btnMinus, btnPlus, btnChat, btnHeart;
    private Button btnAddCart, btnBuyNow;

    // Khai báo biến dữ liệu
    private FirebaseFirestore db;
    private String productId;
    private Products currentProduct;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // 1. Khởi tạo View
        initViews();

        // 2. Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // 3. Nhận dữ liệu từ Intent
        productId = getIntent().getStringExtra("product_id");

        // Debug log để kiểm tra ID
        Log.d("DEBUG_DETAIL", "Received Product ID: " + productId);

        if (productId != null && !productId.isEmpty()) {
            loadProductDetail(productId);
        } else {
            Toast.makeText(this, "Lỗi: Không nhận được ID sản phẩm!", Toast.LENGTH_SHORT).show();
            finish(); // Đóng Activity nếu không có ID
        }

        // 4. Thiết lập sự kiện click
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
        // Nút Back
        btnBack.setOnClickListener(v -> finish());

        // Nút Tăng số lượng
        btnPlus.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
        });

        // Nút Giảm số lượng
        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });

        // Nút Thêm vào giỏ hàng
        btnAddCart.setOnClickListener(v -> openVariantSheet("add_to_cart"));

        // Nút Mua ngay
        btnBuyNow.setOnClickListener(v -> openVariantSheet("buy_now"));
    }

    // Hàm load dữ liệu từ Firebase
    private void loadProductDetail(String id) {
        db.collection("products").document(id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Convert dữ liệu sang Object
                        currentProduct = documentSnapshot.toObject(Products.class);

                        if (currentProduct != null) {
                            // Đôi khi object convert xong chưa có ID, set thủ công để chắc chắn
                            currentProduct.setProductId(documentSnapshot.getId());
                            displayData(currentProduct);
                        }
                    } else {
                        Log.e("FIRESTORE", "Không tìm thấy Document với ID: " + id);
                        Toast.makeText(this, "Sản phẩm không tồn tại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi kết nối: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Hàm hiển thị dữ liệu lên giao diện
    private void displayData(Products product) {
        // Tên sản phẩm
        tvProductName.setText(product.getName());

        // Định dạng giá tiền
        tvProductPrice.setText(String.format("%,.0fđ", product.getSalePrice()));
        tvOldPrice.setText(String.format("%,.0fđ", product.getOriginalPrice()));

        // Mô tả
        if (product.getDescription() != null) {
            tvProductDescription.setText(product.getDescription());
        }

        // Load ảnh bằng Glide
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.lo_roche_posay) // Ảnh chờ
                    .error(R.drawable.lo_roche_posay)      // Ảnh lỗi
                    .into(imgProductLarge);
        }
    }

    // Hàm mở BottomSheet (Dùng chung cho cả Thêm giỏ hàng và Mua ngay)
    private void openVariantSheet(String actionType) {
        if (currentProduct != null) {
            VariantBottomSheet variantSheet = VariantBottomSheet.newInstance(
                    currentProduct.getName(),
                    quantity,
                    (long) currentProduct.getSalePrice(),
                    actionType // "add_to_cart" hoặc "buy_now"
            );
            variantSheet.show(getSupportFragmentManager(), "VariantBottomSheet");
        } else {
            Toast.makeText(this, "Đang tải dữ liệu sản phẩm, vui lòng đợi...", Toast.LENGTH_SHORT).show();
        }
    }
}