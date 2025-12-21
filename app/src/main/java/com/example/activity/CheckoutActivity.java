package com.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CheckoutActivity extends AppCompatActivity {

    private TextView tvSubtotal, tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        tvSubtotal = findViewById(R.id.txtSubtotal);
        tvTotal    = findViewById(R.id.txtTotalAmount);

        updateTotal();

        findViewById(R.id.btnOrder).setOnClickListener(v -> {

            // Thông báo
            Toast.makeText(this, "Đặt hàng thành công! Cảm ơn bạn đã mua hàng", Toast.LENGTH_LONG).show();

            // Xóa giỏ hàng
            CartManager.getInstance().clearCart();

            // Chuyển sang trang ListOrderActivity
            Intent intent = new Intent(CheckoutActivity.this, ListOrderActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            // Kết thúc trang checkout để không quay lại
            finish();
        });

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    private void updateTotal() {
        long total = CartManager.getInstance().getTotalPrice();
        String formatted = String.format("%,dđ", total);
        tvSubtotal.setText(formatted);
        tvTotal.setText(formatted);
    }
}
