package com.salwa.salwa.homepage.ui.account.admin

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.salwa.salwa.R
import com.salwa.salwa.databinding.ActivityAdminListDetailBinding

class AdminListDetailActivity : AppCompatActivity() {

    private var binding: ActivityAdminListDetailBinding? = null
    private var uid:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminListDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.title = "Manage Seller Account"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val admin = intent.getParcelableExtra<AdminListModel>(EXTRA_ADMIN)
        val address = admin?.address
        val dp = admin?.dp
        val name = admin?.name
        val phone = admin?.phone
        val status = admin?.status
        uid = admin?.uid


        binding?.shopName?.setText(name)
        binding?.shopAddress?.setText(address)
        binding?.shopPhone?.setText(phone)

        binding?.roundedImageView2?.let {
            Glide.with(this)
                .load(dp)
                .into(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_admin, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_accept_seller) {
            // tampilkan konfirmasi untuk menerima bukti pembayaran (khusus admin yang dapat melakukan)
            showAcceptDialog()
        } else if (item.itemId == R.id.menu_decline_seller) {
            // tampilkan konfirmasi sebelum menolak order
            showDeclineDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAcceptDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirm to Accept Seller")
            .setMessage("Are you sure want to accept this Seller ?")
            .setPositiveButton(
                "YES"
            ) { dialogInterface: DialogInterface?, _ ->
                // accept seller
                binding?.progressBar?.visibility = View.VISIBLE
                updateShopStatus(dialogInterface)
            }
            .setNegativeButton("NO", null)
            .setIcon(R.drawable.ic_baseline_check_circle_24)
            .show()
    }

    private fun updateShopStatus(dialogInterface: DialogInterface?) {
        uid?.let {
            FirebaseFirestore
                .getInstance()
                .collection("shop")
                .document(it)
                .update("status", "verified")
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        binding?.progressBar?.visibility = View.GONE
                        dialogInterface?.dismiss()
                        Toast.makeText(this, "Successfully accept this seller", Toast.LENGTH_SHORT).show()
                    } else {
                        binding?.progressBar?.visibility = View.GONE
                        dialogInterface?.dismiss()
                        Toast.makeText(this, "Failure accept this seller", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun showDeclineDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirm to Decline Seller")
            .setMessage("Are you sure want to decline this seller ?")
            .setPositiveButton(
                "YES"
            ) { dialogInterface: DialogInterface?, i: Int ->
                // decline seller
                binding?.progressBar?.visibility = View.VISIBLE
                declineShop(dialogInterface)
            }
            .setNegativeButton("NO", null)
            .setIcon(R.drawable.ic_baseline_clear_24)
            .show()
    }

    private fun declineShop(dialogInterface: DialogInterface?) {
        uid?.let {
            FirebaseFirestore
                .getInstance()
                .collection("shop")
                .document(it)
                .delete()
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        binding?.progressBar?.visibility = View.GONE
                        dialogInterface?.dismiss()
                        Toast.makeText(this, "Successfully delete this seller", Toast.LENGTH_SHORT).show()
                    } else {
                        binding?.progressBar?.visibility = View.GONE
                        dialogInterface?.dismiss()
                        Toast.makeText(this, "Failure delete this seller", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val EXTRA_ADMIN = "admin"
    }
}