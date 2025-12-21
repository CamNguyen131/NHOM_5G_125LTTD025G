package com.example.baitapdemo;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CartActivity extends AppCompatActivity {

    private TextView tvSubtotal, tvTotal;
    private Button btnCheckout;
    private CheckBox cbSelectAll;
    private ImageButton btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initViews();
        setupEvents();
        updateCartSummary();
    }

    private void initViews() {
        tvSubtotal  = findViewById(R.id.txtTamTinh);
        tvTotal     = findViewById(R.id.txtTongTien);
        btnCheckout = findViewById(R.id.btnCheckout);
        cbSelectAll = findViewById(R.id.cbSelectAll);
        btnDelete   = findViewById(R.id.btnDelete);
    }

    private void setupEvents() {
        btnCheckout.setOnClickListener(v -> {
            if (CartManager.getInstance().getItemCount() == 0) {
                Toast.makeText(this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(this, CheckoutActivity.class));
        });

        btnDelete.setOnClickListener(v -> {
            CartManager.getInstance().clearCart();
            Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
            updateCartSummary();
        });

        cbSelectAll.setOnCheckedChangeListener((btn, isChecked) -> updateCartSummary());
    }

    private void updateCartSummary() {
        long total = CartManager.getInstance().getTotalPrice();
        int count = CartManager.getInstance().getItemCount();

        String price = count > 0 ? String.format("%,dđ", total) : "0đ";
        tvSubtotal.setText(price);
        tvTotal.setText(price);
        btnCheckout.setText("Thanh toán (" + count + " sản phẩm)");
        cbSelectAll.setChecked(count > 0);
        cbSelectAll.setText("Chọn tất cả (" + count + "/" + count + ")");
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartSummary();
    }
}