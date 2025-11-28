package com.example.uyen_ck;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;
import java.util.List;

public class MessageListActivity extends AppCompatActivity {

    private LinearLayout tabOverview, tabProducts, tabMessages, tabProfile;
    private LinearLayout currentActiveTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        initializeNavigation();

        // RecyclerView setup
        RecyclerView rvMessages = findViewById(R.id.rvMessages);
        if (rvMessages != null) {
            rvMessages.setLayoutManager(new LinearLayoutManager(this));
            List<String> senders = Arrays.asList("Nguyễn Thị Lan", "Trần Văn Minh");
            rvMessages.setAdapter(new MessageAdapter(senders));
        }
    }

    private void initializeNavigation() {
        tabOverview = findViewById(R.id.tab_overview);
        tabProducts = findViewById(R.id.tab_products);
        tabMessages = findViewById(R.id.tab_messages);
        tabProfile = findViewById(R.id.tab_profile);

        setActiveTab(tabMessages);

        tabOverview.setOnClickListener(v -> {
            setActiveTab(tabOverview);
            // TODO: Navigate to overview screen
        });

        tabProducts.setOnClickListener(v -> {
            setActiveTab(tabProducts);
            // TODO: Navigate to products screen
        });

        tabMessages.setOnClickListener(v -> {
            setActiveTab(tabMessages);
            // TODO: Navigate to messages screen
        });

        tabProfile.setOnClickListener(v -> {
            setActiveTab(tabProfile);
            // TODO: Navigate to profile screen
        });
    }

    private void setActiveTab(LinearLayout tabItem) {
        // Remove highlight from previous active item
        if (currentActiveTab != null) {
            currentActiveTab.setBackgroundColor(android.graphics.Color.TRANSPARENT);
            // Reset colors to gray
            for (int i = 0; i < currentActiveTab.getChildCount(); i++) {
                View child = currentActiveTab.getChildAt(i);
                if (child instanceof ImageView) {
                    ((ImageView) child).setColorFilter(android.graphics.Color.parseColor("#9E9E9E"));
                } else if (child instanceof TextView) {
                    ((TextView) child).setTextColor(android.graphics.Color.parseColor("#9E9E9E"));
                }
            }
        }

        // Highlight new active item
        currentActiveTab = tabItem;
        tabItem.setBackgroundColor(android.graphics.Color.parseColor("#FFE8F5"));
        // Set colors to pink/magenta
        for (int i = 0; i < tabItem.getChildCount(); i++) {
            View child = tabItem.getChildAt(i);
            if (child instanceof ImageView) {
                ((ImageView) child).setColorFilter(android.graphics.Color.parseColor("#E91E63"));
            } else if (child instanceof TextView) {
                ((TextView) child).setTextColor(android.graphics.Color.parseColor("#E91E63"));
            }
        }
    }

    class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
        List<String> senders;

        public MessageAdapter(List<String> senders) {
            this.senders = senders;
        }

        @NonNull
        @Override
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
        public int getItemCount() {
            return senders.size();
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
}
