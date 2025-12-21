package com.example.baitapdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.view.MenuItem;

public class activity_seller_dashboard extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                Intent intent;
                switch (id) {
                    case R.id:
                        intent = new Intent(activity_seller_dashboard.this, HomeActivity.class);
                        break;

                    case androidx.core.R.id.title:
                        // mở ProductListActivity
                        intent = new Intent(activity_seller_dashboard.this, ProductListActivity.class);
                        break;

                    case R.id.tabOrder:
                        intent = new Intent(activity_seller_dashboard.this, ListOrderActivity.class);
                        break;

                    case R.id.ic_messages:
                        // mở MessageListActivity
                        intent = new Intent(activity_seller_dashboard.this, MessageListActivity.class);
                        break;

                    case R.id.ic_profile:
                        intent = new Intent(activity_seller_dashboard.this, MainActivity.class);
                        break;

                    default:
                        return false;
                }

                // tránh tạo nhiều instance (tùy chọn)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            }
        });
    }
}
