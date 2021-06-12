package com.salwa.salwa.homepage.ui.cart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.salwa.salwa.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {


    private final ArrayList<CartModel> cartList = new ArrayList<>();
    public void setData(ArrayList<CartModel> items) {
        cartList.clear();
        cartList.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CartViewHolder holder, int position) {
        holder.bind(cartList.get(position));
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvName, tvTime, tvTotalProduct, tvTotalPrice;
        ImageView imgProduct;
        View view;

        public CartViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.title);
            tvName = itemView.findViewById(R.id.bookedBy);
            tvTime = itemView.findViewById(R.id.bookedTime);
            tvTotalProduct = itemView.findViewById(R.id.totalProduct);
            tvTotalPrice = itemView.findViewById(R.id.price);
            imgProduct = itemView.findViewById(R.id.productDp);
            view = itemView.findViewById(R.id.view2);
        }

        @SuppressLint("SetTextI18n")
        public void bind(CartModel cartModel) {
            tvTitle.setText(cartModel.getTitle());
            tvName.setText("Pembeli: " + cartModel.getBookedBy());
            tvTime.setText("Waktu: " + cartModel.getAddedAt());
            tvTotalPrice.setText("Total Harga: Rp. " + cartModel.getPrice());
            tvTotalProduct.setText("Total pembelian: " + cartModel.getTotalProduct() + " Produk");

            Glide.with(itemView.getContext())
                    .load(cartModel.getProductDp())
                    .placeholder(R.drawable.ic_baseline_broken_image_24)
                    .error(R.drawable.ic_baseline_broken_image_24)
                    .into(imgProduct);

            view.setOnClickListener(view -> {
                Intent intent = new Intent(itemView.getContext(), DetailCartActivity.class);
                intent.putExtra(DetailCartActivity.ITEM_CART, cartModel);
                itemView.getContext().startActivity(intent);
            });

        }
    }
}
