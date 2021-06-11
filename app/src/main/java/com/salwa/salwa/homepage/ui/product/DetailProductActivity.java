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
import com.google.firebase.firestore.FirebaseFirestore;
import com.salwa.salwa.R;
import com.salwa.salwa.databinding.ActivityAddProductBinding;
import com.salwa.salwa.databinding.ActivityDetailProductBinding;

import org.jetbrains.annotations.NotNull;

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
    private int likes = 0;


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
        likes = pm.getLikes();


        binding.title.setText(title);
        binding.description.setText(description);
        binding.price.setText("Rp. " + price);
        binding.rating.setText(String.format("%.1f", likes / 5.0) + " | " + likes + " Kali terjual");

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
        binding.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailProductActivity.this, ReviewProductActivity.class);
                intent.putExtra(ReviewProductActivity.EXTRA_PRODUCT_NAME, title);
                intent.putExtra(ReviewProductActivity.EXTRA_ID, id);
                startActivity(intent);
            }
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


        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String totalProduct = etTotalProduct.getText().toString().trim();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


                // simpan produk yang ada di keranjang ke database
                pb.setVisibility(View.VISIBLE);
                saveCartProductToDatabase(id, title, description, price, dp, uid, pb);
            }
        });
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

                saveRatingInDatabase(ratingBar.getRating(), pb);
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


    private void saveCartProductToDatabase(String id, String title, String description, int price, String dp, String uid, ProgressBar pb) {

        Map<String, Object> users = new HashMap<>();
        users.put("productId", id);
        users.put("bookedBy", uid);
        users.put("title", title);
        users.put("description", description);
        users.put("price", price);
        users.put("productDp", dp);


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
                        Toast.makeText(DetailProductActivity.this, "Berhasil menambahkan produk baru", Toast.LENGTH_SHORT).show();
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

    private void saveRatingInDatabase(float rating, ProgressBar pb) {
        // simpan rating pengguna ke database

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