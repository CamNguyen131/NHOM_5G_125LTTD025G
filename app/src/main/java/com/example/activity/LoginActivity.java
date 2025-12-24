package com.example.activity;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Activity xử lý việc Đăng nhập/Đăng ký của người dùng.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate: LoginActivity đã khởi tạo.");

        // Ánh xạ các thành phần UI
        etUsername = findViewById(R.id.et_email_login);
        etPassword = findViewById(R.id.et_password_login);
        btnLogin = findViewById(R.id.btn_login);

        // Thiết lập sự kiện cho nút Đăng Nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
    }

    /**
     * Xử lý quá trình đăng nhập của người dùng.
     */
    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ Tên đăng nhập và Mật khẩu.", Toast.LENGTH_LONG).show();
            return;
        }

        // TODO: Thay thế logic xác thực giả lập này bằng Firebase Auth, API call, hoặc SQLite
        if (username.equals("admin") && password.equals("123456")) {
            // Xác thực thành công
            Log.i(TAG, "Đăng nhập thành công cho người dùng: " + username);
            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

            // Chuyển hướng đến Trang Chủ
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            // Xóa Login khỏi stack để người dùng không thể quay lại bằng nút Back
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        } else {
            // Xác thực thất bại
            Log.w(TAG, "Đăng nhập thất bại: Sai tên đăng nhập hoặc mật khẩu.");
            Toast.makeText(this, "Tên đăng nhập hoặc Mật khẩu không đúng. Vui lòng thử lại.", Toast.LENGTH_LONG).show();
        }
    }
}