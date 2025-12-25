package com.example.uyen_ck;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView tvQuantity, tvOldPrice, tvProductDescription, btnReadMore;
    private TextView tvProductName;
    private Button btnMinus, btnPlus, btnAddCart, btnBuyNow;
    private ImageButton btnBack, btnHeart, btnChat;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initViews();
        setupEvents();
        applyOldPriceStrike();
        setupReadMoreToggle();
        loadDataFromHome();
    }

    private void initViews() {
        // Các view có thật trong layout của bạn
        tvQuantity = findViewById(R.id.tvQuantity);
        tvOldPrice = findViewById(R.id.tvOldPrice);
        tvProductDescription = findViewById(R.id.tvProductDescription);
        btnReadMore = findViewById(R.id.btnReadMore);

        tvProductName = findViewById(R.id.tvProductName); // <--- ĐÃ THÊM: Tìm TextView tên sản phẩm

        btnAddCart = findViewById(R.id.btnAddCart);
        btnBuyNow  = findViewById(R.id.btnBuyNow);

        btnBack  = findViewById(R.id.btnBack);
        btnHeart = findViewById(R.id.btnHeart);
        btnChat  = findViewById(R.id.btnChat);

        // Số lượng ban đầu
        if (tvQuantity != null) tvQuantity.setText("1");
    }

    private void setupEvents() {
        // Back
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        // Yêu thích
        if (btnHeart != null) btnHeart.setOnClickListener(v ->
                Toast.makeText(this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show());

        // Chat
        if (btnChat != null) btnChat.setOnClickListener(v ->
                Toast.makeText(this, "Mở trò chuyện với shop...", Toast.LENGTH_SHORT).show());

        // Giảm số lượng
        if (btnMinus != null) btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });

        // Tăng số lượng
        if (btnPlus != null) btnPlus.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
        });

        // Thêm vào giỏ
        if (btnAddCart != null) btnAddCart.setOnClickListener(v ->
                Toast.makeText(this, "Đã thêm " + quantity + " sản phẩm vào giỏ hàng!", Toast.LENGTH_LONG).show());

        // Mua ngay
        if (btnBuyNow != null) btnBuyNow.setOnClickListener(v -> {
            Toast.makeText(this, "Chuyển tới thanh toán " + quantity + " sản phẩm...", Toast.LENGTH_LONG).show();
            // Sau này chuyển sang CheckoutActivity
        });
    }

    // Gạch ngang giá cũ
    private void applyOldPriceStrike() {
        if (tvOldPrice != null) {
            tvOldPrice.setPaintFlags(tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    // Xem thêm / Thu gọn mô tả
    private void setupReadMoreToggle() {
        if (btnReadMore == null || tvProductDescription == null) return;

        btnReadMore.setOnClickListener(v -> {
            if (tvProductDescription.getMaxLines() == 3) {
                tvProductDescription.setMaxLines(100);
                btnReadMore.setText("Thu gọn");
            } else {
                tvProductDescription.setMaxLines(3);
                btnReadMore.setText("Xem thêm");
            }
        });
    }

    // <--- PHƯƠNG THỨC ĐÃ SỬA: Gán tên sản phẩm nhận được từ Intent
    private void loadDataFromHome() {
        String productName = getIntent().getStringExtra("product_name");

        if (productName != null && !productName.isEmpty() && tvProductName != null) {
            tvProductName.setText(productName);
        }
    }
}