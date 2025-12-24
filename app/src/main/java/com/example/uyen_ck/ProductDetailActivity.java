package com.example.uyen_ck;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.uyen_ck.models.Products;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivProductImage;
    private TextView tvProductName, tvProductPrice, tvDescription;
    private ImageButton btnBack;
    private FirebaseFirestore db;
    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // 1. Ánh xạ View
        ivProductImage = findViewById(R.id.ivProductImage);
        tvProductName = findViewById(R.id.tvProductName);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        tvDescription = findViewById(R.id.tvDescription);
        btnBack = findViewById(R.id.btnBack);

        // 2. Khởi tạo Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // 3. Nhận productId được gửi từ HomeActivity/ProductListActivity
        productId = getIntent().getStringExtra("product_id");

        if (productId != null) {
            loadProductDetail(productId);
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy ID sản phẩm!", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Nút quay lại màn hình trước
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadProductDetail(String id) {
        // Truy vấn Firestore vào collection "products" theo document ID
        db.collection("products").document(id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Chuyển đổi dữ liệu từ Firestore sang Object Products
                        Products product = documentSnapshot.toObject(Products.class);
                        if (product != null) {
                            displayData(product);
                        }
                    } else {
                        Toast.makeText(this, "Sản phẩm không tồn tại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void displayData(Products product) {
        tvProductName.setText(product.getName());
        // Định dạng giá tiền (Ví dụ: 450000 -> 450.000đ)
        tvProductPrice.setText(String.format("%,.0fđ", product.getOriginalPrice()));

        // Kiểm tra nếu có mô tả thì hiển thị, không thì để mặc định
        if (product.getDescription() != null && !product.getDescription().isEmpty()) {
            tvDescription.setText(product.getDescription());
        }

        // Sử dụng Glide để tải ảnh từ URL lưu trên Firebase
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.placeholder_product)
                    .error(R.drawable.lo_roche_posay) // Ảnh lỗi mặc định
                    .into(ivProductImage);
        }
    }
}