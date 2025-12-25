
package com.example.uyen_ck.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide; // Thêm dòng này để sửa lỗi "Cannot resolve symbol Glide"
import com.example.uyen_ck.ProductDetailActivity;
import com.example.uyen_ck.R;
import com.example.uyen_ck.models.Products;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Products> list;

    public ProductAdapter(Context context, List<Products> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(v);
    }

    // Cập nhật hàm onBindViewHolder trong ProductAdapter.java
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Products p = list.get(position);

        // Hiển thị tên và giá sản phẩm
        holder.tvName.setText(p.getName());
        holder.tvPrice.setText(String.format("%,.0f đ", p.getSalePrice()));


        // Sử dụng Glide để tải hình ảnh từ URL
        if (p.getImageUrl() != null && !p.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(p.getImageUrl())
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_placeholder) // Ảnh hiển thị nếu lỗi tải
                    .into(holder.imgProduct);
        } else {
            holder.imgProduct.setImageResource(R.drawable.img_placeholder);
        }

        // Xử lý sự kiện click chuyển sang trang chi tiết sản phẩm
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            // "product_id" phải khớp với Key nhận dữ liệu tại ProductDetailActivity

            intent.putExtra("product_id", p.getProductId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return (list != null) ? list.size() : 0;
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
        }
    }
}