package com.salwa.salwa.homepage.ui.cart;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.salwa.salwa.R;
import com.salwa.salwa.databinding.ActivityDetailCartBinding;
import com.salwa.salwa.utils.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetailCartActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

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
    private String shopId;
    private String address;
    private boolean isPickup = false;
    private String dateTime;


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
        shopId = cm.getShopId();


        binding.name.setText("Name : " + bookedBy);
        binding.title.setText(title);
        binding.description.setText(description);
        binding.price.setText("Total Proce: Rp. " + price);
        binding.totalProduct.setText("Total Quantity: " + totalProduct + " Product");

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

        // CEK APAKAH PENGGUNA KLIK PICKUP ATAU TIDAK
        checkIsPickupOrNot();

        // PILIH WAKTU PICKUP
        getDateTimePickup();

    }

    private void getDateTimePickup() {
        binding.dateTime.setOnClickListener(view -> {
            DatePickerFragment datePickerFragment = new DatePickerFragment();
            datePickerFragment.show(getSupportFragmentManager(), "DatePicker");
        });
    }

    @SuppressLint("SetTextI18n")
    private void checkIsPickupOrNot() {
        binding.pickup.setOnClickListener(view -> {
            if(binding.pickup.isChecked()) {
                FirebaseFirestore
                        .getInstance()
                        .collection("shop")
                        .document(shopId)
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            address = ""  +documentSnapshot.get("address");
                            isPickup = true;
                            binding.addressEt.setText(address);
                            binding.dateTime.setVisibility(View.VISIBLE);
                            binding.addressEt.setEnabled(false);
                        });
            } else {
                binding.addressEt.setText("");
                isPickup = false;
                binding.dateTime.setVisibility(View.GONE);
                binding.addressEt.setEnabled(true);
            }
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
        else if(isPickup && binding.dateTime.getText().toString().equals("Choose Date Time Here")) {
            Toast.makeText(DetailCartActivity.this, "Pickup time must be set", Toast.LENGTH_SHORT).show();
            return;
        }

        // lakukan pemesanan dan pembayaran
        new AlertDialog.Builder(this)
                .setTitle("Confirmation Order")
                .setMessage("Are you sure want to order: " + title + " ?")
                .setPositiveButton("YES", (dialogInterface, i) -> {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    // order produk
                    orderProduct();
                })
                .setNegativeButton("NO", null)
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
        order.put("shopId", shopId);
        order.put("isPickup", isPickup);
        order.put("pickupDate", dateTime);

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
                        binding.btnOrder.setVisibility(View.GONE);
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePicker tag, int year, int mon, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, mon, day);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        dateTime = simpleDateFormat.format(calendar.getTime());
        binding.dateTime.setText("Pickup on: " + dateTime);
    }
}