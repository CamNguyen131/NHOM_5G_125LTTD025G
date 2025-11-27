package com.example.uyen_ck;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
public class OrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // Xử lý nút back (mũi tên)
        ImageView backButton = findViewById(R.id.back_button); // Cần thêm id cho ImageView back
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Quay lại activity trước đó (ListOrderActivity)
            }
        });

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        LinearLayout tabHome = findViewById(R.id.tabHome);
        LinearLayout tabCart = findViewById(R.id.tabCart);
        LinearLayout tabOrder = findViewById(R.id.tabOrder);
        LinearLayout tabAccount = findViewById(R.id.tabAccount);

        // Tab Tài khoản
        tabAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        // Tab Đơn hàng
        tabOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this, ListOrderActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        // Các tab khác
        tabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Điều hướng đến trang chủ
            }
        });

        tabCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Điều hướng đến giỏ hàng
            }
        });
    }
}