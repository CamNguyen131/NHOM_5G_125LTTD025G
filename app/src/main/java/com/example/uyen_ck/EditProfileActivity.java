package com.example.uyen_ck;

import android.os.Bundle;
import android.widget.Button; // Sửa lỗi Cannot resolve symbol Button
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.uyen_ck.models.Address;
import com.example.uyen_ck.models.User;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {

    // CHỈ KHAI BÁO 1 LẦN Ở ĐÂY
    private EditText etFullName, etEmail, etPhone, etAddress;
    private ImageView ivAvatar;
    private Button btnSave;
    private ImageButton btnBack;
    private FirebaseFirestore db;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initViews();
        db = FirebaseFirestore.getInstance();
        loadProfileData("user_01"); // ID mẫu trong hình ddf22c.png

        btnSave.setOnClickListener(v -> saveProfileData("user_01"));
        btnBack.setOnClickListener(v -> finish());
    }

    private void initViews() {
        etFullName = findViewById(R.id.full_name);
        etEmail = findViewById(R.id.email);
        etPhone = findViewById(R.id.phone);
        etAddress = findViewById(R.id.address);
        ivAvatar = findViewById(R.id.avatar);
        btnSave = findViewById(R.id.btn_save);
        btnBack = findViewById(R.id.btn_back);
    }

    private void loadProfileData(String userId) {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // SỬA LỖI: Chỉ gán, không dùng từ khóa 'User' ở đầu
                        currentUser = documentSnapshot.toObject(User.class);
                        if (currentUser != null) {
                            etFullName.setText(currentUser.getDisplayName());

                            List<Address> addresses = currentUser.getAddresses();
                            if (addresses != null && !addresses.isEmpty()) {
                                Address activeAddr = addresses.get(0);
                                for (Address a : addresses) {
                                    if (a.isDefault()) { activeAddr = a; break; }
                                }
                                etPhone.setText(activeAddr.getPhone());
                                etAddress.setText(activeAddr.getAddressLine());
                            }
                            if (currentUser.getAvatarUrl() != null) {
                                Glide.with(this).load(currentUser.getAvatarUrl()).into(ivAvatar);
                            }
                        }
                    }
                });
    }

    private void saveProfileData(String userId) {
        if (currentUser == null) return;
        currentUser.setDisplayName(etFullName.getText().toString().trim());
        // Cập nhật địa chỉ mặc định trong list
        db.collection("users").document(userId).set(currentUser)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Lưu thành công!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}