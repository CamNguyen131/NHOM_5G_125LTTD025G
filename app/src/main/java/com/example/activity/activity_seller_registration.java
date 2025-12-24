package com.example.activity;
import androidx.appcompat.app.AppCompatActivity; // Cho AppCompatActivity
import android.os.Bundle; // Cho Bundle và onCreate
import android.content.Intent; // Cho Intent và startActivity (Chỉ cần cho Step 1)
import android.widget.Button; // Cho Button
import android.widget.ImageButton; // Cho ImageButton
public class activity_seller_registration extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sửa lỗi ở đây: Thay tên layout không tồn tại
        // bằng tên layout chứa các ID bạn đang tìm kiếm (ví dụ: activity_register_step1)
        setContentView(R.layout.activity_seller_registration);

        Button btnContinue = findViewById(R.id.btn_continue);
        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(activity_seller_registration.this, activity_seller_registration_step2.class);
            startActivity(intent);
        });
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
    }
}