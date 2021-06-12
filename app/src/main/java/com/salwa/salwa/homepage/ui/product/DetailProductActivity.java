package com.salwa.salwa.homepage.ui.product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.salwa.salwa.R;
import com.salwa.salwa.databinding.ActivityDetailProductBinding;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailProductActivity extends AppCompatActivity {

    private ActivityDetailProductBinding binding;

    public static final String ITEM_PRODUCT = "item";
    private String id;
    private String title;
    private String description;
    private int price;
    private String dp;


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // tambahkan judul dan ikon back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Detail Produk");
        actionBar.setDisplayHomeAsUpEnabled(true);

        // dapatkan atribut dari produk yang di klik pengguna
        ProductModel pm = getIntent().getParcelableExtra(ITEM_PRODUCT);
        id = pm.getProductId();
        title = pm.getTitle();
        description = pm.getDescription();
        price = pm.getPrice();
        dp = pm.getImage();
        double likes = pm.getLikes();
        int personRated = pm.getPersonRated();

        binding.title.setText(title);
        binding.description.setText(description);
        binding.price.setText("Rp. " + price);
        binding.rating.setText(String.format("%.1f", likes) + " | " + personRated + " Penilaian");

        binding.imageView6.setVisibility(View.GONE);

        Glide.with(this)
                .load(pm.getImage())
                .placeholder(R.drawable.ic_baseline_broken_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(binding.productDp);


        // beri penilaian terhadap produk / rating
        giveRatingAboutProduct();

        // tambahkan produk ke keranjang
        addProductToCart();

        // lihat review produk ini
        showReviewProduct();


    }

    private void showReviewProduct() {
        binding.comment.setOnClickListener(view -> {
            Intent intent = new Intent(DetailProductActivity.this, ReviewProductActivity.class);
            intent.putExtra(ReviewProductActivity.EXTRA_PRODUCT_NAME, title);
            intent.putExtra(ReviewProductActivity.EXTRA_ID, id);
            startActivity(intent);
        });
    }

    private void addProductToCart() {
        binding.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTotalProductAdded();
            }
        });
    }


    private void giveRatingAboutProduct() {
        binding.ratingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRatingPopUp();
            }
        });
    }


    private void showTotalProductAdded() {
        Dialog dialog;
        Button btnAddToCart, btnDismiss;
        EditText etTotalProduct;
        ProgressBar pb;

        dialog = new Dialog(this);

        dialog.setContentView(R.layout.popup_cart);
        dialog.setCanceledOnTouchOutside(false);

        btnAddToCart = dialog.findViewById(R.id.addCart);
        btnDismiss = dialog.findViewById(R.id.dismissBtn);
        etTotalProduct = dialog.findViewById(R.id.totalProduct);
        pb = dialog.findViewById(R.id.progress_bar);


        btnDismiss.setOnClickListener(view -> dialog.dismiss());

        btnAddToCart.setOnClickListener(view -> {
            // jika pengguna menginputkan produk kosong atau 0
            if (etTotalProduct.getText().toString().trim().isEmpty() || Integer.parseInt(etTotalProduct.getText().toString().trim()) == 0) {
                Toast.makeText(DetailProductActivity.this, "Minimal 1 produk", Toast.LENGTH_SHORT).show();
                return;
            }

            // jika pengguna menginputkan produk >= 1
            pb.setVisibility(View.VISIBLE);
            int totalProduct = Integer.parseInt(etTotalProduct.getText().toString().trim());
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // total harga produk yang diambil
            int totalPrice = price * totalProduct;

            // ambil nama pembeli
            FirebaseFirestore
                    .getInstance()
                    .collection("users")
                    .document(uid)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            // sembunyikan progress bar untuk selesai loading
                            saveCartProductToDatabase(id, title, description, totalPrice, dp, uid, pb, totalProduct, documentSnapshot.get("name").toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            // sembunyikan progress bar untuk selesai loading
                            pb.setVisibility(View.GONE);
                            Toast.makeText(DetailProductActivity.this, "Gagal mendapatkan nama pembeli", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    private void showRatingPopUp() {
        Dialog dialog;
        Button btnSubmitRating, btnDismiss;
        RatingBar ratingBar;
        ProgressBar pb;

        dialog = new Dialog(this);

        dialog.setContentView(R.layout.popup_rating);
        dialog.setCanceledOnTouchOutside(false);


        btnSubmitRating = dialog.findViewById(R.id.submitRating);
        btnDismiss = dialog.findViewById(R.id.dismissBtn);
        ratingBar = dialog.findViewById(R.id.ratingBar);
        pb = dialog.findViewById(R.id.progress_bar);


        btnSubmitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(DetailProductActivity.this, String.valueOf(ratingBar.getRating()), Toast.LENGTH_SHORT).show();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                saveRatingInDatabase(ratingBar.getRating(), pb, uid, dialog);
            }
        });

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }


    private void saveCartProductToDatabase(
            String id,
            String title,
            String description,
            int totalPrice,
            String dp,
            String uid,
            ProgressBar pb,
            int totalProduct,
            String name) {

        // ambil tanggal hari ini dengan format: dd - MMM - yyyy, HH:mm:ss
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat getDate = new SimpleDateFormat("dd MMM yyyy, HH:mm:ss");
        String format = getDate.format(new Date());

        Map<String, Object> users = new HashMap<>();
        users.put("productId", id);
        users.put("bookedBy", name);
        users.put("userUid", uid);
        users.put("title", title);
        users.put("description", description);
        users.put("price", totalPrice);
        users.put("totalProduct", totalProduct);
        users.put("productDp", dp);
        users.put("addedAt", format);


        FirebaseFirestore
                .getInstance()
                .collection("cart")
                .document(String.valueOf(System.currentTimeMillis()))
                .set(users)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // sembunyikan progress bar untuk selesai loading
                        pb.setVisibility(View.GONE);
                        Toast.makeText(DetailProductActivity.this, "Berhasil menambahkan produk ke keranjang", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        // sembunyikan progress bar untuk selesai loading
                        pb.setVisibility(View.GONE);
                        Toast.makeText(DetailProductActivity.this, "Ups, tidak berhasil menambahkan produk ke keranjang", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void saveRatingInDatabase(float rating, ProgressBar pb, String uid, Dialog dialog) {

        Map<String, Object> rate = new HashMap<>();
        rate.put("uid", uid);
        rate.put("rating", rating);


        // simpan rating pengguna ke database
        pb.setVisibility(View.VISIBLE);
        FirebaseFirestore
                .getInstance()
                .collection("product")
                .document(id)
                .collection("rating")
                .document(uid)
                .set(rate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        pb.setVisibility(View.GONE);
                        dialog.dismiss();
                        Toast.makeText(DetailProductActivity.this, "Berhasil memberi penilaian", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        pb.setVisibility(View.GONE);
                        dialog.dismiss();
                        Toast.makeText(DetailProductActivity.this, "Gagal memberi penilaian", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}