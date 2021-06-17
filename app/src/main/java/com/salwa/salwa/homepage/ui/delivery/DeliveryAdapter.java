package com.salwa.salwa.homepage.ui.delivery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.salwa.salwa.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder> {


    private final ArrayList<DeliveryModel> deliveryList = new ArrayList<>();
    public void setData(ArrayList<DeliveryModel> items) {
        deliveryList.clear();
        deliveryList.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public DeliveryViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delivery, parent, false);
        return new DeliveryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DeliveryViewHolder holder, int position) {
        holder.bind(deliveryList.get(position));
    }

    @Override
    public int getItemCount() {
        return deliveryList.size();
    }

    public static class DeliveryViewHolder extends RecyclerView.ViewHolder {

        ImageView productDp;

        TextView title, pembeli, waktu, totalItem, totalHarga;
        View view10;
        TextView deliveryStatus;

        CardView cardView4;

        public DeliveryViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title);
            productDp = view.findViewById(R.id.productDp);
            pembeli = view.findViewById(R.id.bookedBy);
            waktu = view.findViewById(R.id.waktu);
            totalItem = view.findViewById(R.id.totalProduct);
            totalHarga = view.findViewById(R.id.price);
            view10 = view.findViewById(R.id.view10);
            deliveryStatus = view.findViewById(R.id.deliveryStatus);
            cardView4 = view.findViewById(R.id.cardView4);
        }

        @SuppressLint("SetTextI18n")
        public void bind(DeliveryModel deliveryModel) {
            title.setText(deliveryModel.getTitle());
            pembeli.setText("Pembeli: " + deliveryModel.getBookedBy());
            waktu.setText("Waktu: "+deliveryModel.getAddedAt());
            totalItem.setText("Total item: " + deliveryModel.getTotalProduct());
            totalHarga.setText("Total harga: Rp."+deliveryModel.getPrice());

            Glide.with(itemView.getContext())
                    .load(deliveryModel.getProductDp())
                    .placeholder(R.drawable.ic_baseline_broken_image_24)
                    .error(R.drawable.ic_baseline_broken_image_24)
                    .into(productDp);

            if(deliveryModel.getDeliveryStatus().equals("Sudah Dikirim")) {
                view10.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.sudah_bayar));
                deliveryStatus.setText(deliveryModel.getDeliveryStatus());
            }


            cardView4.setOnClickListener(view -> {
                Intent intent = new Intent(itemView.getContext(), DetailDeliveryActivity.class);
                intent.putExtra(DetailDeliveryActivity.ITEM_DELIVERY, deliveryModel);
                itemView.getContext().startActivity(intent);
            });
        }
    }
}
