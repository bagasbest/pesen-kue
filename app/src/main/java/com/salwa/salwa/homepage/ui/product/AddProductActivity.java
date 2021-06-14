package com.salwa.salwa.homepage.ui.product;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.salwa.salwa.R;
import com.salwa.salwa.databinding.ActivityAddProductBinding;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddProductActivity extends AppCompatActivity {

    private ActivityAddProductBinding binding;

    // variabel
    private static final int REQUEST_FROM_GALLERY_TO_SELF_PHOTO = 1001;
    private String productDp = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // tambahkan judul dan ikon back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Tambah Produk Baru");
        actionBar.setDisplayHomeAsUpEnabled(true);


        // klik tombol tambahkan produk
        binding.submitProduct.setOnClickListener(view -> {
            // validasi form inputan, jangan sampai ada yang kosong
            validateFormAddProduct();
        });

        // klik tambahkan foto produk
        binding.productDp.setOnClickListener(view -> {
            ImagePicker.with(AddProductActivity.this)
                    .galleryOnly()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start(REQUEST_FROM_GALLERY_TO_SELF_PHOTO);

            binding.progressBar.setVisibility(View.VISIBLE);
        });
    }


    // validasi kolom inputan produk
    private void validateFormAddProduct() {
        String title = Objects.requireNonNull(binding.title.getText()).toString().trim();
        String description = Objects.requireNonNull(binding.description.getText()).toString().trim();
        String price = Objects.requireNonNull(binding.price.getText()).toString().trim();


        if (title.isEmpty()) {
            binding.title.setError("Nama Produk harus diisi");
            return;
        } else if (description.isEmpty()) {
            binding.description.setError("Deskripsi Produk harus diisi");
            return;
        } else if (price.isEmpty()) {
            binding.price.setError("Harga Produk harus diisi");
            return;
        } else if(productDp == null) {
            Toast.makeText(AddProductActivity.this, "Anda harus menambahkan gambar produk", Toast.LENGTH_SHORT).show();
        }

        // jika tidak ada yang kosong, maka data harus diinpukan ke database
        inputProductToDatabase(title, description, price);

    }


    private void inputProductToDatabase(String title, String description, String price) {
        binding.progressBar.setVisibility(View.VISIBLE);
        String timeInMillis = String.valueOf(System.currentTimeMillis());

        // ambil tanggal hari ini dengan format: dd - MMM - yyyy, HH:mm:ss
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat getDate = new SimpleDateFormat("dd - MMM - yyyy, HH:mm:ss");
        String format = getDate.format(new Date());

        Map<String, Object> users = new HashMap<>();
        users.put("productId", timeInMillis);
        users.put("title", title);
        users.put("description", description);
        users.put("price", Integer.parseInt(price));
        users.put("productDp", productDp);
        users.put("likes", 0);
        users.put("addedAt", format);
        users.put("updatedAt", format);

        FirebaseFirestore.getInstance()
                .collection("product")
                .document(timeInMillis)
                .set(users)
                .addOnSuccessListener(unused -> {
                    // sembunyikan progress bar untuk selesai loading
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(AddProductActivity.this, "Berhasil menambahkan produk baru", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // sembunyikan progress bar untuk selesai loading
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(AddProductActivity.this, "Ups, tidak berhasil menambahkan produk baru", Toast.LENGTH_SHORT).show();
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_FROM_GALLERY_TO_SELF_PHOTO) {
                uploadImageToDatabase(data.getData());
                Log.d("image URI: ", data.getData().toString());

                // tampilkan gambar produk ke halaman AddProductActivity
                Glide.with(this)
                        .load(data.getData())
                        .placeholder(R.drawable.ic_baseline_add_to_photos_24)
                        .error(R.drawable.ic_baseline_add_to_photos_24)
                        .into(binding.productDp);
                binding.progressBar.setVisibility(View.GONE);


            }
        }
    }



    private void uploadImageToDatabase(Uri data) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        String imageFileName = "product/image_" + System.currentTimeMillis() + ".png";


        mStorageRef.child(imageFileName).putFile(data)
                .addOnSuccessListener(taskSnapshot ->
                        mStorageRef.child(imageFileName).getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    mProgressDialog.dismiss();
                                    productDp = uri.toString();
                                    Log.d("uri Image: ", uri.toString());
                                    binding.addHint.setVisibility(View.GONE);
                                })
                                .addOnFailureListener(e -> {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(AddProductActivity.this, "Gagal mengunggah gambar produk", Toast.LENGTH_SHORT).show();
                                    Log.d("productDp", e.toString());
                                    binding.addHint.setVisibility(View.VISIBLE);
                                }))
                .addOnFailureListener(e -> {
                    mProgressDialog.dismiss();
                    Toast.makeText(AddProductActivity.this, "Gagal mengunggah gambar produk", Toast.LENGTH_SHORT).show();
                    Log.d("productDp", e.toString());
                    binding.addHint.setVisibility(View.VISIBLE);
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}