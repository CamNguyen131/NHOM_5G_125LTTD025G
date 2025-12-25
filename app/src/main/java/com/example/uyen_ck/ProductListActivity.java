package com.example.uyen_ck;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
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

        // 2. Click vào từng sản phẩm
        setupProductClick(R.id.product1);
        setupProductClick(R.id.product2);
        setupProductClick(R.id.product3);
        setupProductClick(R.id.product4);

        setupBottomNavigation();
    }

    private void setupProductClick(int id) {
        LinearLayout product = findViewById(id);
        if (product != null) {
            product.setOnClickListener(v -> {
                startActivity(new Intent(this, AddProductActivity.class));
            });
        }
    }

    private void setupBottomNavigation() {
        LinearLayout tabOverview = findViewById(R.id.tab_overview);
        LinearLayout tabProducts = findViewById(R.id.tab_products);
        LinearLayout tabMessages = findViewById(R.id.tab_messages);
        LinearLayout tabProfile = findViewById(R.id.tab_profile);

        if (tabOverview != null) {
            tabOverview.setOnClickListener(v -> startActivity(new Intent(this, HomeActivity.class)));
        }

        if (tabProducts != null) {
            tabProducts.setOnClickListener(v -> {
                // Already on ProductListActivity, no action needed
            });
        }

        if (tabMessages != null) {
            tabMessages.setOnClickListener(v -> startActivity(new Intent(this, MessageListActivity.class)));
        }

        if (tabProfile != null) {
            tabProfile.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        }
    }
}
