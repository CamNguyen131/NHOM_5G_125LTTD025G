package com.example.baitapdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

/**
 * Activity cho màn hình Trang Chủ (Home).
 * Nơi hiển thị danh sách sản phẩm, thanh điều hướng, v.v.
 */
public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private Button btnLogout;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: HomeActivity đã khởi tạo.");

        // Khởi tạo các View
        btnLogout = findViewById(R.id.btn_logout);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        // Thiết lập Bottom Navigation
        setupBottomNavigation();

        // Thiết lập sự kiện cho nút Đăng Xuất
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogout();
            }
        });

        // TODO: Viết logic để tải và hiển thị danh sách sản phẩm
        loadProductList();
    }

    /**
     * Thiết lập Bottom Navigation Bar
     */
    private void setupBottomNavigation() {
        // Đánh dấu tab "Trang chủ" là đang được chọn
        bottomNavigationView.setSelectedItemId(R.id.tabHome);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.tabHome) {
                    // Đã ở trang Home, không làm gì
                    return true;
                } else if (itemId == R.id.tabCart) {
                    // Chuyển đến CartActivity
                    startActivity(new Intent(HomeActivity.this, CartActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (itemId == R.id.tabOrder) {
                    // Chuyển đến ListOrderActivity
                    startActivity(new Intent(HomeActivity.this, ListOrderActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                } else if (itemId == R.id.rv_categories) {
                    // Chuyển đến MainActivity (Tài khoản)
                    startActivity(new Intent(HomeActivity.this, MainActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Giả lập việc tải dữ liệu sản phẩm.
     */
    private void loadProductList() {
        Log.d(TAG, "Đang tải danh sách sản phẩm...");
        Toast.makeText(this, "Trang chủ sẵn sàng. Tải dữ liệu thành công.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Xử lý quá trình đăng xuất.
     */
    private void handleLogout() {
        Log.i(TAG, "Đang đăng xuất người dùng...");
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Toast.makeText(this, "Đã đăng xuất.", Toast.LENGTH_SHORT).show();
    }
}