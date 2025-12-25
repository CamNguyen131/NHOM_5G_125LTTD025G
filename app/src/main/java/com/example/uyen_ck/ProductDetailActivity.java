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
    private Products currentProduct; // Lưu thông tin sản phẩm hiện tại
    private int quantity = 1; // Số lượng mặc định

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        db = FirebaseFirestore.getInstance();
        initViews();

        // 1. Nhận ID sản phẩm từ Intent (Từ trang danh sách gửi sang)
        // Lưu ý: Bên ProductListActivity phải gửi key là "product_id"
        if (getIntent() != null) {
            productId = getIntent().getStringExtra("product_id");
        }

        if (productId != null && !productId.isEmpty()) {
            loadProductDetail(productId);
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy ID sản phẩm", Toast.LENGTH_SHORT).show();
            finish(); // Đóng trang nếu không có ID
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
        btnChat = findViewById(R.id.btnChat);     // Nếu có nút chat
        btnHeart = findViewById(R.id.btnHeart);   // Nếu có nút yêu thích

        btnAddCart = findViewById(R.id.btnAddCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);
    }

    // Tải dữ liệu từ Firestore dựa trên productId
    private void loadProductDetail(String id) {
        db.collection("products").document(id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        currentProduct = documentSnapshot.toObject(Products.class);
                        if (currentProduct != null) {
                            // Quan trọng: Gán lại ID vì toObject đôi khi không tự lấy ID của document
                            currentProduct.setProductId(documentSnapshot.getId());
                            displayData(currentProduct);
                        }
                    } else {
                        Toast.makeText(this, "Sản phẩm không còn tồn tại", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi kết nối: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Hiển thị dữ liệu lên giao diện
    private void displayData(Products product) {
        tvProductName.setText(product.getName());

        // Format giá tiền: 100000 -> 100,000đ
        tvProductPrice.setText(String.format("%,.0fđ", product.getSalePrice()));
        tvOldPrice.setText(String.format("%,.0fđ", product.getOriginalPrice()));

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

        // Nút THÊM VÀO GIỎ HÀNG
        btnAddCart.setOnClickListener(v -> {
            if (currentProduct != null) {
                // Gọi VariantBottomSheet với đầy đủ tham số (ID, Tên, Ảnh, SL, Giá, Loại hành động)
                VariantBottomSheet variantSheet = VariantBottomSheet.newInstance(
                        currentProduct.getProductId(),  // ID (Mới thêm)
                        currentProduct.getName(),       // Tên
                        currentProduct.getImageUrl(),   // Ảnh (Mới thêm)
                        quantity,                       // Số lượng
                        (long) currentProduct.getSalePrice(), // Giá
                        "add_to_cart"                   // Hành động
                );
                variantSheet.show(getSupportFragmentManager(), "VariantBottomSheet");
            }
        });

        // Nút MUA NGAY
        btnBuyNow.setOnClickListener(v -> {
            if (currentProduct != null) {
                VariantBottomSheet variantSheet = VariantBottomSheet.newInstance(
                        currentProduct.getProductId(),
                        currentProduct.getName(),
                        currentProduct.getImageUrl(),
                        quantity,
                        (long) currentProduct.getSalePrice(),
                        "buy_now"
                );
                variantSheet.show(getSupportFragmentManager(), "VariantBottomSheet");
            }
        });
    }
}