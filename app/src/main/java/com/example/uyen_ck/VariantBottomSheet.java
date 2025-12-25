package com.example.uyen_ck;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class VariantBottomSheet extends BottomSheetDialogFragment {
    private static final String TAG = "VariantBottomSheet";

    private TextView selectedView = null;
    private Products product;
    private int quantity;
    private String actionType;

    // Khởi tạo instance mới với Products object
    public static VariantBottomSheet newInstance(Products product, int quantity, String actionType) {
        VariantBottomSheet sheet = new VariantBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable("product", product);
        args.putInt("quantity", quantity);
        args.putString("actionType", actionType);
        sheet.setArguments(args);
        return sheet;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = (Products) getArguments().getSerializable("product");
            quantity = getArguments().getInt("quantity", 1);
            actionType = getArguments().getString("actionType", "add_to_cart");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.variant_color, container, false);

        TextView option1 = view.findViewById(R.id.option1);
        TextView option2 = view.findViewById(R.id.option2);
        TextView option3 = view.findViewById(R.id.option3);
        TextView option4 = view.findViewById(R.id.option4);

        // Thiết lập text cho các variant
        option1.setText("50ml");
        option2.setText("100ml");
        option3.setText("Hồng");
        option4.setText("Trắng");

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
        option3.setOnClickListener(listener);
        option4.setOnClickListener(listener);

        // Mặc định chọn option đầu tiên
        option1.performClick();

        // Nút đóng
        view.findViewById(R.id.btnClose).setOnClickListener(v -> dismiss());

        // Nút xác nhận
        view.findViewById(R.id.btnDone).setOnClickListener(v -> onConfirm());

        return view;
    }

    private void onConfirm() {
        if (selectedView == null || product == null) {
            Toast.makeText(requireContext(), "Vui lòng chọn phân loại", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedVariant = selectedView.getText().toString();

        if ("buy_now".equals(actionType)) {
            // Chuyển đến màn hình thanh toán
            Intent intent = new Intent(requireContext(), CheckoutActivity.class);
            intent.putExtra("product", product);
            intent.putExtra("variant", selectedVariant);
            intent.putExtra("quantity", quantity);
            intent.putExtra("isDirectPurchase", true);
            startActivity(intent);
        } else {
            // Gọi phương thức addToCart từ ProductDetailActivity
            if (getActivity() instanceof ProductDetailActivity) {
                ((ProductDetailActivity) getActivity()).addToCart(product, quantity, selectedVariant);
            }
        }
        dismiss();
    }
}