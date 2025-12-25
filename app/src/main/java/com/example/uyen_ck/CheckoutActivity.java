package com.example.uyen_ck;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uyen_ck.models.CartDetail;
import com.example.uyen_ck.models.Order;
import com.example.uyen_ck.models.OrderDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private TextView tvSubtotal, tvTotal;
    private EditText edtBuyerName, edtPhoneNumber, edtDeliveryAddress, edtNote;
    private Button btnOrder;
    private ImageButton btnBack;

    private FirebaseFirestore db;
    private long finalTotal = 0;

    private boolean isBuyNow;
    private List<CartDetail> cartItems;

    // Biến nhận dữ liệu Mua Ngay
    private String pName, pVariant;
    private int pQty;
    private long pPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        db = FirebaseFirestore.getInstance();
        initViews();

        Intent intent = getIntent();
        isBuyNow = intent.getBooleanExtra("is_buy_now", true);

        if (isBuyNow) {
            // MUA NGAY
            pName = intent.getStringExtra("productName");
            pVariant = intent.getStringExtra("variant");
            pQty = intent.getIntExtra("quantity", 0);
            pPrice = intent.getLongExtra("price", 0);

            finalTotal = pQty * pPrice;
        } else {
            // MUA TỪ GIỎ HÀNG -> Sửa cách nhận dữ liệu ở đây
            // Ép kiểu về ArrayList<CartDetail>
            cartItems = (ArrayList<CartDetail>) intent.getSerializableExtra("cart_items");

            finalTotal = 0;
            if (cartItems != null) {
                for (CartDetail item : cartItems) {
                    finalTotal += (long) (item.getQuantity() * item.getPrice());
                }
            }
        }

        displayTotal();

        btnBack.setOnClickListener(v -> finish());
        btnOrder.setOnClickListener(v -> validateAndSaveOrder());
    }

    private void initViews() {
        tvSubtotal = findViewById(R.id.txtSubtotal);
        tvTotal = findViewById(R.id.txtTotalAmount);
        btnOrder = findViewById(R.id.btnOrder);
        btnBack = findViewById(R.id.btnBack);
        edtBuyerName = findViewById(R.id.edtBuyerName);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtDeliveryAddress = findViewById(R.id.edtDeliveryAddress);
        edtNote = findViewById(R.id.edtNote);
    }

    private void displayTotal() {
        String formatted = String.format("%,dđ", finalTotal);
        tvSubtotal.setText(formatted);
        tvTotal.setText(formatted);
        btnOrder.setText("Đặt hàng (" + formatted + ")");
    }

    private void validateAndSaveOrder() {
        String name = edtBuyerName.getText().toString().trim();
        String phone = edtPhoneNumber.getText().toString().trim();
        String address = edtDeliveryAddress.getText().toString().trim();
        String note = edtNote.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentUserId = (FirebaseAuth.getInstance().getCurrentUser() != null)
                ? FirebaseAuth.getInstance().getCurrentUser().getUid() : "user_03";

        btnOrder.setEnabled(false);
        btnOrder.setText("Đang xử lý...");

        db.runTransaction(transaction -> {
            DocumentReference orderCountRef = db.collection("counters").document("order_counter");
            Long currentOrderVal = transaction.get(orderCountRef).getLong("currentValue");
            long nextOrderVal = (currentOrderVal != null) ? currentOrderVal + 1 : 1;

            String autoOrderId = String.format("order_%02d", nextOrderVal);
            String orderCode = "ORD" + String.format("%02d", nextOrderVal);

            Order order = new Order();
            order.setOrderId(autoOrderId);
            order.setBuyerId(currentUserId);
            order.setOrderCode(orderCode);
            order.setBuyerName(name);
            order.setDeliveryAddress(address);
            order.setPhoneNumber(phone);
            order.setTotalAmount((double) finalTotal);
            order.setCreatedAt(System.currentTimeMillis());
            order.setStatus("pending");
            order.setNote(note);

            List<OrderDetails> orderItemsList = new ArrayList<>();

            if (isBuyNow) {
                orderItemsList.add(new OrderDetails(pName, pQty, (double) pPrice, pVariant));
            } else {
                if (cartItems != null) {
                    for (CartDetail cartItem : cartItems) {
                        OrderDetails detail = new OrderDetails(
                                cartItem.getProductName(),
                                cartItem.getQuantity(),
                                cartItem.getPrice(),
                                cartItem.getVariant()
                        );
                        detail.setProductImage(cartItem.getProductImage());
                        orderItemsList.add(detail);
                    }
                }
            }
            order.setItems(orderItemsList);

            transaction.set(db.collection("orders").document(autoOrderId), order);
            transaction.update(orderCountRef, "currentValue", nextOrderVal);

            return autoOrderId;

        }).addOnSuccessListener(orderId -> {
            Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_LONG).show();
            if (!isBuyNow) {
                clearFirestoreCart(currentUserId);
            } else {
                navigateToOrderList();
            }
        }).addOnFailureListener(e -> {
            btnOrder.setEnabled(true);
            btnOrder.setText("Đặt hàng");
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    private void clearFirestoreCart(String userId) {
        db.collection("carts").document("cart_user_" + userId)
                .update("items", Collections.emptyList())
                .addOnCompleteListener(task -> navigateToOrderList());
    }

    private void navigateToOrderList() {
        Intent intent = new Intent(CheckoutActivity.this, ListOrderActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}