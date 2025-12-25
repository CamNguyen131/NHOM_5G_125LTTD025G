package com.example.uyen_ck;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uyen_ck.models.Order;
import com.example.uyen_ck.models.OrderDetails;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {
    private TextView tvSubtotal, tvTotal;
    private EditText edtBuyerName, edtPhoneNumber, edtDeliveryAddress, edtNote;
    private Button btnOrder;
    private ImageButton btnBack;

    private FirebaseFirestore db;
    private long finalTotal = 0;

    private String pName, pVariant;
    private int pQty;
    private long pPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        db = FirebaseFirestore.getInstance();
        initViews();

        pName = getIntent().getStringExtra("productName");
        pVariant = getIntent().getStringExtra("variant");
        pQty = getIntent().getIntExtra("quantity", 0);
        pPrice = getIntent().getLongExtra("price", 0);

        if (pName != null && pQty > 0) {
            finalTotal = pQty * pPrice;
        } else {
            finalTotal = CartManager.getInstance().getTotalPrice();
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
    }

    private void validateAndSaveOrder() {
        String name = edtBuyerName.getText().toString().trim();
        String phone = edtPhoneNumber.getText().toString().trim();
        String address = edtDeliveryAddress.getText().toString().trim();
        String note = edtNote.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin giao hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        db.runTransaction(transaction -> {
            DocumentReference orderCountRef = db.collection("counters").document("order_counter");
            DocumentReference buyerCountRef = db.collection("counters").document("buyer_counter");

            long nextOrderVal = transaction.get(orderCountRef).getLong("currentValue") + 1;
            long nextBuyerVal = transaction.get(buyerCountRef).getLong("currentValue") + 1;

            String autoOrderId = String.format("order_%02d", nextOrderVal);
            String autoBuyerId = String.format("buyer_%02d", nextBuyerVal);
            String orderCode = "ORD" + String.format("%02d", nextOrderVal);

            Order order = new Order();
            order.setOrderId(autoOrderId);
            order.setBuyerId(autoBuyerId);
            order.setOrderCode(orderCode);
            order.setBuyerName(name);
            order.setDeliveryAddress(address);
            order.setPhoneNumber(phone);
            order.setTotalAmount((double) finalTotal);
            order.setCreatedAt(System.currentTimeMillis());
            order.setStatus("pending");
            order.setNote(note);

            List<OrderDetails> items = new ArrayList<>();
            items.add(new OrderDetails(pName, pQty, (double) pPrice, pVariant));
            order.setItems(items);

            transaction.set(db.collection("orders").document(autoOrderId), order);
            transaction.update(orderCountRef, "currentValue", nextOrderVal);
            transaction.update(buyerCountRef, "currentValue", nextBuyerVal);

            return autoOrderId;
        }).addOnSuccessListener(orderId -> {
            Toast.makeText(this, "Đặt hàng thành công! ID: " + orderId, Toast.LENGTH_LONG).show();

            // Xóa giỏ hàng
            CartManager.getInstance().clearCart();

            // Chuyển đến màn hình Lịch sử đơn hàng
            Intent intent = new Intent(CheckoutActivity.this, ListOrderActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }
}