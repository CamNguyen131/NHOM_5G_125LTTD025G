package com.example.uyen_ck;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    // Khai báo các view
    private TextView tvName, tvEmail, tvAvatar, tvOrderCount, tvProductCount, tvTotalAmount;
    private TextView labelOrder, labelProduct, labelAmount;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            // 1. ĐẢM BẢO FIREBASE ĐƯỢC KHỞI TẠO
            FirebaseApp.initializeApp(this);

            // 2. Khởi tạo Firebase
            db = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();

            Log.d(TAG, "Firebase initialized. mAuth: " + (mAuth != null));

            // 3. Kiểm tra mAuth có null không
            if (mAuth == null) {
                Log.e(TAG, "FirebaseAuth is null! Initializing again...");
                mAuth = FirebaseAuth.getInstance();
            }

        } catch (Exception e) {
            Log.e(TAG, "Error initializing Firebase: " + e.getMessage(), e);
            Toast.makeText(this, "Lỗi khởi tạo ứng dụng: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // 4. Ánh xạ view từ layout XML
        initViews();

        // 5. Tải dữ liệu
        loadUserData();

        // 6. Thiết lập các sự kiện click
        setupAccountRows();
        setupLogoutButton();
        setupBottomNavigation();
    }

    private void initViews() {
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvAvatar = findViewById(R.id.tvAvatar);
        tvOrderCount = findViewById(R.id.tvOrderCount);
        tvProductCount = findViewById(R.id.tvProductCount);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);

        labelOrder = findViewById(R.id.labelOrder);
        labelProduct = findViewById(R.id.labelProduct);
        labelAmount = findViewById(R.id.labelAmount);
    }

    private void loadUserData() {
        try {
            // KIỂM TRA mAuth CÓ NULL KHÔNG
            if (mAuth == null) {
                Log.e(TAG, "mAuth is null in loadUserData");
                Toast.makeText(this, "Lỗi xác thực. Vui lòng khởi động lại ứng dụng.",
                        Toast.LENGTH_LONG).show();
                return;
            }

            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser == null) {
                Log.e(TAG, "Người dùng chưa đăng nhập");
                Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return;
            }

            String uid = currentUser.getUid();
            Log.d(TAG, "Đang tải dữ liệu cho UID: " + uid);

            // Lấy thông tin User từ Firestore collection "users"
            db.collection("users").document(uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Log.d(TAG, "Document user tồn tại: " + documentSnapshot.getData());

                            // LẤY DỮ LIỆU - QUAN TRỌNG: dùng đúng tên field như đã lưu
                            String name = documentSnapshot.getString("displayName");
                            String email = documentSnapshot.getString("email");
                            String role = documentSnapshot.getString("role"); // "customer" hoặc "seller"

                            // Debug: In ra giá trị
                            Log.d(TAG, "displayName: " + name);
                            Log.d(TAG, "email: " + email);
                            Log.d(TAG, "role: " + role);

                            // Nếu không có email trong Firestore, lấy từ Firebase Auth
                            if (email == null || email.isEmpty()) {
                                email = currentUser.getEmail();
                                Log.d(TAG, "Lấy email từ Auth: " + email);
                            }

                            // Nếu không có role, mặc định là "customer"
                            if (role == null || role.isEmpty()) {
                                role = "customer";
                            }

                            // HIỂN THỊ LÊN GIAO DIỆN
                            tvName.setText(name != null && !name.isEmpty() ? name : "Người dùng");
                            tvEmail.setText(email != null && !email.isEmpty() ? email : "Chưa có email");

                            // Hiển thị chữ cái đầu trên Avatar
                            if (name != null && !name.isEmpty()) {
                                tvAvatar.setText(name.substring(0, 1).toUpperCase());
                                Log.d(TAG, "Avatar text: " + name.substring(0, 1).toUpperCase());
                            } else {
                                // Nếu không có name, lấy từ email
                                if (email != null && !email.isEmpty()) {
                                    tvAvatar.setText(email.substring(0, 1).toUpperCase());
                                    Log.d(TAG, "Avatar từ email: " + email.substring(0, 1).toUpperCase());
                                }
                            }

                            // Cập nhật nhãn (Labels) tùy theo vai trò
                            updateLabels(role);

                            // Tải thống kê số liệu đơn hàng
                            loadStats(uid, role);

                        } else {
                            Log.e(TAG, "Document user không tồn tại trong Firestore");

                            // Hiển thị thông tin từ Firebase Auth
                            String authName = currentUser.getDisplayName();
                            String authEmail = currentUser.getEmail();

                            tvName.setText(authName != null && !authName.isEmpty() ?
                                    authName : "Người dùng");
                            tvEmail.setText(authEmail != null && !authEmail.isEmpty() ?
                                    authEmail : "Chưa có email");

                            if (authName != null && !authName.isEmpty()) {
                                tvAvatar.setText(authName.substring(0, 1).toUpperCase());
                            } else if (authEmail != null && !authEmail.isEmpty()) {
                                tvAvatar.setText(authEmail.substring(0, 1).toUpperCase());
                            } else {
                                tvAvatar.setText("?");
                            }

                            updateLabels("customer");
                            loadStats(uid, "customer");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Lỗi tải thông tin user từ Firestore: " + e.getMessage());

                        // Vẫn hiển thị thông tin từ Firebase Auth nếu Firestore lỗi
                        String authName = currentUser.getDisplayName();
                        String authEmail = currentUser.getEmail();

                        tvName.setText(authName != null && !authName.isEmpty() ?
                                authName : "Người dùng");
                        tvEmail.setText(authEmail != null && !authEmail.isEmpty() ?
                                authEmail : "Chưa có email");

                        if (authName != null && !authName.isEmpty()) {
                            tvAvatar.setText(authName.substring(0, 1).toUpperCase());
                        } else if (authEmail != null && !authEmail.isEmpty()) {
                            tvAvatar.setText(authEmail.substring(0, 1).toUpperCase());
                        }

                        updateLabels("customer");
                        loadStats(uid, "customer");
                    });

        } catch (Exception e) {
            Log.e(TAG, "Exception in loadUserData: " + e.getMessage(), e);
            Toast.makeText(this, "Lỗi tải dữ liệu: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void updateLabels(String role) {
        if (role != null && role.equalsIgnoreCase("seller")) {
            labelOrder.setText("Đã bán");
            labelProduct.setText("Sản phẩm");
            labelAmount.setText("Doanh thu");
            Log.d(TAG, "Cập nhật label cho SELLER");
        } else {
            labelOrder.setText("Đơn mua");
            labelProduct.setText("Đã mua");
            labelAmount.setText("Tổng chi");
            Log.d(TAG, "Cập nhật label cho CUSTOMER");
        }
    }

    private void loadStats(String uid, String role) {
        Log.d(TAG, "Đang load stats cho uid: " + uid + ", role: " + role);

        // Tạm thời hiển thị 0 cho tất cả
        tvOrderCount.setText("0");
        tvProductCount.setText("0");
        tvTotalAmount.setText("0đ");

        // TODO: Thêm logic tính toán thực tế sau
        // calculateBuyerStats(uid) hoặc calculateSellerStats(uid)
    }

    private void setupAccountRows() {
        findViewById(R.id.rowInfo).setOnClickListener(v -> {
            Toast.makeText(this, "Thông tin cá nhân", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.rowAddress).setOnClickListener(v -> {
            Toast.makeText(this, "Địa chỉ giao hàng", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.rowPayment).setOnClickListener(v -> {
            Toast.makeText(this, "Phương thức thanh toán", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.rowNotification).setOnClickListener(v -> {
            Toast.makeText(this, "Thông báo", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.rowSecurity).setOnClickListener(v -> {
            Toast.makeText(this, "Bảo mật", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.rowReport).setOnClickListener(v -> {
            Toast.makeText(this, "Báo cáo tiêu dùng", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.rowSupport).setOnClickListener(v -> {
            Toast.makeText(this, "Trung tâm hỗ trợ", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.rowTerms).setOnClickListener(v -> {
            Toast.makeText(this, "Điều khoản dịch vụ", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnSell).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, activity_seller_registration.class);
            startActivity(intent);
        });

        findViewById(R.id.rowSetting).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        });
    }

    private void setupLogoutButton() {
        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            if (mAuth != null) {
                mAuth.signOut();
            }
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data khi quay lại màn hình
        loadUserData();
    }

    private void autoUpdateMissingNames() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) return;

        String uid = currentUser.getUid();

        db.collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Kiểm tra xem có displayName không
                        if (!documentSnapshot.contains("displayName")) {
                            // Lấy email để tạo tên
                            String email = currentUser.getEmail();
                            String name = "Người dùng";

                            if (email != null && !email.isEmpty()) {
                                int atIndex = email.indexOf('@');
                                if (atIndex > 0) {
                                    name = email.substring(0, atIndex);
                                }
                            }

                            // Cập nhật vào Firestore
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("displayName", name);

                            db.collection("users").document(uid)
                                    .update(updates)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d(TAG, "Đã tự động cập nhật displayName cho user");
                                        // Load lại dữ liệu
                                        loadUserData();
                                    });
                        }
                    }
                });
    }
        private void setupBottomNavigation() {
            LinearLayout tabHome = findViewById(R.id.tabHome);
            LinearLayout tabCart = findViewById(R.id.tabCart);
            LinearLayout tabOrder = findViewById(R.id.tabOrder);
            LinearLayout tabAccount = findViewById(R.id.tabAccount);

            // Tab Đơn hàng
            tabOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ListOrderActivity.class);
                    startActivity(intent);
                }
            });

            // Tab Tài khoản (đã ở trang này nên không cần làm gì)
            tabAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Đã ở trang tài khoản, không cần điều hướng
                }
            });

            // Các tab khác có thể thêm tương tự
            tabHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Điều hướng đến trang chủ
                    // Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    // startActivity(intent);
                }
            });

            tabCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Điều hướng đến giỏ hàng
                    // Intent intent = new Intent(MainActivity.this, CartActivity.class);
                    // startActivity(intent);
                }
            });
    }
}