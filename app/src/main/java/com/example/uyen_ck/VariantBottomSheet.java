package com.example.uyen_ck;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.uyen_ck.models.Cart;
import com.example.uyen_ck.models.CartDetail;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class VariantBottomSheet extends BottomSheetDialogFragment {

    private TextView selectedView = null;
    private FirebaseFirestore db;

    // Cập nhật: Thêm productId và productImage vào tham số
    public static VariantBottomSheet newInstance(String productId, String productName, String productImage, int quantity, long price, String actionType) {
        VariantBottomSheet sheet = new VariantBottomSheet();
        Bundle args = new Bundle();
        args.putString("productId", productId); // Mới
        args.putString("productName", productName);
        args.putString("productImage", productImage); // Mới
        args.putInt("quantity", quantity);
        args.putLong("price", price);
        args.putString("actionType", actionType);
        sheet.setArguments(args);
        return sheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.variant_color, container, false);
        db = FirebaseFirestore.getInstance();

        // ... (Phần ánh xạ Option 1, 2, 3... giữ nguyên như cũ)
        TextView option1 = view.findViewById(R.id.option1);
        TextView option2 = view.findViewById(R.id.option2);
        // ... (Code xử lý click đổi màu giữ nguyên) ...

        View.OnClickListener listener = v -> {
            if (selectedView != null) {
                selectedView.setBackgroundResource(R.drawable.bg_variant_selector);
                selectedView.setTextColor(Color.BLACK);
            }
            ((TextView) v).setBackgroundResource(R.drawable.bg_button_pink);
            ((TextView) v).setTextColor(Color.WHITE);
            selectedView = (TextView) v;
        };

        option1.setOnClickListener(listener);
        option2.setOnClickListener(listener);
        // Ánh xạ thêm option3, option4 nếu có...

        option1.performClick(); // Mặc định chọn

        view.findViewById(R.id.btnClose).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.btnDone).setOnClickListener(v -> onConfirm());

        return view;
    }

    private void onConfirm() {
        if (selectedView == null) {
            Toast.makeText(requireContext(), "Vui lòng chọn phân loại", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedVariant = selectedView.getText().toString();
        Bundle args = getArguments();

        if (args != null) {
            String id = args.getString("productId");
            String name = args.getString("productName");
            String img = args.getString("productImage");
            int qty = args.getInt("quantity");
            long price = args.getLong("price");
            String actionType = args.getString("actionType");

            if ("buy_now".equals(actionType)) {
                // Xử lý Mua ngay (Giữ nguyên)
                Intent intent = new Intent(requireContext(), CheckoutActivity.class);
                intent.putExtra("productName", name);
                intent.putExtra("variant", selectedVariant);
                intent.putExtra("quantity", qty);
                intent.putExtra("price", price);
                startActivity(intent);
                dismiss();
            } else {
                // Xử lý Thêm vào giỏ -> ĐẨY LÊN FIREBASE
                addToCartFirebase(id, name, img, selectedVariant, qty, price);
            }
        }
    }

    // --- HÀM QUAN TRỌNG NHẤT: Thêm vào Firestore ---
    private void addToCartFirebase(String id, String name, String img, String variant, int qty, double price) {
        // Lấy ID user (hoặc dùng cứng user_03 để test)
        String userId = (FirebaseAuth.getInstance().getCurrentUser() != null)
                ? FirebaseAuth.getInstance().getCurrentUser().getUid() : "user_03";

        DocumentReference docRef = db.collection("carts").document("cart_user_" + userId);

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            Cart cart;
            List<CartDetail> items = new ArrayList<>();

            if (documentSnapshot.exists()) {
                // Giỏ hàng đã có -> Lấy danh sách cũ về
                cart = documentSnapshot.toObject(Cart.class);
                if (cart != null && cart.getItems() != null) {
                    items = cart.getItems();
                }
            } else {
                // Chưa có giỏ hàng -> Tạo mới
                cart = new Cart();
                cart.setCartId("cart_" + userId);
                cart.setUserId(userId);
            }

            // Kiểm tra xem sản phẩm này + variant này đã có trong giỏ chưa
            boolean exists = false;
            for (CartDetail item : items) {
                if (item.getProductId().equals(id) && item.getVariant().equals(variant)) {
                    // Đã có -> Cộng dồn số lượng
                    item.setQuantity(item.getQuantity() + qty);
                    exists = true;
                    break;
                }
            }

            // Nếu chưa có -> Tạo item mới thêm vào list
            if (!exists) {
                CartDetail newItem = new CartDetail();
                newItem.setProductId(id);
                newItem.setProductName(name);
                newItem.setProductImage(img);
                newItem.setVariant(variant);
                newItem.setQuantity(qty);
                newItem.setPrice(price);
                items.add(newItem);
            }

            // Cập nhật lại list vào object Cart
            cart.setItems(items);
            cart.setUpdatedAt(System.currentTimeMillis());

            // Đẩy ngược lên Firebase
            docRef.set(cart)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(requireContext(), "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                        dismiss(); // Đóng BottomSheet
                        // Mở giỏ hàng để user thấy ngay (Tuỳ chọn)
                        Intent intent = new Intent(requireContext(), CartActivity.class);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        dismiss();
                    });
        });
    }
}