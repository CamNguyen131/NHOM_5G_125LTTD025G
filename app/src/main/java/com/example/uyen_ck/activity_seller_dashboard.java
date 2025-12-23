package com.example.uyen_ck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class activity_seller_dashboard extends AppCompatActivity {

    private LinearLayout tabOrder, tabCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);

        // Ánh xạ các view
        tabOrder = findViewById(R.id.tabOrder);
        tabCart = findViewById(R.id.tabCart);

        // Xử lý sự kiện nhấn vào tab Đơn hàng (Tin nhắn)
        tabOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến MessageListActivity
                Intent intent = new Intent(activity_seller_dashboard.this, MessageListActivity.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện nhấn vào tab Giỏ hàng (Sản phẩm)
        tabCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến ProductListActivity
                Intent intent = new Intent(activity_seller_dashboard.this, ProductListActivity.class);
                startActivity(intent);
            }
        });
    }
}