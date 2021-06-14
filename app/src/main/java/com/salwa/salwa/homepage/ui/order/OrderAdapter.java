package com.salwa.salwa.homepage.ui.order;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.salwa.salwa.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {


    private final ArrayList<OrderModel> orderList = new ArrayList<>();
    public void setData(ArrayList<OrderModel> items) {
        orderList.clear();
        orderList.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OrderViewHolder holder, int position) {
        holder.bind(orderList.get(position));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        View view2, view3;
        TextView title, name, timeOrdered, totalProduct, price, paymentStatus;
        ImageView productDp;

        public OrderViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            view2 = itemView.findViewById(R.id.view2);
            view3 = itemView.findViewById(R.id.view3);
            name = itemView.findViewById(R.id.bookedBy);
            timeOrdered = itemView.findViewById(R.id.bookedTime);
            totalProduct = itemView.findViewById(R.id.totalProduct);
            price = itemView.findViewById(R.id.price);
            paymentStatus = itemView.findViewById(R.id.paymentStatus);
            productDp = itemView.findViewById(R.id.productDp);
        }

        @SuppressLint("SetTextI18n")
        public void bind(OrderModel orderModel) {
            title.setText(orderModel.getTitle());
            name.setText("Nama: " + orderModel.getBookedBy());
            timeOrdered.setText("Waktu: " + orderModel.getAddedAt());
            totalProduct.setText("Total pembelian: " + orderModel.getTotalProduct());
            price.setText("Total harga: Rp." + orderModel.getPrice());

            Glide.with(itemView.getContext())
                    .load(orderModel.getProductDp())
                    .placeholder(R.drawable.ic_baseline_broken_image_24)
                    .error(R.drawable.ic_baseline_broken_image_24)
                    .into(productDp);

            if(orderModel.getStatus().equals("Sudah Bayar")) {
                view3.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.sudah_bayar));
                paymentStatus.setText("Status: " + orderModel.getStatus());
            } else if (orderModel.getStatus().equals("Dalam Proses")) {
                view3.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.dalam_proses));
                paymentStatus.setText("Status: " + orderModel.getStatus());

            }

            view2.setOnClickListener(view -> {
                Intent intent = new Intent(itemView.getContext(), DetailOrderActivity.class);
                intent.putExtra(DetailOrderActivity.ITEM_ORDER, orderModel);
                itemView.getContext().startActivity(intent);
            });
        }
    }
}
