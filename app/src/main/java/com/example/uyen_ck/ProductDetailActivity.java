package com.example.uyen_ck;

import android.os.Bundle;
import android.view.View;
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

    // Khai báo các biến giao diện
    private ImageView imgProductLarge;
    private TextView tvProductName, tvProductPrice, tvOldPrice, tvProductDescription, tvQuantity;
    private ImageButton btnBack, btnMinus, btnPlus, btnChat, btnHeart;
    private Button btnAddCart, btnBuyNow;

    // Khai báo biến xử lý dữ liệu
    private FirebaseFirestore db;
    private String productId;
    private Products currentProduct; // Lưu thông tin sản phẩm đang xem
    private int quantity = 1; // Số lượng mặc định ban đầu là 1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // Ánh xạ View
        initViews();

        // 1. Nhận ID sản phẩm từ Intent (Do trang ProductListActivity gửi sang)
        if (getIntent() != null) {
            productId = getIntent().getStringExtra("product_id");
        }

        // 2. Kiểm tra ID và tải dữ liệu
        if (productId != null && !productId.isEmpty()) {
            loadProductDetail(productId);
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy ID sản phẩm!", Toast.LENGTH_SHORT).show();
            finish(); // Đóng Activity nếu không có ID
        }

        // 3. Cài đặt sự kiện click
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

    // Hàm tải chi tiết sản phẩm từ Firestore
    private void loadProductDetail(String id) {
        db.collection("products").document(id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Chuyển đổi dữ liệu Firestore sang object Products
                        currentProduct = documentSnapshot.toObject(Products.class);

                        if (currentProduct != null) {
                            // Quan trọng: Gán lại ID từ documentSnapshot để đảm bảo chính xác
                            currentProduct.setProductId(documentSnapshot.getId());

                            // Hiển thị dữ liệu lên màn hình
                            displayData(currentProduct);
                        }
                    } else {
                        Toast.makeText(this, "Sản phẩm không tồn tại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Hàm hiển thị dữ liệu lên giao diện
    private void displayData(Products product) {
        tvProductName.setText(product.getName());

        // Định dạng giá tiền: 100000 -> 100.000đ
        tvProductPrice.setText(String.format("%,.0fđ", product.getSalePrice()));
        tvOldPrice.setText(String.format("%,.0fđ", product.getOriginalPrice()));

        if (product.getDescription() != null) {
            tvProductDescription.setText(product.getDescription());
        }

        // Tải ảnh bằng Glide
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.lo_roche_posay) // Ảnh chờ (nhớ thay bằng ảnh của bạn)
                    .error(R.drawable.lo_roche_posay)      // Ảnh lỗi
                    .into(imgProductLarge);
        }
    }

    private void setupEvents() {
        // Nút Quay lại
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

        // --- XỬ LÝ SỰ KIỆN: THÊM VÀO GIỎ HÀNG ---
        btnAddCart.setOnClickListener(v -> {
            if (currentProduct != null) {
                // Gọi VariantBottomSheet để người dùng chọn Phân loại (nếu có) và xác nhận thêm
                VariantBottomSheet variantSheet = VariantBottomSheet.newInstance(
                        currentProduct.getProductId(),    // ID sản phẩm
                        currentProduct.getName(),         // Tên
                        currentProduct.getImageUrl(),     // Ảnh
                        quantity,                         // Số lượng đang chọn
                        (long) currentProduct.getSalePrice(), // Giá bán
                        "add_to_cart"                     // Hành động: Thêm giỏ
                );
                variantSheet.show(getSupportFragmentManager(), "VariantBottomSheet");
            }
        });

        // --- XỬ LÝ SỰ KIỆN: MUA NGAY ---
        btnBuyNow.setOnClickListener(v -> {
            if (currentProduct != null) {
                VariantBottomSheet variantSheet = VariantBottomSheet.newInstance(
                        currentProduct.getProductId(),
                        currentProduct.getName(),
                        currentProduct.getImageUrl(),
                        quantity,
                        (long) currentProduct.getSalePrice(),
                        "buy_now"                        // Hành động: Mua ngay
                );
                variantSheet.show(getSupportFragmentManager(), "VariantBottomSheet");
            }
        });

        // Nút Chat (Tính năng mở rộng)
        if (btnChat != null) {
            btnChat.setOnClickListener(v -> {
                Toast.makeText(this, "Tính năng Chat đang phát triển", Toast.LENGTH_SHORT).show();
                // Intent intent = new Intent(this, ChatDetailActivity.class);
                // startActivity(intent);
            });
        }
    }
}