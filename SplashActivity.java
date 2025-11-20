package com.example.beauty;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.mamadebeauty.R;

/**
 * Activity xử lý màn hình Splash. Hiển thị logo sau đó chuyển hướng.
 */
public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private static final long SPLASH_DELAY_MS = 2000; // 2 giây

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            // TODO: Thay thế logic giả lập này bằng kiểm tra đăng nhập thực tế (ví dụ: SharedPreferences)
            boolean isLoggedIn = false;

            if (isLoggedIn) {
                // Đã đăng nhập -> Chuyển đến Trang Chủ
                Log.i(TAG, "Chuyển đến HomeActivity.");
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            } else {
                // Chưa đăng nhập -> Chuyển đến Đăng Nhập
                Log.i(TAG, "Chuyển đến LoginActivity.");
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }

            // Kết thúc Splash Activity để người dùng không thể quay lại bằng nút Back
            finish();
        }, SPLASH_DELAY_MS);
    }
}