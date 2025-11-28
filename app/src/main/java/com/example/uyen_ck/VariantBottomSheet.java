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
        View view = inflater.inflate(R.layout.variant_color, container, false);

        TextView option1 = view.findViewById(R.id.option1);
        TextView option2 = view.findViewById(R.id.option2);
        TextView option3 = view.findViewById(R.id.option3);
        TextView option4 = view.findViewById(R.id.option4);

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

        option1.performClick(); // Mặc định chọn option 1 khi mở sheet

        // Nút đóng
        view.findViewById(R.id.btnClose).setOnClickListener(v -> dismiss());

        // Nút xác nhận
        view.findViewById(R.id.btnDone).setOnClickListener(v -> onConfirm());

        return view;
    }

    private void onConfirm() {
        if (selectedView == null) {
            Toast.makeText(requireContext(), "Vui lòng chọn phân loại", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedVariant = selectedView.getText().toString();

        Toast.makeText(requireContext(), "Đã chọn: " + selectedVariant, Toast.LENGTH_SHORT).show();

        // Chuyển sang CheckoutActivity
        Intent intent = new Intent(requireContext(), CheckoutActivity.class);
        intent.putExtra("variant", selectedVariant);

        // Gửi thêm dữ liệu sản phẩm nếu cần
        if (getArguments() != null) {
            intent.putExtra("productName", getArguments().getString("productName"));
            intent.putExtra("quantity", getArguments().getInt("quantity"));
            intent.putExtra("price", getArguments().getLong("price"));
        }

        startActivity(intent);

        dismiss(); // Đóng BottomSheet sau khi chuyển màn hình
    }
}
