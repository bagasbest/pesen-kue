package com.salwa.salwa.homepage.ui.account

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.salwa.salwa.databinding.ActivityCreateShopBinding
import java.util.*

class CreateShopActivity : AppCompatActivity() {

    private var binding: ActivityCreateShopBinding? = null
    private var shopDp: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateShopBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.title = "Register Shop"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // KLIK AMBIL GAMBAR SHOP
        binding?.roundedImageView2?.setOnClickListener {
            captureImage()
        }

        // KLIK BUAT SHOP
        clickCreateShop()
    }

    private fun clickCreateShop() {
        binding?.submitBtn?.setOnClickListener {
            val name = binding?.shopName?.text.toString().trim()
            val address = binding?.shopAddress?.text.toString().trim()
            val phone = binding?.shopPhone?.text.toString().trim()

            when {
                name.isEmpty() -> {
                    binding?.shopName?.error = "Shop Name must be filled"
                    return@setOnClickListener
                }
                address.isEmpty() -> {
                    binding?.shopAddress?.error = "Shop Address must be filled"
                    return@setOnClickListener
                }
                phone.isEmpty() -> {
                    binding?.shopPhone?.error = "Shop Phone must be filled"
                    return@setOnClickListener
                }
                shopDp == null -> {
                    Toast.makeText(this, "Shop Dp must be added", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            // SAVE SHOP TO DATABASE
            saveShopToDatabase(name, address, phone)
        }
    }

    private fun saveShopToDatabase(name: String, address: String, phone: String) {
        binding?.progressBar?.visibility = View.VISIBLE
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        val shop = hashMapOf(
            "uid" to uid,
            "name" to name,
            "address" to address,
            "phone" to phone,
            "dp" to shopDp,
            "status" to "review"
        )

        // CREATE SHOP IN DATABASE
        if (uid != null) {
            FirebaseFirestore
                .getInstance()
                .collection("shop")
                .document(uid)
                .set(shop)
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        binding?.progressBar?.visibility = View.GONE
                        startActivity(Intent(this, CreateShopUnderReviewActivity::class.java))
                    } else {
                        binding?.progressBar?.visibility = View.GONE
                        Toast.makeText(this, "Failure to create shop", Toast.LENGTH_SHORT).show()
                        Log.e("TAG", it.toString())
                    }
                }
        }
    }

    private fun captureImage() {
        ImagePicker.with(this)
            .galleryOnly()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start(REQUEST_FROM_GALLERY_TO_DP)
        binding!!.progressBar.visibility = View.VISIBLE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            Log.d("TAG", requestCode.toString())
            when (requestCode) {
                REQUEST_FROM_GALLERY_TO_DP -> {
                    // UPLOAD SHOP DP KE DATABASE
                    uploadShopDp(data?.data!!)

                    // tampilkan gambar produk ke halaman AddProductActivity
                    Glide.with(this)
                        .load(data.data)
                        .into(binding!!.roundedImageView2)
                    binding!!.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun uploadShopDp(data: Uri) {
        val mStorageRef = FirebaseStorage.getInstance().reference
        val mProgressDialog = ProgressDialog(this)

        mProgressDialog.setMessage("Please wait until progress finished...")
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
        val imageFileName = "shop_dp/image_" + System.currentTimeMillis() + ".png"


        mStorageRef.child(imageFileName).putFile(data)
            .addOnSuccessListener {
                mStorageRef.child(imageFileName).downloadUrl
                    .addOnSuccessListener { uri: Uri ->
                        mProgressDialog.dismiss()
                        shopDp = uri.toString()
                        Log.d("uri Image: ", uri.toString())
                        binding!!.placeHolder.visibility = View.GONE
                    }
                    .addOnFailureListener { e: Exception ->
                        mProgressDialog.dismiss()
                        Toast.makeText(
                            this,
                            "Failure upload shop dp",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("shop dp", e.toString())
                        binding!!.placeHolder.visibility = View.VISIBLE
                    }
            }
            .addOnFailureListener { e: Exception ->
                mProgressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Failure upload shop dp",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("shop dp", e.toString())
                binding!!.placeHolder.visibility = View.VISIBLE
            }
    }

    companion object {
        const val REQUEST_FROM_GALLERY_TO_DP = 1001
    }
}