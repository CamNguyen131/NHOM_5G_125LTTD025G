package com.example.uyen_ck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout tabHome, tabCart, tabOrder, tabAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initBottomNavigation();
        setupClickListeners();
    }

    private void initBottomNavigation() {
        tabHome    = findViewById(R.id.tabHome);
        tabCart    = findViewById(R.id.tabCart);
        tabOrder   = findViewById(R.id.tabOrder);
        tabAccount = findViewById(R.id.tabAccount);

        int colorPink = ContextCompat.getColor(this, R.color.pink_primary);
        int colorGray = ContextCompat.getColor(this, android.R.color.darker_gray);

        setTabColor(tabHome, colorPink);
        setTabColor(tabCart, colorGray);
        setTabColor(tabOrder, colorGray);
        setTabColor(tabAccount, colorGray);

        if (tabHome != null) {
            tabHome.setBackgroundResource(R.drawable.gb_pink_light);
        }
    }
    private void setupClickListeners() {
        tabHome.setOnClickListener(v -> Toast.makeText(this, "Đã ở Trang chủ", Toast.LENGTH_SHORT).show());
        tabCart.setOnClickListener(v -> startActivityAndFinish(CartActivity.class));
        tabOrder.setOnClickListener(v -> startActivityAndFinish(ListOrderActivity.class));
        tabAccount.setOnClickListener(v -> startActivityAndFinish(MainActivity.class));
        findViewById(R.id.etSearch).setOnClickListener(v -> toast("Tìm kiếm"));
        findViewById(R.id.btnNotification).setOnClickListener(v -> toast("Thông báo"));
        findViewById(R.id.btnPromo).setOnClickListener(v -> toast("Flash Sale!"));
        int[] categories = {R.id.categoryFaceCare, R.id.categoryMakeup, R.id.categoryHairCare,
                R.id.categoryMore, R.id.categoryGift, R.id.categorySale, R.id.categoryNew};
        for (int id : categories) {
            findViewById(id).setOnClickListener(v -> toast("Mở danh mục"));
        }
        findViewById(R.id.btnViewAllNewProducts).setOnClickListener(v -> toast("Xem tất cả"));
        findViewById(R.id.tvProductName1).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
            intent.putExtra("product_id", "SP001");
            intent.putExtra("product_name", "Kem Chống Nắng Anthelios UV Mune 400");
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }
    //
    private void setTabColor(LinearLayout tab, int color) {
        if (tab == null) return;
        for (int i = 0; i < tab.getChildCount(); i++) {
            View child = tab.getChildAt(i);
            if (child instanceof ImageView) {
                ((ImageView) child).setColorFilter(color);
            } else if (child instanceof TextView) {
                ((TextView) child).setTextColor(color);
            }
        }
    }
    private void startActivityAndFinish(Class<?> cls) {
        startActivity(new Intent(this, cls));
        finish();
        overridePendingTransition(0, 0);
    }
    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}