package com.example.activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddProductActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product); // <--- Cần file XML này

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (findViewById(R.id.btnSave) != null) {
            findViewById(R.id.btnSave).setOnClickListener(v -> {
                Toast.makeText(this, "Đã lưu sản phẩm!", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }
}