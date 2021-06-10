package com.salwa.salwa.homepage.ui.product;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.salwa.salwa.databinding.ActivityReviewProductBinding;


public class ReviewProductActivity extends AppCompatActivity {

    private ActivityReviewProductBinding binding;

    public static final String EXTRA_PRODUCT_NAME = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ReviewViewModel reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
        binding = ActivityReviewProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // tambahkan judul dan ikon back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Review " + getIntent().getStringExtra(EXTRA_PRODUCT_NAME));
        actionBar.setDisplayHomeAsUpEnabled(true);


        // tampilkan review
        binding.reviewRv.setLayoutManager(new LinearLayoutManager(this));
        ReviewAdapter reviewAdapter = new ReviewAdapter();
        reviewAdapter.notifyDataSetChanged();
        binding.reviewRv.setAdapter(reviewAdapter);

        binding.progressBar.setVisibility(View.VISIBLE);
        reviewViewModel.setReviewList();
        reviewViewModel.getReviewList().observe(this, reviewList -> {
            if (reviewList.size() > 0) {
                binding.noData.setVisibility(View.GONE);
                binding.reviewRv.setVisibility(View.VISIBLE);
                reviewAdapter.setData(reviewList);
            } else {
                binding.noData.setVisibility(View.VISIBLE);
                binding.reviewRv.setVisibility(View.GONE);
            }
            binding.progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}