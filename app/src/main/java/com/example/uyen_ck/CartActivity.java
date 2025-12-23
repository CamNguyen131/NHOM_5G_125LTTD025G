package com.example.uyen_ck;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CartActivity extends AppCompatActivity {

    private TextView tvSubtotal, tvTotal;
    private Button btnCheckout;
    private CheckBox cbSelectAll;
    private ImageButton btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initViews();
        setupEvents();
        updateCartSummary();
        setupBottomNavigation();
    }

    private void initViews() {
        tvSubtotal = findViewById(R.id.txtTamTinh);
        tvTotal = findViewById(R.id.txtTongTien);
        btnCheckout = findViewById(R.id.btnCheckout);
        cbSelectAll = findViewById(R.id.cbSelectAll);
        btnDelete = findViewById(R.id.btnDelete);
    }

    private void setupEvents() {
        btnCheckout.setOnClickListener(v -> {
            int count = CartManager.getInstance().getItemCount();
            long total = CartManager.getInstance().getTotalPrice();

            if (count == 0) {
                Toast.makeText(this, "Không có sản phẩm để thanh toán", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Tổng thanh toán: " + String.format("%,dđ", total), Toast.LENGTH_LONG).show();
            startActivity(new Intent(CartActivity.this, CheckoutActivity.class));
        });


        btnDelete.setOnClickListener(v -> {
            CartManager.getInstance().clearCart();
            Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
            updateCartSummary();
        });

        cbSelectAll.setOnCheckedChangeListener((btn, isChecked) -> updateCartSummary());


    }

    private void updateCartSummary() {
        long total = CartManager.getInstance().getTotalPrice();
        int count = CartManager.getInstance().getItemCount();

        String price = count > 0 ? String.format("%,dđ", total) : "0đ";
        tvSubtotal.setText(price);
        tvTotal.setText(price);
        btnCheckout.setText("Thanh toán (" + count + " sản phẩm)");
        cbSelectAll.setChecked(count > 0);
        cbSelectAll.setText("Chọn tất cả (" + count + "/" + count + ")");
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartSummary();
    }

    private void setupBottomNavigation() {
        LinearLayout tabHome = findViewById(R.id.tabHome);
        LinearLayout tabCart = findViewById(R.id.tabCart);
        LinearLayout tabOrder = findViewById(R.id.tabOrder);
        LinearLayout tabAccount = findViewById(R.id.tabAccount);

        // Tab Tài khoản
        tabAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        // Tab Đơn hàng
        tabOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, ListOrderActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        // Các tab khác
        tabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        tabCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(CartActivity.this, CartActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
            }
        });
    }
}