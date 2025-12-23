package com.example.uyen_ck;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Activity cho màn hình Trang Chủ (Home).
 * Nơi hiển thị danh sách sản phẩm, thanh điều hướng, v.v.
 */
public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: HomeActivity đã khởi tạo.");

        // Khởi tạo các View (ví dụ: nút Đăng Xuất)
        btnLogout = findViewById(R.id.btn_logout);

        // Thiết lập sự kiện cho nút Đăng Xuất (Giả lập)
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogout();
            }
        });

        // TODO: Viết logic để tải và hiển thị danh sách sản phẩm
        loadProductList();

        setupBottomNavigation();
    }

    /**
     * Giả lập việc tải dữ liệu sản phẩm.
     */
    private void loadProductList() {
        // Thực hiện các lệnh gọi API hoặc truy vấn cơ sở dữ liệu ở đây
        Log.d(TAG, "Đang tải danh sách sản phẩm...");
        Toast.makeText(this, "Trang chủ sẵn sàng. Tải dữ liệu thành công.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Xử lý quá trình đăng xuất.
     */
    private void handleLogout() {
        // TODO: Xóa thông tin đăng nhập (ví dụ: xóa token trong SharedPreferences)
        Log.i(TAG, "Đang đăng xuất người dùng...");

        // Chuyển hướng về màn hình Đăng Nhập
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        // Xóa tất cả các Activity trước đó khỏi stack
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Toast.makeText(this, "Đã đăng xuất.", Toast.LENGTH_SHORT).show();
    }

    private void setupBottomNavigation() {
        LinearLayout tabHome = findViewById(R.id.tabHome);
        LinearLayout tabCart = findViewById(R.id.tabCart);
        LinearLayout tabOrder = findViewById(R.id.tabOrder);
        LinearLayout tabAccount = findViewById(R.id.tabAccount);

        // Tab Trang chủ
        if (tabHome != null) {
            tabHome.setOnClickListener(v -> {
                // Không làm gì, đã ở HomeActivity
                Log.d(TAG, "Tab Trang chủ được nhấn");
            });
        }

        // Tab Giỏ hàng
        if (tabCart != null) {
            tabCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Tab Giỏ hàng được nhấn");
                    Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intent);
                    finish(); // Thêm finish() để đóng HomeActivity
                }
            });
        }

        // Tab Đơn hàng - ĐÃ SỬA LỖI
        if (tabOrder != null) {
            tabOrder.setOnClickListener(v -> {
                Log.d(TAG, "Tab Đơn hàng được nhấn");
                Intent intent = new Intent(HomeActivity.this, ListOrderActivity.class);
                startActivity(intent);
                finish(); // Thêm finish() để đóng HomeActivity
            });
        }

        // Tab Tài khoản
        if (tabAccount != null) {
            tabAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Tab Tài khoản được nhấn");
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Thêm finish() để đóng HomeActivity
                }
            });
        }
    }
}