package com.example.uyen_ck;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private static final long SPLASH_DELAY_MS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            // Lấy trạng thái đăng nhập từ bộ nhớ máy
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

            if (isLoggedIn) {
                Log.i(TAG, "Đã đăng nhập: Chuyển đến ProductDetailActivity.");
                Intent intent = new Intent(SplashActivity.this, ProductDetailActivity.class);

                // MẸO: Thường ProductDetail cần ID sản phẩm. Nếu bạn muốn mở một sản phẩm mặc định:
                // intent.putExtra("PRODUCT_ID", 1);

                startActivity(intent);
            } else {
                Log.i(TAG, "Chưa đăng nhập: Chuyển đến LoginActivity.");
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish(); // Quan trọng: Đóng Splash để không quay lại được bằng nút Back
        }, SPLASH_DELAY_MS);
    }
}