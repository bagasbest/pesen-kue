package com.salwa.salwa.homepage.ui.order;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.salwa.salwa.R;
import com.salwa.salwa.databinding.ActivityDetailOrderBinding;
import com.salwa.salwa.homepage.ui.cart.CartModel;

public class DetailOrderActivity extends AppCompatActivity {

    private ActivityDetailOrderBinding binding;

    public static final String ITEM_ORDER = "order";
    private String addedAt;
    private String bookedBy;
    private String description;
    private int price;
    private String productDp;
    private String productId;
    private String address;
    private String phone;
    private String title;
    private int totalProduct;
    private String userUid;
    private String status;
    private String proofPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        binding = ActivityDetailOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // dapatkan atribut dari produk yang di klik pengguna
        OrderModel om = getIntent().getParcelableExtra(ITEM_ORDER);
        title = om.getTitle();

        // tambahkan judul dan ikon back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true);



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}