
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
public class Product {
    private String name, brand, imageUrl, status;
    private Object salePrice; // Dùng Object để nhận cả String/Number từ Firebase

    public Product() {}

    // Hàm lấy giá an toàn để hiển thị lên TextView
    public double getSafePrice() {
        try {
            if (salePrice instanceof Number) return ((Number) salePrice).doubleValue();
            if (salePrice instanceof String) return Double.parseDouble((String) salePrice);
        } catch (Exception e) { return 0.0; }
        return 0.0;
    }

    // Đủ Getter/Setter cho các trường: name, brand, imageUrl, status, salePrice
    public void setSalePrice(Object salePrice) { this.salePrice = salePrice; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public String getImageUrl() { return imageUrl; }
}
