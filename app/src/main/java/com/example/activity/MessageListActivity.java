package com.example.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class MessageListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        RecyclerView rvMessages = findViewById(R.id.rvMessages);
        if (rvMessages != null) {
            rvMessages.setLayoutManager(new LinearLayoutManager(this));
            List<String> senders = Arrays.asList("Nguyễn Thị Lan", "Trần Văn Minh");
            rvMessages.setAdapter(new MessageAdapter(senders));
        }
    }

    class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
        List<String> senders;
        public MessageAdapter(List<String> senders) { this.senders = senders; }

        @NonNull @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.text1.setText(senders.get(position));
            holder.text2.setText("Tin nhắn mới...");
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(MessageListActivity.this, ChatDetailActivity.class);
                intent.putExtra("USER_NAME", senders.get(position));
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() { return senders.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView text1, text2;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                text1 = itemView.findViewById(android.R.id.text1);
                text2 = itemView.findViewById(android.R.id.text2);
            }
        }
    }
}