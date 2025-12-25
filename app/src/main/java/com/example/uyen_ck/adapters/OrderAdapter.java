package com.example.uyen_ck.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uyen_ck.OrderDetailActivity;
import com.example.uyen_ck.R;
import com.example.uyen_ck.models.Order;
import com.example.uyen_ck.models.OrderDetails;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        if (order == null) return;

        // 1. Thông tin chung
        holder.tvOrderCode.setText("Mã: " + (order.getOrderCode() != null ? order.getOrderCode() : "---"));

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("vi", "VN"));
            holder.tvOrderDate.setText(sdf.format(new Date(order.getCreatedAt())));

            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            holder.tvTotalAmount.setText(formatter.format(order.getTotalAmount()));
        } catch (Exception e) {
            holder.tvTotalAmount.setText(order.getTotalAmount() + " đ");
        }

        // 2. Trạng thái
        String status = order.getStatus();
        String displayStatus = status;
        int color = Color.BLACK;

        if ("pending".equalsIgnoreCase(status) || "Pending".equalsIgnoreCase(status)) {
            displayStatus = "Chờ xác nhận"; color = Color.parseColor("#FF9800");
        } else if ("shipping".equalsIgnoreCase(status)) {
            displayStatus = "Đang giao"; color = Color.parseColor("#2196F3");
        } else if ("completed".equalsIgnoreCase(status)) {
            displayStatus = "Đã giao"; color = Color.parseColor("#4CAF50");
        }
        holder.tvStatus.setText(displayStatus);
        holder.tvStatus.setTextColor(color);

        // 3. HIỂN THỊ DANH SÁCH SẢN PHẨM (DYNAMIC VIEWS)
        // Xóa các view cũ để tránh bị trùng lặp khi cuộn
        holder.llProductContainer.removeAllViews();

        List<OrderDetails> items = order.getItems();
        if (items != null && !items.isEmpty()) {
            for (Object itemObj : items) {
                // Tạo view con từ layout item_order_product_row.xml
                View itemView = LayoutInflater.from(context).inflate(R.layout.item_order_product_row, holder.llProductContainer, false);

                ImageView imgItem = itemView.findViewById(R.id.imgProductItem);
                TextView tvName = itemView.findViewById(R.id.tvProductItemName);
                TextView tvQty = itemView.findViewById(R.id.tvProductItemQty);

                String name = "Sản phẩm";
                String imgUrl = "";
                int qty = 1;

                // Lấy dữ liệu (Xử lý cả trường hợp Map và Object)
                if (itemObj instanceof OrderDetails) {
                    OrderDetails detail = (OrderDetails) itemObj;
                    name = detail.getProductName();
                    imgUrl = detail.getProductImage();
                    qty = detail.getQuantity();
                } else if (itemObj instanceof Map) {
                    Map<String, Object> map = (Map<String, Object>) itemObj;
                    if (map.containsKey("productName")) name = (String) map.get("productName");
                    if (map.containsKey("productImage")) imgUrl = (String) map.get("productImage");
                    if (map.containsKey("quantity")) qty = Integer.parseInt(String.valueOf(map.get("quantity")));
                }

                // Gán dữ liệu vào view con
                tvName.setText(name);
                tvQty.setText("x" + qty);
                if (imgUrl != null && !imgUrl.isEmpty()) {
                    Glide.with(context).load(imgUrl).into(imgItem);
                }

                // Thêm view con vào container cha
                holder.llProductContainer.addView(itemView);
            }
        }

        // 4. Sự kiện Click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("orderId", order.getOrderId());
            context.startActivity(intent);
        });

        if(holder.btnBuyAgain != null) {
            holder.btnBuyAgain.setOnClickListener(v -> {
                // Xử lý mua lại
            });
        }
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderCode, tvOrderDate, tvStatus, tvTotalAmount;
        LinearLayout llProductContainer; // Đây là cái Container chứa list sản phẩm
        LinearLayout btnBuyAgain;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderCode = itemView.findViewById(R.id.tvOrderCode);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);

            // Ánh xạ Container thay vì các view lẻ tẻ cũ
            llProductContainer = itemView.findViewById(R.id.llProductContainer);

            btnBuyAgain = itemView.findViewById(R.id.btnBuyAgain);
        }
    }
}