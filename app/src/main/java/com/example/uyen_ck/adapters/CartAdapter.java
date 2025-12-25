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

    // Constructor đã được dọn dẹp
    public CartAdapter(List<CartDetail> list, OnCartUpdateListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Ánh xạ đúng layout item_cart.xml
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartDetail item = list.get(position);

        // Đổ dữ liệu vào view
        holder.tvName.setText(item.getProductName());
        holder.tvBrand.setText(item.getVariant() != null ? item.getVariant() : "Mặc định");
        holder.tvQty.setText(String.valueOf(item.getQuantity()));

        // Định dạng tiền tệ (Ví dụ: 100,000đ)
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        holder.tvPrice.setText(formatter.format(item.getPrice()) + "đ");

        // Tải ảnh sản phẩm
        Glide.with(holder.itemView.getContext())
                .load(item.getProductImage())
                .placeholder(R.drawable.lo_roche_posay)
                .error(R.drawable.lo_roche_posay)
                .into(holder.imgProduct);

        // --- Xử lý sự kiện ---

        // 1. Tăng số lượng
        holder.btnPlus.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            holder.tvQty.setText(String.valueOf(item.getQuantity()));
            if (listener != null) listener.onUpdate(list);
        });

        // 2. Giảm số lượng
        holder.btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                holder.tvQty.setText(String.valueOf(item.getQuantity()));
                if (listener != null) listener.onUpdate(list);
            }
        });

        // 3. Xóa sản phẩm
        holder.btnDelete.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                list.remove(currentPosition);
                notifyItemRemoved(currentPosition);
                notifyItemRangeChanged(currentPosition, list.size());
                if (listener != null) listener.onUpdate(list);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (list != null) ? list.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvPrice, tvQty, tvBrand;
        ImageButton btnPlus, btnMinus, btnDelete;
        CheckBox cbSelectItem;

        ViewHolder(View v) {
            super(v);
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