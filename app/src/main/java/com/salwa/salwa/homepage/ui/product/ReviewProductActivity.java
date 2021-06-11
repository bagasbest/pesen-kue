package com.salwa.salwa.homepage.ui.product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.salwa.salwa.databinding.ActivityReviewProductBinding;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ReviewProductActivity extends AppCompatActivity {

    private ActivityReviewProductBinding binding;
    private ReviewViewModel reviewViewModel;


    public static final String EXTRA_PRODUCT_NAME = "name";
    public static final String EXTRA_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewProductBinding.inflate(getLayoutInflater());
        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
        setContentView(binding.getRoot());

        // tambahkan judul dan ikon back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Review " + getIntent().getStringExtra(EXTRA_PRODUCT_NAME));
        actionBar.setDisplayHomeAsUpEnabled(true);


        // tampilkan semua review
        showReview();

        // menulis review
        writeAReview();

    }

    private void showReview() {
        // tampilkan seluruh review
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.reviewRv.setLayoutManager(linearLayoutManager);
        ReviewAdapter reviewAdapter = new ReviewAdapter();
        reviewAdapter.notifyDataSetChanged();
        binding.reviewRv.setAdapter(reviewAdapter);

        binding.progressBar.setVisibility(View.VISIBLE);
        reviewViewModel.setReviewList(getIntent().getStringExtra(EXTRA_ID));
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

    private void writeAReview() {
        binding.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reviewText = binding.review.getText().toString().trim();

                if (reviewText.isEmpty()) {
                    binding.review.setError("Ups, anda belum menulis apapun");
                    return;
                }

                // ambil nama penulis review
                binding.progressBar2.setVisibility(View.VISIBLE);
                binding.imageButton.setVisibility(View.GONE);
                FirebaseFirestore
                        .getInstance()
                        .collection("users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                // simpan review teks yang telah di tulis ke dalam database
                                saveReviewToDatabase(reviewText, documentSnapshot.get("name").toString());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                binding.progressBar2.setVisibility(View.GONE);
                                binding.imageButton.setVisibility(View.VISIBLE);
                                binding.review.getText().clear();
                                Toast.makeText(ReviewProductActivity.this, "Gagal mengambil nama penulis review", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void saveReviewToDatabase(String reviewText, String name) {

        String timeInMillis = String.valueOf(System.currentTimeMillis());

        // ambil tanggal hari ini dengan format: dd - MMM - yyyy, HH:mm:ss
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat getDate = new SimpleDateFormat("dd MMM yyyy, HH:mm:ss");
        String format = getDate.format(new Date());

        Map<String, Object> review = new HashMap<>();
        review.put("uid", timeInMillis);
        review.put("name", name);
        review.put("reviewTxt", reviewText);
        review.put("addedAt", format);


        FirebaseFirestore
                .getInstance()
                .collection("product")
                .document(getIntent().getStringExtra(EXTRA_ID))
                .collection("review")
                .document(timeInMillis)
                .set(review)
                .addOnSuccessListener(unused -> {
                    showReview();
                    binding.progressBar2.setVisibility(View.GONE);
                    binding.imageButton.setVisibility(View.VISIBLE);
                    binding.review.getText().clear();

                    Toast.makeText(ReviewProductActivity.this, "Berhasil menambahkan review", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    binding.progressBar2.setVisibility(View.GONE);
                    binding.imageButton.setVisibility(View.VISIBLE);
                    binding.review.getText().clear();

                    Toast.makeText(ReviewProductActivity.this, "Gagal menambahkan review", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}