package com.example.baitapdemo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp Hoạt động Chính, là điểm truy cập đầu tiên của ứng dụng.
 * Ở đây, chúng ta sẽ mô phỏng việc khởi tạo UI và tải dữ liệu.
 */
public class HeadActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lưu ý: Cần có file layout XML tên là 'activity_main' trong res/layout/
        // Ví dụ: setContentView(R.layout.activity_main);

        // Giả lập giao diện, nếu không có file XML layout
        Log.d(TAG, "onCreate: Khởi tạo MainActivity");

        // Mô phỏng việc tải dữ liệu sản phẩm
        loadInitialData();
    }

    /**
     * Hàm giả lập việc tải dữ liệu sản phẩm ban đầu,
     * ví dụ từ API hoặc Database.
     */
    private void loadInitialData() {
        List<Product> featuredProducts = new ArrayList<>();

        // Thêm một vài sản phẩm mẫu
        featuredProducts.add(new Product(
                "P001",
                "Áo Thun Thể Thao",
                "Áo thun co giãn tốt, phù hợp cho tập gym và chạy bộ.",
                250000.0,
                4.5f,
                "url_image_ao_thun",
                false));

        featuredProducts.add(new Product(
                "P002",
                "Giày Sneakers Nữ",
                "Giày màu hồng nhẹ nhàng, đế cao su chống trượt.",
                780000.0,
                4.8f,
                "url_image_sneakers",
                true));

        Log.i(TAG, "Đã tải thành công " + featuredProducts.size() + " sản phẩm nổi bật.");

        // Hiển thị thông báo Toast đơn giản
        Toast.makeText(this,
                "Chào mừng đến với ứng dụng E-commerce!",
                Toast.LENGTH_LONG).show();

        // Kiểm tra dữ liệu sản phẩm đầu tiên
        if (!featuredProducts.isEmpty()) {
            Product firstProduct = featuredProducts.get(0);
            Log.d(TAG, "Sản phẩm đầu tiên: " + firstProduct.getName() + " - Giá: " + firstProduct.getPrice());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Ứng dụng hiển thị.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: Ứng dụng bị che khuất.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Ứng dụng bị hủy.");
    }
}