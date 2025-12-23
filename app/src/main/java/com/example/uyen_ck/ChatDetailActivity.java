package com.example.uyen_ck;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ChatDetailActivity extends AppCompatActivity {

    private LinearLayout chatContainer;
    private ScrollView scrollViewChat;
    private EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        // 1. Ánh xạ View
        chatContainer = findViewById(R.id.chatContainer);
        scrollViewChat = findViewById(R.id.scrollViewChat);
        etMessage = findViewById(R.id.etMessage);
        TextView tvName = findViewById(R.id.tvUserName);

        // 2. Nhận tên người chat
        String userName = getIntent().getStringExtra("USER_NAME");
        if(userName != null) tvName.setText(userName);

        // 3. Xử lý nút Back
        findViewById(R.id.btnBackChat).setOnClickListener(v -> finish());

        // 4. Thêm dữ liệu mẫu (Hardcode giống hình)
        addMessageToLayout("Sản phẩm này còn hàng không ạ?", "5 phút trước", false);
        addMessageToLayout("Dạ sản phẩm vẫn còn hàng ạ! Bạn có muốn đặt hàng không?", "2 phút trước", true);

        // 5. Xử lý gửi tin nhắn
        findViewById(R.id.btnSend).setOnClickListener(v -> {
            String content = etMessage.getText().toString().trim();
            if (!content.isEmpty()) {
                addMessageToLayout(content, "Vừa xong", true);
                etMessage.setText("");

                // Cuộn xuống dưới cùng
                scrollViewChat.post(() -> scrollViewChat.fullScroll(View.FOCUS_DOWN));
            }
        });
    }

    // Hàm tự tạo giao diện tin nhắn bằng Code (Không cần XML item)
    private void addMessageToLayout(String text, String time, boolean isMe) {
        // 1. Layout bao quanh 1 dòng chat
        LinearLayout messageLayout = new LinearLayout(this);
        messageLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 24); // Cách dưới 24px
        messageLayout.setLayoutParams(layoutParams);

        // Căn lề trái hay phải
        if (isMe) {
            messageLayout.setGravity(Gravity.END);
        } else {
            messageLayout.setGravity(Gravity.START);
        }

        // 2. TextView hiển thị nội dung tin nhắn
        TextView tvContent = new TextView(this);
        tvContent.setText(text);
        tvContent.setTextSize(16);
        tvContent.setPadding(32, 24, 32, 24);
        tvContent.setMaxWidth(800); // Giới hạn chiều rộng để không tràn màn hình

        if (isMe) {
            tvContent.setTextColor(Color.WHITE);
            tvContent.setBackgroundResource(R.drawable.bg_message_right); // Màu hồng
        } else {
            tvContent.setTextColor(Color.BLACK);
            tvContent.setBackgroundResource(R.drawable.bg_message_left); // Màu trắng/xám
        }

        messageLayout.addView(tvContent);

        // 3. TextView hiển thị thời gian
        TextView tvTime = new TextView(this);
        tvTime.setText(time);
        tvTime.setTextSize(12);
        tvTime.setTextColor(Color.GRAY);
        tvTime.setPadding(0, 8, 0, 0); // Cách tin nhắn 1 chút

        messageLayout.addView(tvTime);

        // 4. Thêm vào màn hình chính
        chatContainer.addView(messageLayout);
    }
}