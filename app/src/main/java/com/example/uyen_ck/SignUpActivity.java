package com.example.uyen_ck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uyen_ck.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private EditText etFullname;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etAddress;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private CheckBox cbTerms;
    private Button btnRegister;
    private TextView tvLoginLink;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Khởi tạo các view
        etFullname = findViewById(R.id.et_fullname_register);
        etEmail = findViewById(R.id.et_email_register);
        etPhone = findViewById(R.id.et_phone_register);
        etAddress = findViewById(R.id.et_address_register);
        etPassword = findViewById(R.id.et_password_register);
        etConfirmPassword = findViewById(R.id.et_confirm_password_register);
        cbTerms = findViewById(R.id.cb_terms);
        btnRegister = findViewById(R.id.btn_register);
        tvLoginLink = findViewById(R.id.tv_login_link);

        btnRegister.setOnClickListener(v -> handleSignUp());

        // Xử lý sự kiện nhấn vào "Đăng nhập ngay"
        tvLoginLink.setOnClickListener(v -> {
            finish(); // Quay lại màn hình đăng nhập
        });
    }

    private void handleSignUp() {
        String fullname = etFullname.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Kiểm tra validation
        if (fullname.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập họ và tên", Toast.LENGTH_SHORT).show();
            etFullname.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            etPhone.requestFocus();
            return;
        }

        if (address.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập địa chỉ", Toast.LENGTH_SHORT).show();
            etAddress.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            etPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            etConfirmPassword.requestFocus();
            return;
        }

        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Vui lòng đồng ý với điều khoản sử dụng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo tài khoản Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        saveUserToFirestore(uid, fullname, email, phone, address);
                    } else {
                        Toast.makeText(SignUpActivity.this,
                                "Lỗi đăng ký: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                        Log.e(TAG, "SignUp failed", task.getException());
                    }
                });
    }

    // Trong file SignUpActivity.java

    // Trong file SignUpActivity.java
    private void saveUserToFirestore(String uid, String fullname, String email, String phone, String address) {
        Map<String, Object> user = new HashMap<>();
        user.put("uid", uid);
        user.put("displayName", fullname);
        user.put("email", email);
        user.put("phone", phone);
        user.put("address", address);

        // THAY ĐỔI TẠI ĐÂY: Thiết lập mặc định là buyer
        user.put("role", "buyer");

        user.put("createdAt", System.currentTimeMillis());

        db.collection("users").document(uid)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SignUpActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                });
    }
}