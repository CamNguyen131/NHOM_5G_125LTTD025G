package com.example.uyen_ck;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Xử lý click vào cài đặt
        LinearLayout rowSetting = findViewById(R.id.rowSetting);
        rowSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        // Xử lý bottom navigation
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        LinearLayout tabHome = findViewById(R.id.tabHome);
        LinearLayout tabCart = findViewById(R.id.tabCart);
        LinearLayout tabOrder = findViewById(R.id.tabOrder);
        LinearLayout tabAccount = findViewById(R.id.tabAccount);

        // Tab Đơn hàng
        tabOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListOrderActivity.class);
                startActivity(intent);
            }
        });

        // Tab Tài khoản (đã ở trang này nên không cần làm gì)
        tabAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đã ở trang tài khoản, không cần điều hướng
            }
        });

        // Các tab khác có thể thêm tương tự
        tabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Điều hướng đến trang chủ
                // Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                // startActivity(intent);
            }
        });

        tabCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Điều hướng đến giỏ hàng
                // Intent intent = new Intent(MainActivity.this, CartActivity.class);
                // startActivity(intent);
            }
        });
    }
}