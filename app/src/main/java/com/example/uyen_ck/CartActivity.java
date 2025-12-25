package com.example.uyen_ck;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uyen_ck.adapters.CartAdapter;
import com.example.uyen_ck.models.Cart;
import com.example.uyen_ck.models.CartDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartUpdateListener {

    // Khai báo View
    private RecyclerView rvCart;
    private CartAdapter adapter;
    private List<CartDetail> itemList = new ArrayList<>();

    private TextView txtTongTien, txtTamTinh, txtPhiVanChuyen;
    private Button btnCheckout;
    private CheckBox cbSelectAll;

    // Firebase
    private FirebaseFirestore db;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        db = FirebaseFirestore.getInstance();

        // Lấy ID người dùng (dùng user_03 nếu chưa đăng nhập để test)
        currentUserId = (FirebaseAuth.getInstance().getCurrentUser() != null)
                ? FirebaseAuth.getInstance().getCurrentUser().getUid() : "user_03";

        initViews();
        loadCartData();
        setupBottomNavigation();
    }

    private void initViews() {
        rvCart = findViewById(R.id.rvCart);
        txtTongTien = findViewById(R.id.txtTongTien);
        txtTamTinh = findViewById(R.id.txtTamTinh);
        txtPhiVanChuyen = findViewById(R.id.txtPhiVanChuyen);
        btnCheckout = findViewById(R.id.btnCheckout);
        cbSelectAll = findViewById(R.id.cbSelectAll);

        // Setup RecyclerView
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(itemList, this);
        rvCart.setAdapter(adapter);

        // --- SỰ KIỆN NÚT THANH TOÁN (ĐÃ SỬA LỖI CRASH) ---
        btnCheckout.setOnClickListener(v -> {
            if (itemList.size() > 0) {
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);

                // 1. Đánh dấu là mua từ giỏ hàng (nhiều món)
                intent.putExtra("is_buy_now", false);

                // 2. Truyền toàn bộ danh sách sản phẩm sang CheckoutActivity
                // QUAN TRỌNG: Tạo mới ArrayList để Android hiểu kiểu dữ liệu, tránh lỗi Parcel
                ArrayList<CartDetail> listToSend = new ArrayList<>(itemList);
                intent.putExtra("cart_items", listToSend);

                startActivity(intent);
            } else {
                Toast.makeText(this, "Giỏ hàng đang trống", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- TẢI DỮ LIỆU TỪ FIREBASE (REALTIME) ---
    private void loadCartData() {
        db.collection("carts")
                .document("cart_user_" + currentUserId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;

                    if (value != null && value.exists()) {
                        Cart cart = value.toObject(Cart.class);

                        if (cart != null && cart.getItems() != null) {
                            itemList.clear();
                            itemList.addAll(cart.getItems());

                            // Cập nhật danh sách và tính tiền
                            adapter.notifyDataSetChanged();
                            calculateTotalPrice();
                        }
                    } else {
                        // Trường hợp giỏ hàng trống/bị xóa
                        itemList.clear();
                        adapter.notifyDataSetChanged();
                        calculateTotalPrice();
                    }
                });
    }

    // --- TÍNH TỔNG TIỀN ---
    private void calculateTotalPrice() {
        double subTotal = 0; // Tạm tính
        int totalQuantity = 0; // Tổng số lượng sản phẩm

        for (CartDetail item : itemList) {
            subTotal += item.getSubTotal();
            totalQuantity += item.getQuantity();
        }

        // Logic phí vận chuyển: Trên 500k Freeship, dưới 500k ship 30k
        double shippingFee = (subTotal >= 500000 || subTotal == 0) ? 0 : 30000;
        double finalTotal = subTotal + shippingFee;

        // Hiển thị lên giao diện
        String strSubTotal = String.format("%,.0fđ", subTotal);
        String strTotal = String.format("%,.0fđ", finalTotal);

        txtTamTinh.setText(strSubTotal);
        txtTongTien.setText(strTotal);

        // Hiển thị phí ship
        if (txtPhiVanChuyen != null) {
            if (shippingFee == 0) {
                txtPhiVanChuyen.setText("Miễn phí");
                // Kiểm tra xem màu holo_green_dark có tồn tại không, nếu không dùng Color.GREEN
                txtPhiVanChuyen.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            } else {
                txtPhiVanChuyen.setText(String.format("%,.0fđ", shippingFee));
                txtPhiVanChuyen.setTextColor(getResources().getColor(android.R.color.black));
            }
        }

        // Cập nhật text nút thanh toán
        btnCheckout.setText("Thanh toán (" + totalQuantity + " sản phẩm)");
    }

    // --- UPDATE LẠI FIREBASE KHI NGƯỜI DÙNG TĂNG/GIẢM/XÓA Ở ADAPTER ---
    @Override
    public void onUpdate(List<CartDetail> newList) {
        db.collection("carts")
                .document("cart_user_" + currentUserId)
                .update(
                        "items", newList,
                        "updatedAt", System.currentTimeMillis()
                )
                .addOnSuccessListener(aVoid -> calculateTotalPrice()) // Update thành công thì tính lại tiền
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi cập nhật", Toast.LENGTH_SHORT).show());
    }

    // --- THANH ĐIỀU HƯỚNG DƯỚI ĐÁY ---
    private void setupBottomNavigation() {
        LinearLayout tabHome = findViewById(R.id.tabHome);
        LinearLayout tabCart = findViewById(R.id.tabCart);
        LinearLayout tabOrder = findViewById(R.id.tabOrder);
        LinearLayout tabAccount = findViewById(R.id.tabAccount);

        if (tabHome != null) {
            tabHome.setOnClickListener(v -> {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            });
        }

        // Tab giỏ hàng (đang ở đây -> không làm gì)

        if (tabOrder != null) {
            tabOrder.setOnClickListener(v -> {
                startActivity(new Intent(this, ListOrderActivity.class));
                finish();
            });
        }

        if (tabAccount != null) {
            tabAccount.setOnClickListener(v -> {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            });
        }
    }
}