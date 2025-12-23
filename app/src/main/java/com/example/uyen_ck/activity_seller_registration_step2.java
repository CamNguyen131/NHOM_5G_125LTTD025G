package com.example.uyen_ck;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class activity_seller_registration_step2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration_step2);

        Button btnBackBottom = findViewById(R.id.btn_back_bottom);
        if (btnBackBottom != null) {
            btnBackBottom.setOnClickListener(v -> finish());
        }
        ImageButton btnBackTop = findViewById(R.id.btn_back);
        if (btnBackTop != null) {
            btnBackTop.setOnClickListener(v -> finish());
        }
        Button btnComplete = findViewById(R.id.btn_complete);
        if (btnComplete != null) {
            btnComplete.setOnClickListener(v -> {
                Toast.makeText(this, "Đăng ký thành công! Chào mừng bạn đến với gian hàng", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(activity_seller_registration_step2.this, activity_seller_dashboard.class);
                  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                finish();
            });
        }
    }
}