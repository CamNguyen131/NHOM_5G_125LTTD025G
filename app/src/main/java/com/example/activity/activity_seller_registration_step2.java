package com.example.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent; // Cho Intent và startActivity
import android.view.View; // Cho View, OnClickListener
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast; // Cho Toast
// Thêm bất kỳ lớp nào bạn đang sử dụng mà bị báo lỗi.
// ...
public class activity_seller_registration_step2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Đảm bảo tên layout khớp với file XML của Bước 2
        setContentView(R.layout.activity_seller_registration_step2);

        // --- 1. Xử lý nút QUAY LẠI ở Footer (id: btn_back_bottom) ---
        Button btnBackBottom = findViewById(R.id.btn_back_bottom);
        btnBackBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Phương thức finish() sẽ đóng Activity hiện tại (Step 2)
                // và quay lại Activity trước đó (Step 1).
                finish();
            }
        });

        // --- 2. (Tùy chọn) Xử lý nút BACK ở Header (id: btn_back) ---
        ImageButton btnBackTop = findViewById(R.id.btn_back);
        btnBackTop.setOnClickListener(v -> finish()); // Sử dụng Lambda gọn hơn

        // --- 3. Xử lý nút HOÀN TẤT ĐĂNG KÝ (id: btn_complete) ---
        // (Đây là logic chuyển sang Seller Dashboard mà chúng ta đã làm trước đó)
        Button btnComplete = findViewById(R.id.btn_complete);
        btnComplete.setOnClickListener(v -> {
            Toast.makeText(this, "Đăng ký hoàn tất, chuyển màn hình...", Toast.LENGTH_SHORT).show();
            // Thêm Intent để chuyển sang SellerDashboardActivity tại đây.
        });
    }
}