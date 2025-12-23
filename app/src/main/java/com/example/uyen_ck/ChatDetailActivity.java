package com.example.uyen_ck;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
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

        chatContainer = findViewById(R.id.chatContainer);
        scrollViewChat = findViewById(R.id.scrollViewChat);
        etMessage = findViewById(R.id.etMessage);
        TextView tvName = findViewById(R.id.tvUserName);
        String userName = getIntent().getStringExtra("USER_NAME");
        if (userName != null) tvName.setText(userName);
        findViewById(R.id.btnBackChat).setOnClickListener(v -> finish());
        addMessageToLayout("Sản phẩm này còn hàng không ạ?", "5 phút trước", false);
        addMessageToLayout("Dạ sản phẩm vẫn còn hàng ạ!", "2 phút trước", true);
        findViewById(R.id.btnSend).setOnClickListener(v -> {
            String content = etMessage.getText().toString().trim();
            if (!content.isEmpty()) {
                addMessageToLayout(content, "Vừa xong", true);
                etMessage.setText("");
                scrollViewChat.post(() -> scrollViewChat.fullScroll(View.FOCUS_DOWN));
            }
        });
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        findViewById(R.id.tab_overview).setOnClickListener(v -> {
            Intent intent = new Intent(ChatDetailActivity.this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });

        findViewById(R.id.tab_products).setOnClickListener(v -> {
            Intent intent = new Intent(ChatDetailActivity.this, ProductListActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });
        findViewById(R.id.tab_messages).setOnClickListener(v -> {
        });
        findViewById(R.id.tab_profile).setOnClickListener(v -> {
            Intent intent = new Intent(ChatDetailActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });
    }
    private void addMessageToLayout(String text, String time, boolean isMe) {
        LinearLayout messageLayout = new LinearLayout(this);
        messageLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 24);
        messageLayout.setLayoutParams(layoutParams);
        messageLayout.setGravity(isMe ? Gravity.END : Gravity.START);

        TextView tvContent = new TextView(this);
        tvContent.setText(text);
        tvContent.setTextSize(16);
        tvContent.setPadding(32, 24, 32, 24);
        tvContent.setMaxWidth(800);
        tvContent.setTextColor(isMe ? Color.WHITE : Color.BLACK);
        tvContent.setBackgroundResource(isMe ? R.drawable.bg_message_right : R.drawable.bg_message_left);
        messageLayout.addView(tvContent);

        TextView tvTime = new TextView(this);
        tvTime.setText(time);
        tvTime.setTextSize(12);
        tvTime.setTextColor(Color.GRAY);
        tvTime.setPadding(0, 8, 0, 0);
        messageLayout.addView(tvTime);

        chatContainer.addView(messageLayout);
    }
}