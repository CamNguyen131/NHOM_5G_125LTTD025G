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

    private TextView tvQuantity, tvOldPrice;
    private Button btnAddCart, btnBuyNow;
    private ImageButton btnMinus, btnPlus;
    private int quantity = 1;
    private final long PRICE_PER_ITEM = 850000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initViews();
        setupEvents();
        strikeOldPrice();
    }

    private void initViews() {
        tvQuantity = findViewById(R.id.tvQuantity);
        tvOldPrice = findViewById(R.id.tvOldPrice);
        btnMinus   = findViewById(R.id.btnMinus);
        btnPlus    = findViewById(R.id.btnPlus);
        btnAddCart = findViewById(R.id.btnAddCart);
        btnBuyNow  = findViewById(R.id.btnBuyNow);
    }

    private void setupEvents() {
        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });

        btnPlus.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
        });

        btnAddCart.setOnClickListener(v -> {
            CartManager.getInstance().addItem("Serum Vitamin C", quantity, PRICE_PER_ITEM);
            Toast.makeText(this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
        });

        btnBuyNow.setOnClickListener(v -> {
            CartManager.getInstance().addItem("Serum Vitamin C", quantity, PRICE_PER_ITEM);
            startActivity(new Intent(this, CheckoutActivity.class));
        });
    }

    private void strikeOldPrice() {
        tvOldPrice.setPaintFlags(tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }
}