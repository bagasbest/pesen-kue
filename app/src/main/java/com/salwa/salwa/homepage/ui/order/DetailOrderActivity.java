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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.salwa.salwa.R;
import com.salwa.salwa.databinding.ActivityDetailOrderBinding;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailOrderActivity extends AppCompatActivity {

    private ActivityDetailOrderBinding binding;
    private static final int REQUEST_FROM_GALLERY_TO_SELF_PHOTO = 1001;


    public static final String ITEM_ORDER = "order";
    private String bookedBy;
    private int price;
    private String productDp;
    private String address;
    private String phone;
    private String title;
    private int totalProduct;
    private String userUid;
    private String status;
    private String orderId;
    private String uid = "";
    private String shopId;
    private boolean isPickup = false;
    private String pickupDate;
    private String productId;
    private String proofPayment;

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

        String addedAt = om.getAddedAt();
        bookedBy = om.getBookedBy();
        String description = om.getDescription();
        price = om.getPrice();
        productDp = om.getProductDp();
        productId = om.getProductId();
        address = om.getAddress();
        phone = om.getPhone();
        totalProduct = om.getTotalProduct();
        userUid = om.getUserUid();
        status = om.getStatus();
        proofPayment = om.getProofPayment();
        orderId = om.getOrderId();
        shopId = om.getShopId();
        isPickup = om.isPickup();
        pickupDate = om.getPickupDate();

        binding.title.setText(title);
        binding.description.setText(description);
        binding.name.setText("Nama: " + bookedBy);
        binding.totalProduct.setText("Total Produk: " + totalProduct);
        binding.addressEt.setText(address);
        binding.textView7.setText("No.Telepon: " + phone);
        binding.price.setText("Total Harga: " + price);

        Glide.with(this)
                .load(productDp)
                .placeholder(R.drawable.ic_baseline_broken_image_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(binding.productDp);

        // JIKA SUDAH MENGUPLOAD BUKTI PEMBAYARAN, MAKA TIDAK BISA LAGI MENGEDIT DATA
        if (!proofPayment.equals("Belum Bayar")) {
            binding.savePaymentProof.setVisibility(View.GONE);
            binding.bank.setVisibility(View.GONE);
            binding.cod.setVisibility(View.GONE);
            binding.paymentBank.setVisibility(View.VISIBLE);
            binding.placeHolder.setVisibility(View.GONE);
            Glide.with(this)
                    .load(proofPayment)
                    .into(binding.roundedImageView2);
        }

        // JIKA PEMBAYARAN MELALUI COD
        if(proofPayment.equals("COD")) {
            binding.savePaymentProof.setVisibility(View.GONE);
            binding.bank.setVisibility(View.GONE);
            binding.cod.setVisibility(View.GONE);
            binding.paymentBank.setVisibility(View.VISIBLE);
            binding.codTv.setVisibility(View.VISIBLE);
            binding.placeHolder.setVisibility(View.GONE);
            binding.roundedImageView2.setVisibility(View.GONE);
        }


        // unggah bukti pembayaran
        if(status.equals("Belum Bayar")) {
            binding.roundedImageView2.setOnClickListener(view -> {
                uploadProofPayment();
            });
        }


        // CEK APAKAH PICKUP ATAU TIDAK
        checkIsPickupOrNot();

        // PAY WITH BANK ACCOUNT
        payWithBankAccount();

        // PAY WITH COD
        payWithCOD();


        // KLIK TOMBOL SAVE PAYMENT PROOF
        binding.savePaymentProof.setOnClickListener(view ->{

            if(proofPayment.equals("Belum Bayar")) {
                Toast.makeText(DetailOrderActivity.this, "Please, input payment proof", Toast.LENGTH_SHORT).show();
                return;
            }

            setProofPaymentToOrderItem("bank");
        });

    }

    private void payWithCOD() {
        binding.cod.setOnClickListener(view -> {
            setProofPaymentToOrderItem("cod");
        });
    }

    private void payWithBankAccount() {
        binding.bank.setOnClickListener(view -> {
            binding.paymentBank.setVisibility(View.VISIBLE);
        });
    }

    @SuppressLint("SetTextI18n")
    private void checkIsPickupOrNot() {
        if(isPickup) {
            binding.dateTime.setVisibility(View.VISIBLE);
            binding.dateTime.setText("Pickup on: " + pickupDate);
            binding.pickUp.setChecked(true);
        }
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
                                    mProgressDialog.dismiss();
                                    proofPayment = uri.toString();
                                    Log.d("uri Image: ", uri.toString());
                                    binding.placeHolder.setVisibility(View.GONE);
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

    private void setProofPaymentToOrderItem(String paymentMethod) {

        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, Object> proofPaymentImage = new HashMap<>();
        proofPaymentImage.put("paymentStatus", "Dalam Proses");
        if(paymentMethod.equals("bank")) {
            proofPaymentImage.put("proofPayment", proofPayment);
        } else {
            proofPaymentImage.put("proofPayment", "COD");
        }

        FirebaseFirestore
                .getInstance()
                .collection("order")
                .document(orderId)
                .update(proofPaymentImage)
                .addOnCompleteListener(task -> {

                    binding.progressBar.setVisibility(View.GONE);
                    binding.savePaymentProof.setVisibility(View.GONE);
                    binding.cod.setVisibility(View.GONE);
                    binding.bank.setVisibility(View.GONE);
                    // tampilkan dialog ketika berhasil mengunggah bukti pembayaran
                    new AlertDialog.Builder(this)
                            .setTitle("Berhasil Mengunggah Bukti Pembayaran")
                            .setMessage("Anda berhasil mengunggah bukti pembayaran produk ini, silahkan tunggu beberapa saat.\n\nAdmin akan mengecek bukti pembayaran yang anda kirimkan, setelah itu produk akan dikirimkan ke alamat anda")
                            .setPositiveButton("YA", null)
                            .setIcon(R.drawable.ic_baseline_check_circle_24)
                            .show();
                })
                .addOnFailureListener(e -> {
                    binding.progressBar.setVisibility(View.GONE);
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

        // SELLER BISA MEMVERIFIKASI ORDER YANG BERSTATUS DALAM PROSES
        if(status.equals("Dalam Proses")) {
            // CEK APAKAH SELLER ATAU BUKAN
           if(uid.equals(shopId)) {
               item.setVisible(true);
           }
        }


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
                            .addOnCompleteListener(task -> {
                                if(task.isSuccessful()) {
                                    Toast.makeText(DetailOrderActivity.this, "Berhasil menghapus riwayat pembayaran  produk " + title, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(DetailOrderActivity.this, "Gagal menghapus riwayat pembayaran  produk " + title, Toast.LENGTH_SHORT).show();
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
                .setMessage("Apakah anda yakin kustomer telah melakukan transfer uang ke rekening anda untuk membeli produk ini ?")
                .setPositiveButton("YA", (dialogInterface, i) -> {
                    // order produk
                    updateStatusOrder();
                })
                .setNegativeButton("TIDAK", null)
                .setIcon(R.drawable.ic_baseline_check_circle_24)
                .show();
    }

    private void updateStatusOrder() {
        ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        // set time in millis
        String timeInMillis = String.valueOf(System.currentTimeMillis());

        // ambil tanggal hari ini dengan format: dd - MMM - yyyy, HH:mm:ss
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat getDate = new SimpleDateFormat("dd MMM yyyy, HH:mm:ss");
        String format = getDate.format(new Date());

        Map<String, Object> deliverProduct = new HashMap<>();
        deliverProduct.put("bookedBy", bookedBy);
        deliverProduct.put("userUid", userUid);
        deliverProduct.put("address", address);
        deliverProduct.put("phone", phone);
        deliverProduct.put("title", title);
        deliverProduct.put("price", price);
        deliverProduct.put("totalProduct", totalProduct);
        deliverProduct.put("productDp", productDp);
        deliverProduct.put("addedAt", format);
        deliverProduct.put("deliveryStatus", "Belum Dikirim");
        deliverProduct.put("deliveryId", timeInMillis);
        deliverProduct.put("shopId", shopId);
        deliverProduct.put("isPickup", isPickup);
        deliverProduct.put("pickupDate", pickupDate);
        deliverProduct.put("productId", productId);
        if(proofPayment.equals("COD")) {
            deliverProduct.put("cod","YA");
        }else {
            deliverProduct.put("cod","TIDAK");
        }


        // UPDATE STATUS PEMBAYARAN MENJADI SUDAH MEMBAYAR
        Map<String, Object> payment = new HashMap<>();
        if(proofPayment.equals("COD")) {
            payment.put("paymentStatus", "COD");
        } else {
            payment.put("paymentStatus", "Sudah Bayar");
        }
        FirebaseFirestore
                .getInstance()
                .collection("order")
                .document(orderId)
                .update(payment)
                .addOnCompleteListener(task -> {

                    // KIRIM DATA KE DELIVERY
                    FirebaseFirestore
                            .getInstance()
                            .collection("delivery")
                            .document(timeInMillis)
                            .set(deliverProduct)
                            .addOnCompleteListener(task1 -> {
                                if(task1.isSuccessful()) {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(DetailOrderActivity.this, "Berhasil memperbarui status pembayaran menjadi Sudah Membayar", Toast.LENGTH_SHORT).show();
                                }else {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(DetailOrderActivity.this, "Gagal memperbarui status pembayaran", Toast.LENGTH_SHORT).show();
                                }
                            });

                })
                .addOnFailureListener(e -> {
                    mProgressDialog.dismiss();
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