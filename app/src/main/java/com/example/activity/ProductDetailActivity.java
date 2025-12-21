package com.example.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView tvQuantity, tvOldPrice;
    private Button btnMinus, btnPlus, btnAddCart, btnBuyNow;
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

        // Giảm số lượng
        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });

        // Tăng số lượng
        btnPlus.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
        });

        // Khi bấm "Thêm vào giỏ" → mở Bottom Sheet
        btnAddCart.setOnClickListener(v -> openVariantBottomSheet("cart"));

        // Khi bấm "Đặt hàng ngay" → mở Bottom Sheet
        btnBuyNow.setOnClickListener(v -> openVariantBottomSheet("buy_now"));
    }

    /**
     * Hàm mở BottomSheet và truyền dữ liệu sang VariantBottomSheet
     */
    private void openVariantBottomSheet(String actionType) {
        VariantBottomSheet sheet = VariantBottomSheet.newInstance(
                "Serum Vitamin C", quantity, PRICE_PER_ITEM, actionType);

        sheet.show(getSupportFragmentManager(), "VariantBottomSheet");
    }

    private void strikeOldPrice() {
        tvOldPrice.setPaintFlags(tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }
}
