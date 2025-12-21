package com.example.baitapdemo;

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
        option1.performClick(); // Mặc định chọn cái đầu

        view.findViewById(R.id.btnClose).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.btnDone).setOnClickListener(v -> {
            if (selectedView != null) {
                Toast.makeText(requireContext(), "Đã chọn: " + selectedView.getText(), Toast.LENGTH_SHORT).show();
            }
            dismiss();
        });

        return view;
    }
}