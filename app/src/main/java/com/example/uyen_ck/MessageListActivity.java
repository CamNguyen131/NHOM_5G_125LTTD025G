package com.example.uyen_ck;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uyen_ck.models.Chat;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageListActivity extends AppCompatActivity {

    private LinearLayout tabOverview, tabProducts, tabMessages, tabProfile;
    private LinearLayout currentActiveTab;
    private RecyclerView rvMessages;
    private MessageAdapter adapter;
    private List<Chat> chatList;
    private FirebaseFirestore db;

    // GIẢ LẬP ID NGƯỜI DÙNG HIỆN TẠI (Sau này bạn lấy từ FirebaseAuth)
    private String currentUserId = "user_01";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        db = FirebaseFirestore.getInstance();
        initializeNavigation();

        rvMessages = findViewById(R.id.rvMessages);
        rvMessages.setLayoutManager(new LinearLayoutManager(this));

        chatList = new ArrayList<>();
        adapter = new MessageAdapter(chatList);
        rvMessages.setAdapter(adapter);

        loadChatsFromFirebase();
    }

    private void loadChatsFromFirebase() {
        // Tìm các cuộc trò chuyện có chứa user_01 trong mảng participants
        db.collection("chats")
                .whereArrayContains("participants", currentUserId)
                .orderBy("lastTimestamp", Query.Direction.DESCENDING) // Sắp xếp tin nhắn mới nhất lên đầu
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Lỗi tải tin nhắn: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (value != null) {
                        chatList.clear();
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            Chat chat = doc.toObject(Chat.class);
                            if (chat != null) {
                                chatList.add(chat);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    // --- Adapter ---
    class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
        List<Chat> chats;

        public MessageAdapter(List<Chat> chats) {
            this.chats = chats;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Dùng layout mặc định simple_list_item_2 cho nhanh (như code cũ của bạn)
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Chat chat = chats.get(position);

            // Logic tìm tên người kia: Lấy ID khác với ID của mình trong mảng participants
            String otherUserId = "Unknown";
            if (chat.getParticipants() != null) {
                for (String id : chat.getParticipants()) {
                    if (!id.equals(currentUserId)) {
                        otherUserId = id;
                        break;
                    }
                }
            }

            // Hiển thị tên người chat (Tạm thời hiện ID, bạn cần query bảng users để lấy tên thật)
            holder.text1.setText("Chat với: " + otherUserId);

            // Hiển thị tin nhắn cuối cùng và thời gian
            String time = new SimpleDateFormat("HH:mm dd/MM", Locale.getDefault()).format(new Date(chat.getLastTimestamp()));
            holder.text2.setText(chat.getLastMessage() + " (" + time + ")");

            String finalOtherUserId = otherUserId;
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(MessageListActivity.this, ChatDetailActivity.class);
                intent.putExtra("CHAT_ID", chat.getChatId()); // Quan trọng: Truyền ID cuộc trò chuyện
                intent.putExtra("USER_NAME", finalOtherUserId); // Truyền tên để hiển thị header
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return chats.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView text1, text2;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                text1 = itemView.findViewById(android.R.id.text1);
                text2 = itemView.findViewById(android.R.id.text2);
            }
        }
    }

    private void initializeNavigation() {
        tabOverview = findViewById(R.id.tab_overview);
        tabProducts = findViewById(R.id.tab_products);
        tabMessages = findViewById(R.id.tab_messages);
        tabProfile = findViewById(R.id.tab_profile);
        setActiveTab(tabMessages);

        // ... (Giữ nguyên phần xử lý click tab chuyển trang của bạn) ...
        if (tabOverview != null) {
            tabOverview.setOnClickListener(v -> {
                // Thêm code chuyển trang SellerDashboard
                finish();
            });
        }
    }

    private void setActiveTab(LinearLayout tabItem) {
        // ... (Giữ nguyên logic đổi màu tab của bạn) ...
    }
}