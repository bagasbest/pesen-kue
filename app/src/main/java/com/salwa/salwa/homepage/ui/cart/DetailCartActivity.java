package com.salwa.salwa.homepage.ui.cart;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.salwa.salwa.R;
import com.salwa.salwa.databinding.ActivityDetailCartBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailCartActivity extends AppCompatActivity {

    private ActivityDetailCartBinding binding;

    public static final String ITEM_CART = "item";
    private String bookedBy;
    private String description;
    private int price;
    private String productDp;
    private String productId;
    private String title;
    private int totalProduct;
    private String customerUid;
    private String cartId;

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

        bookedBy = cm.getBookedBy();
        description = cm.getDescription();
        price = cm.getPrice();
        productDp = cm.getProductDp();
        productId = cm.getProductId();
        totalProduct = cm.getTotalProduct();
        customerUid = cm.getUserUid();
        cartId = cm.getCartId();


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


        // klik tombol pemesanan
        binding.btnOrder.setOnClickListener(view -> {
            // validasi pemesanan produk
            validateFormToOrder();
        });

    }

    @SuppressLint("ResourceType")
    private void validateFormToOrder() {
        // validasi form alamat, no telepon
        if(binding.addressEt.getText().toString().trim().isEmpty()) {
            binding.addressEt.setError("Alamat harus dicantumkan sedetail mungkin");
            return;
        }
        else if(binding.phoneEt.getText().toString().trim().isEmpty()) {
            binding.phoneEt.setError("No. Telepon harus dicantumkan");
            return;
        }

        // lakukan pemesanan dan pembayaran
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Pemesanan")
                .setMessage("Apakah anada yakin ingin melakukan pemesanan produk: " + title + " ?")
                .setPositiveButton("YA", (dialogInterface, i) -> {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    // order produk
                    orderProduct();
                })
                .setNegativeButton("TIDAK", null)
                .setIcon(R.drawable.ic_baseline_payment_24)
                .setIconAttribute(R.color.secondary)
                .show();
    }

    private void orderProduct() {
        // set time in millis
        String timeInMillis = String.valueOf(System.currentTimeMillis());

        // ambil tanggal hari ini dengan format: dd - MMM - yyyy, HH:mm:ss
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat getDate = new SimpleDateFormat("dd MMM yyyy, HH:mm:ss");
        String format = getDate.format(new Date());

        Map<String, Object> order = new HashMap<>();
        order.put("productId", productId);
        order.put("bookedBy", bookedBy);
        order.put("userUid", customerUid);
        order.put("address", binding.addressEt.getText().toString().trim());
        order.put("phone", binding.phoneEt.getText().toString().trim());
        order.put("title", title);
        order.put("description", description);
        order.put("price", price);
        order.put("totalProduct", totalProduct);
        order.put("productDp", productDp);
        order.put("addedAt", format);
        order.put("paymentStatus", "Belum Bayar");
        order.put("proofPayment", "Belum Bayar");
        order.put("orderId", timeInMillis);

        FirebaseFirestore
                .getInstance()
                .collection("order")
                .document(timeInMillis)
                .set(order)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        // hapus produk ini dari keranjang
                        deleteProductFromCart();
                    }else {
                        // sembunyikan progress bar untuk selesai loading
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(DetailCartActivity.this, "Ups, tidak berhasil melakukan pemesanan produk", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void deleteProductFromCart() {
        FirebaseFirestore
                .getInstance()
                .collection("cart")
                .document(cartId)
                .delete()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        // sembunyikan progress bar untuk selesai loading
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(DetailCartActivity.this, "Berhasil melakukan pemesanan produk", Toast.LENGTH_SHORT).show();

                    } else {
                        // sembunyikan progress bar untuk selesai loading
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(DetailCartActivity.this, "Ups, tidak berhasil melakukan pemesanan produk", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}