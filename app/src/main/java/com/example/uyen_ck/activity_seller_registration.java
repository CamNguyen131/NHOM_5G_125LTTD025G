package com.example.uyen_ck;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class activity_seller_registration extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        // --- BỔ SUNG DÒNG NÀY ---
        Button btnContinue = findViewById(R.id.btnContinue);
        // -----------------------

        btnContinue.setOnClickListener(v -> {
            String name = ((EditText)findViewById(R.id.store_name)).getText().toString().trim();
            String phone = ((EditText)findViewById(R.id.phone)).getText().toString().trim();

            if(name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(activity_seller_registration.this, activity_seller_registration_step2.class);
            intent.putExtra("storeName", name);
            intent.putExtra("storePhone", phone);
            startActivity(intent);
        });

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
    }
}