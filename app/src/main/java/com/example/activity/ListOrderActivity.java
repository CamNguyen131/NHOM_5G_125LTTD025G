package com.example.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class ListOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_order);

        // Xử lý click vào đơn hàng son kem lì MAC
        LinearLayout cardMacMatte = findViewById(R.id.cardMacMatte); // Cần thêm id cho card này
        cardMacMatte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListOrderActivity.this, OrderDetailActivity.class);
                startActivity(intent);
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
        if (tabAccount != null) {
            tabAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ListOrderActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
        }

        // Tab Đơn hàng
        if (tabOrder != null) {
            tabOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Đã ở trang đơn hàng, không cần điều hướng
                }
            });
        }

        // Tab Trang chủ
        if (tabHome != null) {
            tabHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ListOrderActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
        }
        //Tab Giỏ hàng
        if (tabCart != null) {
            tabCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ListOrderActivity.this, CartActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
        }
    }
}