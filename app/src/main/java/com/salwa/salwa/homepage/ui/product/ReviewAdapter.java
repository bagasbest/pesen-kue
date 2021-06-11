package com.salwa.salwa.homepage.ui.product;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.salwa.salwa.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private final ArrayList<ReviewModel> reviewList = new ArrayList<>();
    public void setData(ArrayList<ReviewModel> items) {
        reviewList.clear();
        reviewList.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ReviewViewHolder holder, int position) {
        holder.bind(reviewList.get(position));
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvReviewText, addedAt;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.name);
            tvReviewText = itemView.findViewById(R.id.reviewTxt);
            addedAt = itemView.findViewById(R.id.addedAt);
        }

        public void bind(ReviewModel reviewModel) {

            tvName.setText(reviewModel.getName());
            tvReviewText.setText(reviewModel.getReviewText());
            addedAt.setText(reviewModel.getTimeStamp());

        }
    }
}
