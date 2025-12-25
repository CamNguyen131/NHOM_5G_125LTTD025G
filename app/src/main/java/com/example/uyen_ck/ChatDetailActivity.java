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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uyen_ck.models.Message;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ChatDetailActivity extends AppCompatActivity {

    private LinearLayout chatContainer;
    private ScrollView scrollViewChat;
    private EditText etMessage;
    private FirebaseFirestore db;

    private String currentChatId;
    private String currentUserId = "user_01"; // ID người đang đăng nhập

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        db = FirebaseFirestore.getInstance();

        chatContainer = findViewById(R.id.chatContainer);
        scrollViewChat = findViewById(R.id.scrollViewChat);
        etMessage = findViewById(R.id.etMessage);

        TextView tvName = findViewById(R.id.tvUserName);
        String userName = getIntent().getStringExtra("USER_NAME");
        currentChatId = getIntent().getStringExtra("CHAT_ID");

        if (userName != null) tvName.setText(userName);

        findViewById(R.id.btnBackChat).setOnClickListener(v -> finish());

        // 1. Tải và lắng nghe tin nhắn
        listenToMessages();

        // 2. Gửi tin nhắn
        findViewById(R.id.btnSend).setOnClickListener(v -> sendMessage());

        setupBottomNavigation();
    }

    private void listenToMessages() {
        if (currentChatId == null) return;

        // Truy cập vào subcollection "messages" bên trong document chat
        db.collection("chats").document(currentChatId).collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING) // Tin cũ ở trên, mới ở dưới
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {
                        if (error != null) return;

                        if (value != null) {
                            chatContainer.removeAllViews(); // Xóa view cũ để load lại (Cách đơn giản)

                            for (DocumentSnapshot doc : value.getDocuments()) {
                                Message msg = doc.toObject(Message.class);
                                if (msg != null) {
                                    // Kiểm tra xem tin nhắn là của mình hay người khác
                                    boolean isMe = msg.getSenderId().equals(currentUserId);

                                    String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(msg.getTimestamp()));
                                    addMessageToLayout(msg.getContent(), time, isMe);
                                }
                            }

                            // Tự động cuộn xuống dưới cùng
                            scrollViewChat.post(() -> scrollViewChat.fullScroll(View.FOCUS_DOWN));
                        }
                    }
                });
    }

    private void sendMessage() {
        String content = etMessage.getText().toString().trim();
        if (content.isEmpty()) return;

        long timestamp = System.currentTimeMillis();

        // 1. Tạo object tin nhắn
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("senderId", currentUserId);
        messageMap.put("content", content);
        messageMap.put("timestamp", timestamp);

        // 2. Lưu vào subcollection "messages"
        db.collection("chats").document(currentChatId).collection("messages")
                .add(messageMap)
                .addOnSuccessListener(documentReference -> {
                    etMessage.setText(""); // Xóa ô nhập sau khi gửi thành công
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Gửi thất bại", Toast.LENGTH_SHORT).show());

        // 3. Cập nhật "lastMessage" ở document cha (để hiện ở màn hình danh sách)
        Map<String, Object> lastMsgUpdate = new HashMap<>();
        lastMsgUpdate.put("lastMessage", content);
        lastMsgUpdate.put("lastTimestamp", timestamp);

        db.collection("chats").document(currentChatId).update(lastMsgUpdate);
    }

    // Hàm vẽ giao diện tin nhắn (Giữ nguyên logic của bạn)
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
        // Lưu ý: Đảm bảo bạn có file drawable bg_message_right và bg_message_left
        tvContent.setBackgroundResource(isMe ? R.drawable.bg_message_right : R.drawable.bg_message_left);
        messageLayout.addView(tvContent);

        TextView tvTime = new TextView(this);
        tvTime.setText(time);
        tvTime.setTextSize(12);
        tvTime.setTextColor(Color.GRAY);
        tvTime.setPadding(0, 8, 0, 0);
        // Căn chỉnh thời gian theo tin nhắn
        LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        timeParams.gravity = isMe ? Gravity.END : Gravity.START;
        tvTime.setLayoutParams(timeParams);

        messageLayout.addView(tvTime);

        chatContainer.addView(messageLayout);
    }

    private void setupBottomNavigation() {
        // ... (Giữ nguyên code điều hướng của bạn) ...
    }
}