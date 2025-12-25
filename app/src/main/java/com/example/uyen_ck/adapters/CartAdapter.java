package com.example.uyen_ck.adapters;

import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.uyen_ck.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<CartDetail> list;
    private OnCartUpdateListener listener;

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
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        CartDetail item = list.get(position);

        h.tvName.setText(item.getProductName());
        h.tvPrice.setText(String.format("%,.0fđ", item.getPrice()));
        h.tvQty.setText(String.valueOf(item.getQuantity()));
        h.tvBrand.setText(item.getVariant());

        Glide.with(h.itemView.getContext())
                .load(item.getProductImage())
                .placeholder(R.drawable.lo_roche_posay)
                .into(h.imgProduct);

        h.btnPlus.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            listener.onUpdate(list);
            notifyItemChanged(position);
        });

        h.btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                listener.onUpdate(list);
                notifyItemChanged(position);
            }
        });

        h.btnDelete.setOnClickListener(v -> {
            list.remove(position);
            listener.onUpdate(list);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvPrice, tvQty, tvBrand;
        ImageButton btnPlus, btnMinus, btnDelete;

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
        }
    }
}
