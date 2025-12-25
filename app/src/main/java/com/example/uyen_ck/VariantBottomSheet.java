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

    // 1. Hàm khởi tạo nhận ĐẦY ĐỦ thông tin để lưu vào Firebase
    public static VariantBottomSheet newInstance(String productId, String productName, String productImage, int quantity, long price, String actionType) {
        VariantBottomSheet sheet = new VariantBottomSheet();
        Bundle args = new Bundle();
        args.putString("productId", productId);     // ID để kiểm tra trùng
        args.putString("productName", productName);
        args.putString("productImage", productImage); // Ảnh để hiển thị trong giỏ
        args.putInt("quantity", quantity);
        args.putLong("price", price);
        args.putString("actionType", actionType);   // "add_to_cart" hoặc "buy_now"
        sheet.setArguments(args);
        return sheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Ánh xạ layout chọn màu/size
        View view = inflater.inflate(R.layout.variant_color, container, false);

        db = FirebaseFirestore.getInstance();

        // Ánh xạ các nút chọn phân loại (Màu sắc/Size)
        TextView option1 = view.findViewById(R.id.option1);
        TextView option2 = view.findViewById(R.id.option2);
        TextView option3 = view.findViewById(R.id.option3);
        TextView option4 = view.findViewById(R.id.option4);

        // Sự kiện click chọn option
        View.OnClickListener listener = v -> {
            if (selectedView != null) {
                // Reset nút cũ về trạng thái bình thường
                selectedView.setBackgroundResource(R.drawable.bg_variant_selector);
                selectedView.setTextColor(Color.BLACK);
            }
            // Highlight nút mới được chọn
            ((TextView) v).setBackgroundResource(R.drawable.bg_button_pink);
            ((TextView) v).setTextColor(Color.WHITE);
            selectedView = (TextView) v;
        };

        // Gán sự kiện cho các nút (Bạn có thể thêm option3, option4 nếu layout có)
        option1.setOnClickListener(listener);
        option2.setOnClickListener(listener);
        if (option3 != null) option3.setOnClickListener(listener);
        if (option4 != null) option4.setOnClickListener(listener);

        // Mặc định chọn cái đầu tiên
        option1.performClick();

        // Nút Đóng & Xong
        view.findViewById(R.id.btnClose).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.btnDone).setOnClickListener(v -> onConfirm());

        return view;
    }

    private void onConfirm() {
        if (selectedView == null) {
            Toast.makeText(requireContext(), "Vui lòng chọn phân loại", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedVariant = selectedView.getText().toString(); // Lấy text phân loại (VD: "50ml")
        Bundle args = getArguments();

        if (args != null) {
            String id = args.getString("productId");
            String name = args.getString("productName");
            String img = args.getString("productImage");
            int qty = args.getInt("quantity");
            long price = args.getLong("price");
            String actionType = args.getString("actionType");

            if ("buy_now".equals(actionType)) {
                // --- MUA NGAY: Chuyển thẳng sang thanh toán ---
                Intent intent = new Intent(requireContext(), CheckoutActivity.class);
                // Truyền dữ liệu sang trang thanh toán (nếu cần xử lý mua 1 món)
                // Lưu ý: CheckoutActivity cần code để nhận list hoặc 1 item riêng
                intent.putExtra("productName", name);
                intent.putExtra("variant", selectedVariant);
                intent.putExtra("quantity", qty);
                intent.putExtra("price", price);
                startActivity(intent);
                dismiss();
            } else {
                // --- THÊM VÀO GIỎ: Gọi hàm xử lý Firebase ---
                addToCartFirebase(id, name, img, selectedVariant, qty, price);
            }
        }
    }

    // ============================================================
    // LOGIC THÊM VÀO GIỎ HÀNG (FIRESTORE)
    // ============================================================
    private void addToCartFirebase(String id, String name, String img, String variant, int qty, double price) {
        // 1. Xác định User ID
        String userId = (FirebaseAuth.getInstance().getCurrentUser() != null)
                ? FirebaseAuth.getInstance().getCurrentUser().getUid() : "user_03"; // Mặc định user_03 để test

        DocumentReference docRef = db.collection("carts").document("cart_user_" + userId);

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            Cart cart;
            List<CartDetail> items = new ArrayList<>();

            if (documentSnapshot.exists()) {
                // Nếu giỏ hàng đã tồn tại -> Lấy dữ liệu cũ về
                cart = documentSnapshot.toObject(Cart.class);
                if (cart != null && cart.getItems() != null) {
                    items = cart.getItems();
                }
            } else {
                // Nếu chưa có -> Tạo giỏ hàng mới
                cart = new Cart();
                cart.setCartId("cart_" + userId);
                cart.setUserId(userId);
            }

            // 2. Kiểm tra sản phẩm đã có trong giỏ chưa
            boolean exists = false;
            for (CartDetail item : items) {
                // So sánh cả ID sản phẩm VÀ Phân loại (Variant)
                if (item.getProductId().equals(id) && item.getVariant().equals(variant)) {
                    // Đã có -> Cộng dồn số lượng
                    item.setQuantity(item.getQuantity() + qty);
                    exists = true;
                    break;
                }
            }

            // 3. Nếu chưa có -> Tạo item mới
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

            // 4. Cập nhật lại list items vào giỏ
            cart.setItems(items);
            cart.setUpdatedAt(System.currentTimeMillis());

            // 5. Đẩy ngược lên Firestore
            docRef.set(cart)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(requireContext(), "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                        dismiss(); // Đóng BottomSheet

                        // Tùy chọn: Chuyển sang màn hình Giỏ hàng để user thấy ngay
                        Intent intent = new Intent(requireContext(), CartActivity.class);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        dismiss();
                    });
        }).addOnFailureListener(e -> {
            Toast.makeText(requireContext(), "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            dismiss();
        });
    }
}