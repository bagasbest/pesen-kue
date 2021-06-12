package com.salwa.salwa.homepage.ui.cart;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.salwa.salwa.R;
import com.salwa.salwa.databinding.ActivityDetailCartBinding;
import com.salwa.salwa.homepage.ui.product.ProductModel;

public class DetailCartActivity extends AppCompatActivity {

    private ActivityDetailCartBinding binding;

    public static final String ITEM_CART = "item";
    private String addedAt;
    private String bookedBy;
    private String description;
    private int price;
    private String productDp;
    private String productId;
    private String title;
    private int totalProduct;
    private String customerUid;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // dapatkan atribut dari produk yang di klik pengguna
        CartModel cm = getIntent().getParcelableExtra(ITEM_CART);
        title = cm.getTitle();

        // tambahkan judul dan ikon back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true);

        addedAt = cm.getAddedAt();
        bookedBy = cm.getBookedBy();
        description = cm.getDescription();
        price = cm.getPrice();
        productDp = cm.getProductDp();
        productId = cm.getProductId();
        totalProduct = cm.getTotalProduct();
        customerUid = cm.getUserUid();


        binding.name.setText("Nama : " + bookedBy);
        binding.title.setText(title);
        binding.description.setText(description);
        binding.price.setText("Total Harga: Rp. " + price);
        binding.totalProduct.setText("Total Pemesanan Produk: " + totalProduct);

        Glide.with(this)
                .load(productDp)
                .placeholder(R.drawable.ic_baseline_broken_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(binding.productDp);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}