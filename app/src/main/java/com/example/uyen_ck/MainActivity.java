package com.example.uyen_ck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast; // Thêm import cho Toast nếu dùng

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- 1. Xử lý các hàng TÀI KHOẢN & HỖ TRỢ ---
        setupAccountRows();

        // --- 2. Xử lý nút ĐĂNG XUẤT ---
        setupLogoutButton();

        // --- 3. Xử lý Bottom Navigation ---
        setupBottomNavigation();
    }

    // Khối xử lý các hàng chức năng trong ScrollView
    private void setupAccountRows() {
        // Thông tin cá nhân
        LinearLayout rowInfo = findViewById(R.id.rowInfo);
        if (rowInfo != null) {
            rowInfo.setOnClickListener(v -> {
                Toast.makeText(MainActivity.this, "Chuyển đến Thông tin cá nhân", Toast.LENGTH_SHORT).show();
            });
        }

        LinearLayout btnSell = findViewById(R.id.btnSell);
        if (btnSell != null) {
            btnSell.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, activity_seller_registration.class);
                startActivity(intent);
            });
        }
        LinearLayout rowSetting = findViewById(R.id.rowSetting);
        if (rowSetting != null) {
            rowSetting.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            });
        }
    }

    private void setupLogoutButton() {
        Button btnLogout = findViewById(R.id.btnLogout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {

                Toast.makeText(MainActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void setupBottomNavigation() {
        LinearLayout tabHome = findViewById(R.id.tabHome);
        LinearLayout tabCart = findViewById(R.id.tabCart);
        LinearLayout tabOrder = findViewById(R.id.tabOrder);
        LinearLayout tabAccount = findViewById(R.id.tabAccount);

        // Tab Trang chủ -> Chuyển đến HomeActivity.class
        if (tabHome != null) {
            tabHome.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish(); // Đóng Activity hiện tại nếu muốn Home là màn hình chính
            });
        }

        // Tab Giỏ hàng -> Chuyển đến CartActivity.class
        if (tabCart != null) {
            tabCart.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
                finish();
            });
        }

        // Tab Đơn hàng -> Chuyển đến ListOrderActivity.class
        if (tabOrder != null) {
            tabOrder.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ListOrderActivity.class);
                startActivity(intent);
                finish();
            });
        }

        // Tab Tài khoản (đã ở trang này nên không cần làm gì)
        if (tabAccount != null) {
            tabAccount.setOnClickListener(v -> {
                // Không làm gì, đã ở MainActivity
            });
        }
    }
}