package com.example.uyen_ck;// Thêm các import cần thiết
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class activity_seller_registration_step2 extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration_step2);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String storeName = getIntent().getStringExtra("storeName");
        String storePhone = getIntent().getStringExtra("storePhone");

        Button btnComplete = findViewById(R.id.btn_complete);
        btnComplete.setOnClickListener(v -> {
            String address = ((EditText)findViewById(R.id.store_address)).getText().toString().trim();
            String desc = ((EditText)findViewById(R.id.store_description)).getText().toString().trim();

            // CẬP NHẬT LOGIC KIỂM TRA TẠI ĐÂY
            if (address.length() < 5) {
                Toast.makeText(this, "Địa chỉ quá ngắn (tối thiểu 5 ký tự)", Toast.LENGTH_SHORT).show();
                return;
            }

            if (desc.length() < 15) {
                Toast.makeText(this, "Mô tả cần chi tiết hơn (tối thiểu 15 ký tự)", Toast.LENGTH_SHORT).show();
                return;
            }

            // Nếu vượt qua kiểm tra thì tiến hành cập nhật
            updateUserToSeller(storeName, storePhone, address, desc);
        });

        // Logic nút quay lại
        findViewById(R.id.btn_back_bottom).setOnClickListener(v -> finish());
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    private void updateUserToSeller(String name, String phone, String addr, String desc) {
        String uid = mAuth.getCurrentUser().getUid();

        // Chuẩn bị dữ liệu cập nhật
        Map<String, Object> sellerData = new HashMap<>();
        sellerData.put("role", "seller"); // Cập nhật role
        sellerData.put("storeName", name);
        sellerData.put("storePhone", phone);
        sellerData.put("storeAddress", addr);
        sellerData.put("storeDescription", desc);

        // Thực hiện cập nhật trong Firestore
        db.collection("users").document(uid)
                .update(sellerData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Chúc mừng! Bạn đã trở thành người bán", Toast.LENGTH_LONG).show();

                    // Chuyển đến Dashboard
                    Intent intent = new Intent(this, activity_seller_dashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}