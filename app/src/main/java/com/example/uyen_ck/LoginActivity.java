package com.example.uyen_ck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uyen_ck.models.User; // Import model User
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore; // Thêm thư viện Firestore

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db; // Khai báo Firestore

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance(); // Khởi tạo Firestore

        etUsername = findViewById(R.id.et_email_login);
        etPassword = findViewById(R.id.et_password_login);
        btnLogin = findViewById(R.id.btn_login);

        // Tìm TextView "Đăng ký ngay"
        TextView tvRegisterLink = findViewById(R.id.tv_register_link);

        btnLogin.setOnClickListener(v -> handleLogin());

        // Thiết lập sự kiện chuyển sang trang Đăng ký
        if (tvRegisterLink != null) {
            tvRegisterLink.setOnClickListener(v -> {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            });
            }
    }

    private void handleLogin() {
        String email = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ Email và Mật khẩu.", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid(); // Lấy UID sau khi đăng nhập thành công
                        fetchUserData(uid); // Gọi hàm lấy dữ liệu người dùng
                    } else {
                        Toast.makeText(LoginActivity.this, "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Hàm lấy dữ liệu User và Addresses từ Firestore
    private void fetchUserData(String uid) {
        db.collection("users").document(uid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Map dữ liệu Firestore vào model User
                            User user = document.toObject(User.class);

                            if (user != null) {
                                Log.d(TAG, "User Data: " + user.getDisplayName());
                                // Bạn có thể lưu thông tin User vào Session/Global variable tại đây

                                Toast.makeText(LoginActivity.this, "Chào mừng " + user.getDisplayName(), Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Log.d(TAG, "Không tìm thấy dữ liệu người dùng trong Firestore.");
                            // Chuyển hướng nếu không có profile nhưng đã đăng nhập Auth thành công
                            startActivity(new Intent(LoginActivity.this, ProductDetailActivity.class));
                            finish();
                        }
                    } else {
                        Log.e(TAG, "Lỗi lấy Firestore: ", task.getException());
                    }
                });
    }
}