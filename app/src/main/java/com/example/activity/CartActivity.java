package com.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CartActivity extends AppCompatActivity {

    private CheckBox cbSelectAll, cbSelectItem;
    private ImageButton btnDelete, btnPlus, btnMinus;
    private TextView tvQuantity, tvSubtotal, tvTotal;
    private Button btnCheckout;

    private int quantity = 1;
    private long price = 850000; // Giá cố định theo XML bạn đưa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initViews();
        setupEvents();
        updateCartUI();
    }

    private void initViews() {
        cbSelectAll = findViewById(R.id.cbSelectAll);
        cbSelectItem = findViewById(R.id.cbSelectItem);
        btnDelete = findViewById(R.id.btnDelete);
        btnPlus = findViewById(R.id.btnPlus);
        btnMinus = findViewById(R.id.btnMinus);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvSubtotal = findViewById(R.id.txtTamTinh);
        tvTotal = findViewById(R.id.txtTongTien);
        btnCheckout = findViewById(R.id.btnCheckout);
    }

    private void setupEvents() {

        // Chọn tất cả
        cbSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cbSelectItem.setChecked(isChecked);
            updateCartUI();
        });

        // Chọn từng item
        cbSelectItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cbSelectAll.setChecked(isChecked);
            updateCartUI();
        });

        // Tăng số lượng
        btnPlus.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
            updateCartUI();
        });

        // Giảm số lượng
        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
                updateCartUI();
            }
        });

        // Xóa sản phẩm
        btnDelete.setOnClickListener(v -> {
            quantity = 0;
            tvQuantity.setText("0");
            cbSelectItem.setChecked(false);
            cbSelectAll.setChecked(false);
            updateCartUI();
            Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
        });

        // Thanh toán
        btnCheckout.setOnClickListener(v -> {
            if (quantity == 0 || !cbSelectItem.isChecked()) {
                Toast.makeText(this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(this, CheckoutActivity.class));
        });
    }

    private void updateCartUI() {
        long total = quantity * price;

        if (!cbSelectItem.isChecked()) {
            tvSubtotal.setText("0đ");
            tvTotal.setText("0đ");
            btnCheckout.setText("Thanh toán (0 sản phẩm)");
        } else {
            tvSubtotal.setText(String.format("%,dđ", total));
            tvTotal.setText(String.format("%,dđ", total));
            btnCheckout.setText("Thanh toán (" + quantity + " sản phẩm)");
        }

        cbSelectAll.setText("Chọn tất cả (" + (cbSelectItem.isChecked() ? 1 : 0) + "/1)");
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartUI();
    }
}
