package com.salwa.salwa.homepage.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.salwa.salwa.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final ArrayList<ProductModel> productList = new ArrayList<>();
    public void setData(ArrayList<ProductModel> items) {
        productList.clear();
        productList.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductViewHolder holder, int position) {
        holder.bind(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView productDp;
        TextView titleTv, shopTv, priceTv, ratingTv;
        ConstraintLayout card;


        public ProductViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            productDp = itemView.findViewById(R.id.roundedImageView);
            titleTv = itemView.findViewById(R.id.title);
            shopTv = itemView.findViewById(R.id.shop);
            priceTv = itemView.findViewById(R.id.price);
            ratingTv = itemView.findViewById(R.id.rating);
            card = itemView.findViewById(R.id.itemProduct);
        }

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        public void bind(ProductModel productModel) {

            titleTv.setText(productModel.getTitle());
            shopTv.setText(productModel.getShopName());
            priceTv.setText("Rp. " + productModel.getPrice());
            ratingTv.setText(String.format("%.1f", productModel.getLikes()) + " | " + productModel.getPersonRated() +" Penilaian");

            Glide.with(itemView.getContext())
                    .load(productModel.getImage())
                    .error(R.drawable.ic_baseline_broken_image_24)
                    .into(productDp);


            card.setOnClickListener(view -> {
                Intent intent = new Intent(itemView.getContext(), DetailProductActivity.class);
                intent.putExtra(DetailProductActivity.ITEM_PRODUCT, productModel);
                itemView.getContext().startActivity(intent);
            });


        }
    }
}
