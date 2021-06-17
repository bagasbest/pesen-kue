package com.salwa.salwa.homepage.ui.order;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.salwa.salwa.R;
import com.salwa.salwa.databinding.ActivityDetailOrderBinding;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class DetailOrderActivity extends AppCompatActivity {

    private ActivityDetailOrderBinding binding;
    private static final int REQUEST_FROM_GALLERY_TO_SELF_PHOTO = 1001;


    public static final String ITEM_ORDER = "order";
    private String addedAt;
    private String bookedBy;
    private String description;
    private int price;
    private String productDp;
    private String productId;
    private String kecamatan;
    private String kelurahan;
    private String address;
    private String phone;
    private String title;
    private int totalProduct;
    private String userUid;
    private String status;
    private String proofPayment;
    private String orderId;
    private String uid = "";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        binding = ActivityDetailOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // dapatkan atribut dari produk yang di klik pengguna
        OrderModel om = getIntent().getParcelableExtra(ITEM_ORDER);
        title = om.getTitle();

        // tambahkan judul dan ikon back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true);

        addedAt = om.getAddedAt();
        bookedBy = om.getBookedBy();
        description = om.getDescription();
        price = om.getPrice();
        productDp = om.getProductDp();
        productId = om.getProductId();
        kecamatan = om.getKecamatan();
        kelurahan = om.getKelurahan();
        address = om.getAddress();
        phone = om.getPhone();
        totalProduct = om.getTotalProduct();
        userUid = om.getUserUid();
        status = om.getStatus();
        proofPayment = om.getProofPayment();
        orderId = om.getOrderId();

        binding.title.setText(title);
        binding.description.setText(description);
        binding.name.setText("Nama: " + bookedBy);
        binding.waktu.setText("Waktu: " + addedAt);
        binding.totalProduct.setText("Total Produk: " + totalProduct);
        binding.kecamatan.setText("Kecamatan: " + kecamatan);
        binding.kelurahan.setText("Kelurahan: " + kelurahan);
        binding.address.setText("Alamat: " + address);
        binding.textView7.setText("No.Telepon: " + phone);
        binding.price.setText("Total Harga: " + price);

        Glide.with(this)
                .load(productDp)
                .placeholder(R.drawable.ic_baseline_broken_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(binding.productDp);

        if (!proofPayment.equals("Belum Bayar")) {
            binding.placeHolder.setVisibility(View.GONE);
            Glide.with(this)
                    .load(proofPayment)
                    .into(binding.roundedImageView2);
        }


        // unggah bukti pembayaran
        binding.roundedImageView2.setOnClickListener(view -> {
            uploadProofPayment();
        });


    }

    private void uploadProofPayment() {
        ImagePicker.with(DetailOrderActivity.this)
                .galleryOnly()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(REQUEST_FROM_GALLERY_TO_SELF_PHOTO);

        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_FROM_GALLERY_TO_SELF_PHOTO) {
                uploadProofPaymentToDatabase(data.getData());
                Log.d("image URI: ", data.getData().toString());

                // tampilkan gambar produk ke halaman AddProductActivity
                Glide.with(this)
                        .load(data.getData())
                        .into(binding.roundedImageView2);
                binding.progressBar.setVisibility(View.GONE);
            }
        }
    }

    private void uploadProofPaymentToDatabase(Uri data) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        String imageFileName = "ProofPayment/" + title + "image_" + System.currentTimeMillis() + ".png";


        mStorageRef.child(imageFileName).putFile(data)
                .addOnSuccessListener(taskSnapshot ->
                        mStorageRef.child(imageFileName).getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    productDp = uri.toString();
                                    Log.d("uri Image: ", uri.toString());
                                    binding.placeHolder.setVisibility(View.GONE);
                                    setProofPaymentToOrderItem(mProgressDialog);
                                })
                                .addOnFailureListener(e -> {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(DetailOrderActivity.this, "Gagal mengunggah bukti pembayaran", Toast.LENGTH_SHORT).show();
                                    Log.d("productDp", e.toString());
                                    binding.placeHolder.setVisibility(View.VISIBLE);
                                }))
                .addOnFailureListener(e -> {
                    mProgressDialog.dismiss();
                    Toast.makeText(DetailOrderActivity.this, "Gagal mengunggah bukti pembayaran", Toast.LENGTH_SHORT).show();
                    Log.d("productDp", e.toString());
                    binding.placeHolder.setVisibility(View.VISIBLE);
                });
    }

    private void setProofPaymentToOrderItem(ProgressDialog mProgressDialog) {

        Map<String, Object> proofPaymentImage = new HashMap<>();
        proofPaymentImage.put("paymentStatus", "Dalam Proses");
        proofPaymentImage.put("proofPayment", productDp);

        FirebaseFirestore
                .getInstance()
                .collection("order")
                .document(orderId)
                .update(proofPaymentImage)
                .addOnCompleteListener(task -> {
                    mProgressDialog.dismiss();

                    // tampilkan dialog ketika berhasil mengunggah bukti pembayaran
                    new AlertDialog.Builder(this)
                            .setTitle("Berhasil Mengunggah Bukti Pembayaran")
                            .setMessage("Anda berhasil mengunggah bukti pembayaran produk ini, silahkan tunggu beberapa saat.\n\nAdmin akan mengecek bukti pembayaran yang anda kirimkan, setelah itu produk akan dikirimkan ke alamat anda")
                            .setPositiveButton("YA", null)
                            .setIcon(R.drawable.ic_baseline_check_circle_24)
                            .show();
                })
                .addOnFailureListener(e -> {
                    mProgressDialog.dismiss();
                    Toast.makeText(DetailOrderActivity.this, "Gagal mengunggah bukti pembayaran", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_proof_payment, menu);

        // jika bukan admin maka sembunyikan ikon ceklis untuk memverifikasi pembayaran
        MenuItem item = menu.findItem(R.id.menu_accept).setVisible(false);
        // jika status pembayaran = sudah membayar, maka pengguna dapat menghapus riwayat pembayaran
        MenuItem item2 = menu.findItem(R.id.menu_delete).setVisible(false);


        // CEK APAKAH USER YANG SEDANG LOGIN ADMIN ATAU BUKAN, JIKA YA, MAKA TAMPILKAN IKON VERIFIKASI BUKTI PEMBAYARAN
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (("" + document.get("role")).equals("admin")) {
                            item.setVisible(true);
                        }
                    }
                });

        // jika status pembayaran = sudah membayar, maka pengguna dapat menghapus riwayat pembayaran
        FirebaseFirestore
                .getInstance()
                .collection("order")
                .document(orderId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(("" + documentSnapshot.get("paymentStatus")).equals("Sudah Bayar")) {
                        item2.setVisible(true);
                    }
                });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_accept) {
            // tampilkan konfirmasi untuk menerima bukti pembayaran (khusus admin yang dapat melakukan)
            showConfirmDialog();
        }
        else if (item.getItemId() == R.id.menu_delete) {
            // hapus riwayat pembelian produk
            deleteHistoryPayment();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteHistoryPayment() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Hapus Riwayat Pembayaran")
                .setMessage("Apakah anada yakin ingin menghapus riwayat pembayaran produk ini ?")
                .setPositiveButton("YA", (dialogInterface, i) -> {
                    // Hapus riwayat pembayaran produk ini
                    FirebaseFirestore
                            .getInstance()
                            .collection("order")
                            .document(orderId)
                            .delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(DetailOrderActivity.this, "Berhasil menghapus riwayat pembayaran  produk " + title, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(DetailOrderActivity.this, "Gagal menghapus riwayat pembayaran  produk " + title, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                })
                .setNegativeButton("TIDAK", null)
                .setIcon(R.drawable.ic_baseline_delete_24)
                .show();
    }

    @SuppressLint("ResourceType")
    private void showConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Status Pembayaran")
                .setMessage("Apakah anada yakin kustomer telah melakukan transfer uang ke rekening anda untuk membeli produk ini ?")
                .setPositiveButton("YA", (dialogInterface, i) -> {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    // order produk
                    updateStatusOrder();
                })
                .setNegativeButton("TIDAK", null)
                .setIcon(R.drawable.ic_baseline_check_circle_24)
                .show();
    }

    private void updateStatusOrder() {

        Map<String, Object> payment = new HashMap<>();
        payment.put("paymentStatus", "Sudah Bayar");

        FirebaseFirestore
                .getInstance()
                .collection("order")
                .document(orderId)
                .update(payment)
                .addOnCompleteListener(task -> {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(DetailOrderActivity.this, "Berhasil memperbarui status pembayaran menjadi Sudah Membayar", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(DetailOrderActivity.this, "Gagal memperbarui status pembayaran", Toast.LENGTH_SHORT).show();
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