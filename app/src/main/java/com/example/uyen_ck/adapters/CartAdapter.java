package com.example.uyen_ck.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uyen_ck.R;
import com.example.uyen_ck.models.CartDetail;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<CartDetail> list;
    private OnCartUpdateListener listener;

    // Interface để giao tiếp với Activity
    public interface OnCartUpdateListener {
        void onUpdate(List<CartDetail> newList);
    }

    public CartAdapter(List<CartDetail> list, OnCartUpdateListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
<<<<<<< HEAD
        // Ánh xạ đúng layout item_cart.xml bạn cung cấp (dùng CardView)
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
=======
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
>>>>>>> dafe6fcdc786aed43ce3e370a79299ea3ab289a5
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        CartDetail item = list.get(position);

<<<<<<< HEAD
        holder.tvName.setText(item.getProductName());
        holder.tvBrand.setText(item.getVariant() != null ? item.getVariant() : "Mặc định");
        holder.tvQty.setText(String.valueOf(item.getQuantity()));

        // Format giá tiền (Ví dụ: 100.000đ)
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        holder.tvPrice.setText(formatter.format(item.getPrice()) + "đ");

        // Load ảnh
        Glide.with(holder.itemView.getContext())
                .load(item.getProductImage())
                .placeholder(R.drawable.lo_roche_posay) // Ảnh mặc định nếu lỗi
                .error(R.drawable.lo_roche_posay)
                .into(holder.imgProduct);

        // --- Xử lý sự kiện ---

        // 1. Tăng số lượng
        holder.btnPlus.setOnClickListener(v -> {
=======
        h.tvName.setText(item.getProductName());
        h.tvPrice.setText(String.format("%,.0fđ", item.getPrice()));
        h.tvQty.setText(String.valueOf(item.getQuantity()));
        h.tvBrand.setText(item.getVariant());

        Glide.with(h.itemView.getContext())
                .load(item.getProductImage())
                .placeholder(R.drawable.lo_roche_posay)
                .into(h.imgProduct);

        h.btnPlus.setOnClickListener(v -> {
>>>>>>> dafe6fcdc786aed43ce3e370a79299ea3ab289a5
            item.setQuantity(item.getQuantity() + 1);
            holder.tvQty.setText(String.valueOf(item.getQuantity()));
            // Gửi danh sách mới về Activity để cập nhật Firestore & tính tổng tiền
            listener.onUpdate(list);
        });

<<<<<<< HEAD
        // 2. Giảm số lượng
        holder.btnMinus.setOnClickListener(v -> {
=======
        h.btnMinus.setOnClickListener(v -> {
>>>>>>> dafe6fcdc786aed43ce3e370a79299ea3ab289a5
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                holder.tvQty.setText(String.valueOf(item.getQuantity()));
                listener.onUpdate(list);
            }
        });

<<<<<<< HEAD
        // 3. Xóa sản phẩm
        holder.btnDelete.setOnClickListener(v -> {
=======
        h.btnDelete.setOnClickListener(v -> {
>>>>>>> dafe6fcdc786aed43ce3e370a79299ea3ab289a5
            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list.size());
            listener.onUpdate(list);
        });
    }

    @Override
    public int getItemCount() {
<<<<<<< HEAD
        return list != null ? list.size() : 0;
=======
        return list.size();
>>>>>>> dafe6fcdc786aed43ce3e370a79299ea3ab289a5
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvPrice, tvQty, tvBrand;
        ImageButton btnPlus, btnMinus, btnDelete;
        CheckBox cbSelectItem;

        ViewHolder(View v) {
            super(v);
            // Ánh xạ ID theo file item_cart.xml bạn cung cấp
            imgProduct = v.findViewById(R.id.imgProduct);
            tvName = v.findViewById(R.id.tvProductName);
            tvPrice = v.findViewById(R.id.tvProductPrice);
            tvQty = v.findViewById(R.id.tvQuantity);
            tvBrand = v.findViewById(R.id.tvBrandName);

            btnPlus = v.findViewById(R.id.btnPlus);
            btnMinus = v.findViewById(R.id.btnMinus);
            btnDelete = v.findViewById(R.id.btnDelete);
            cbSelectItem = v.findViewById(R.id.cbSelectItem);
        }
    }
}
