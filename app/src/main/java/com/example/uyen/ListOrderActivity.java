package com.example.uyen;

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
        tabAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListOrderActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        // Tab Đơn hàng (đã ở trang này nên không cần làm gì)
        tabOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đã ở trang đơn hàng, không cần điều hướng
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