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

    // Trong loadProfileData
    private void loadProfileData(String userId) {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Firebase tự mapping mảng 'addresses' vào List<Address> trong model User
                        currentUser = documentSnapshot.toObject(User.class);

                        if (currentUser != null) {
                            etFullName.setText(currentUser.getDisplayName());
                            etPhone.setText(currentUser.getPhoneNumber());

                            // Lấy email trực tiếp từ Firestore (do model User của bạn không có field email)
                            String email = documentSnapshot.getString("email");
                            etEmail.setText(email);

                            // XỬ LÝ ADDRESS TỪ LIST
                            List<Address> addressList = currentUser.getAddresses();
                            if (addressList != null && !addressList.isEmpty()) {
                                Address displayAddr = addressList.get(0); // Mặc định lấy cái đầu tiên

                                for (Address addr : addressList) {
                                    if (addr.isDefault()) { // Tìm địa chỉ có default = true
                                        displayAddr = addr;
                                        break;
                                    }
                                }
                                // Đổ dữ liệu vào EditText
                                etAddress.setText(displayAddr.getAddressLine());
                            }

                            if (currentUser.getAvatarUrl() != null) {
                                Glide.with(this).load(currentUser.getAvatarUrl()).into(ivAvatar);
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void saveProfileData(String userId) {
        if (currentUser == null) return;

        String newName = etFullName.getText().toString().trim();
        String newAddressLine = etAddress.getText().toString().trim();

        // 1. Cập nhật thông tin cơ bản
        currentUser.setDisplayName(newName);

        // 2. Cập nhật vào List addresses (không tách riêng)
        List<Address> list = currentUser.getAddresses();
        if (list != null && !list.isEmpty()) {
            boolean updated = false;
            for (Address addr : list) {
                if (addr.isDefault()) {
                    addr.setAddressLine(newAddressLine);
                    updated = true;
                    break;
                }
            }
            // Nếu không có cái nào default, cập nhật vào cái đầu tiên
            if (!updated) {
                list.get(0).setAddressLine(newAddressLine);
            }
        }

        // 3. Lưu toàn bộ Object currentUser (bao gồm cả mảng addresses đã sửa)
        db.collection("users").document(userId)
                .set(currentUser)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi lưu: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
    // Trong saveProfileData

}