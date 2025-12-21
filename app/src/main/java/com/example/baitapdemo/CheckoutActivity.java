package com.example.baitapdemo;

import android.os.Bundle;
import android.widget.Button;
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
            Toast.makeText(this, "Đ24 thành công! Cảm ơn bạn đã mua hàng", Toast.LENGTH_LONG).show();
            CartManager.getInstance().clearCart();
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