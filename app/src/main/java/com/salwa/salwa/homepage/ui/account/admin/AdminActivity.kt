package com.salwa.salwa.homepage.ui.account.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.salwa.salwa.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity() {

    private var binding: ActivityAdminBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.title = "Admin"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // KLIK SELLER AKUN
        clickSellerAccount()

        // KLIK MANAGE ORDER
        clickManageOrder()

    }

    private fun clickManageOrder() {
        binding?.orderBtn?.setOnClickListener {
            startActivity(Intent(this, ManageOrderActivity::class.java))
        }
    }

    private fun clickSellerAccount() {
        binding?.sellerAccountBtn?.setOnClickListener {
            startActivity(Intent(this, AdminListActivity::class.java))
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
}