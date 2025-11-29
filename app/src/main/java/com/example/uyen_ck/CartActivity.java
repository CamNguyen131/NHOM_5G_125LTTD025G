package com.example.uyen_ck;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class CartActivity extends AppCompatActivity {

    private CheckBox cbSelectAll, cbSelectItem;
    private TextView tvQuantity;
    private ImageButton btnPlus, btnMinus, btnDelete;
    private Button btnCheckout;

    private int quantity = 1;
    private final int productPrice = 850000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initViews();
        setupProductActions();
        setupCheckoutButton();
        setupBottomNavigationWithHighlight();
    }

    private void initViews() {
        cbSelectAll   = findViewById(R.id.cbSelectAll);
        cbSelectItem  = findViewById(R.id.cbSelectItem);
        tvQuantity    = findViewById(R.id.tvQuantity);
        btnPlus       = findViewById(R.id.btnPlus);
        btnMinus      = findViewById(R.id.btnMinus);
        btnDelete     = findViewById(R.id.btnDelete);
        btnCheckout   = findViewById(R.id.btnCheckout);
    }

    private void setupProductActions() {
        btnPlus.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
            updateCheckoutText();
        });

        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
                updateCheckoutText();
            }
        });

        btnDelete.setOnClickListener(v -> {
            quantity = 0;
            tvQuantity.setText("0");
            cbSelectItem.setChecked(false);
            cbSelectAll.setChecked(false);
            updateCheckoutText();
            Toast.makeText(this, "Đã xóa sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
        });

        cbSelectItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cbSelectAll.setChecked(isChecked);
            updateCheckoutText();
        });
        cbSelectAll.setOnCheckedChangeListener((buttonView, isChecked) ->
                cbSelectItem.setChecked(isChecked));
    }

    private void setupCheckoutButton() {
        btnCheckout.setOnClickListener(v -> {
            if (quantity == 0 || !cbSelectItem.isChecked()) {
                Toast.makeText(this, "Vui lòng chọn sản phẩm để thanh toán", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
            intent.putExtra("total_amount", productPrice * quantity);
            intent.putExtra("item_count", quantity);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        updateCheckoutText();
    }

    private void updateCheckoutText() {
        if (quantity == 0 || !cbSelectItem.isChecked()) {
            btnCheckout.setText("Thanh toán (0 sản phẩm)");
            btnCheckout.setEnabled(false);
        } else {
            btnCheckout.setText(String.format("Thanh toán (%d sản phẩm)", quantity));
            btnCheckout.setEnabled(true);
        }
    }
    private void setupBottomNavigationWithHighlight() {
        LinearLayout tabHome    = findViewById(R.id.tabHome);
        LinearLayout tabCart    = findViewById(R.id.tabCart);
        LinearLayout tabOrder   = findViewById(R.id.tabOrder);
        LinearLayout tabAccount = findViewById(R.id.tabAccount);

        int colorGray = ContextCompat.getColor(this, android.R.color.darker_gray);
        int colorPink = ContextCompat.getColor(this, R.color.pink_primary); // hoặc #FF6B87

        setTabColor(tabHome, colorGray);
        setTabColor(tabOrder, colorGray);
        setTabColor(tabAccount, colorGray);
        setTabColor(tabCart, colorPink);
        if (tabCart != null) {
            tabCart.setBackgroundResource(R.drawable.gb_pink_light);
        }

        if (tabHome != null) tabHome.setOnClickListener(v -> startAndFinish(HomeActivity.class));
        if (tabCart != null) tabCart.setOnClickListener(v -> { /* đang ở đây */ });
        if (tabOrder != null) tabOrder.setOnClickListener(v -> startAndFinish(ListOrderActivity.class));
        if (tabAccount != null) tabAccount.setOnClickListener(v -> startAndFinish(MainActivity.class));

        overridePendingTransition(0, 0);
    }

    private void setTabColor(LinearLayout tab, int color) {
        if (tab == null) return;
        for (int i = 0; i < tab.getChildCount(); i++) {
            var child = tab.getChildAt(i);
            if (child instanceof ImageView) {
                ((ImageView) child).setColorFilter(color);
            } else if (child instanceof TextView) {
                ((TextView) child).setTextColor(color);
            }
        }
    }

    private void startAndFinish(Class<?> cls) {
        startActivity(new Intent(this, cls));
        finish();
    }
}