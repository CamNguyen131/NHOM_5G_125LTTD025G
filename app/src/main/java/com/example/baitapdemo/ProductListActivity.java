package com.example.baitapdemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProductListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        // 1. Nút Thêm mới
        Button btnAdd = findViewById(R.id.btnAdd);
        if (btnAdd != null) {
            btnAdd.setOnClickListener(v -> startActivity(new Intent(this, AddProductActivity.class)));
        }

        // 2. Nút Tin nhắn
        TextView navMessage = findViewById(R.id.navMessage);
        if (navMessage != null) {
            navMessage.setOnClickListener(v -> startActivity(new Intent(this, MessageListActivity.class)));
        }

        // 3. Click vào từng sản phẩm
        setupProductClick(R.id.product1);
        setupProductClick(R.id.product2);
        setupProductClick(R.id.product3);
        setupProductClick(R.id.product4);
    }

    private void setupProductClick(int id) {
        LinearLayout product = findViewById(id);
        if (product != null) {
            product.setOnClickListener(v -> {
                // Chuyển sang trang AddProductActivity để sửa (demo)
                startActivity(new Intent(this, AddProductActivity.class));
            });
        }
    }
}
