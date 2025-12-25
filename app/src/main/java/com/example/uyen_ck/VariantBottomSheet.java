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

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class VariantBottomSheet extends BottomSheetDialogFragment {
    public interface OnVariantConfirmListener {
        void onAddToCart(String variant, int quantity);
        void onBuyNow(String variant, int quantity);
    }
    private OnVariantConfirmListener listener;
    public void setOnVariantConfirmListener(OnVariantConfirmListener listener) {
        this.listener = listener;
    }
    private TextView selectedView = null;
    public static VariantBottomSheet newInstance(String productName, int quantity, long price, String actionType) {
        VariantBottomSheet sheet = new VariantBottomSheet();
        Bundle args = new Bundle();
        args.putString("productName", productName);
        args.putInt("quantity", quantity);
        args.putLong("price", price);
        args.putString("actionType", actionType);
        sheet.setArguments(args);
        return sheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Sử dụng layout variant_color.xml đã có trong res
        View view = inflater.inflate(R.layout.variant_color, container, false);

        TextView option1 = view.findViewById(R.id.option1);
        TextView option2 = view.findViewById(R.id.option2);
        TextView option3 = view.findViewById(R.id.option3);
        TextView option4 = view.findViewById(R.id.option4);

        View.OnClickListener listener = v -> {
            if (selectedView != null) {
                // Trả lại background mặc định khi bỏ chọn
                selectedView.setBackgroundResource(R.drawable.bg_variant_selector);
                selectedView.setTextColor(Color.BLACK);
            }

            // Đổi màu khi được chọn
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

        view.findViewById(R.id.btnClose).setOnClickListener(v -> dismiss());

        // Nút xác nhận
        view.findViewById(R.id.btnDone).setOnClickListener(v -> onConfirm());

        return view;
    }

    // Trong VariantBottomSheet.java
    private void onConfirm() {
        if (selectedView == null) {
            Toast.makeText(requireContext(), "Vui lòng chọn phân loại", Toast.LENGTH_SHORT).show();
            return;
        }

        if (listener == null) return;

        String variant = selectedView.getText().toString();
        Bundle args = getArguments();
        if (args == null) return;

        int quantity = args.getInt("quantity");
        String actionType = args.getString("actionType");

        if ("buy_now".equals(actionType)) {
            listener.onBuyNow(variant, quantity);
        } else {
            listener.onAddToCart(variant, quantity);
        }

        dismiss();
    }

}