package com.example.uyen_ck;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;


public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private static final long SPLASH_DELAY_MS = 2000; // 2 giây

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            boolean isLoggedIn = false;

            if (isLoggedIn) {
                Log.i(TAG, "Chuyển đến HomeActivity.");
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            } else {
                Log.i(TAG, "Chuyển đến LoginActivity.");
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish();
        }, SPLASH_DELAY_MS);
    }
}