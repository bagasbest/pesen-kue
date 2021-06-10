package com.salwa.salwa.homepage.ui.product;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.salwa.salwa.R;
import com.salwa.salwa.databinding.ActivityAddProductBinding;
import com.salwa.salwa.databinding.ActivityDetailProductBinding;

public class DetailProductActivity extends AppCompatActivity {

    private ActivityDetailProductBinding binding;

    public static final String ITEM_PRODUCT = "item";


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
        String id = pm.getProductId();
        binding.title.setText(pm.getTitle());
        binding.description.setText(pm.getDescription());
        binding.price.setText("Rp. " + pm.getPrice());
        binding.rating.setText(String.format("%.1f", pm.getLikes() / 5.0) + " | " + pm.getLikes() +" Kali terjual");

        binding.imageView6.setVisibility(View.GONE);

        Glide.with(this)
                .load(pm.getImage())
                .placeholder(R.drawable.ic_baseline_broken_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(binding.productDp);





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