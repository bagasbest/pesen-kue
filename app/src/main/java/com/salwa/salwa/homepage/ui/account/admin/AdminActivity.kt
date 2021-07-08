package com.salwa.salwa.homepage.ui.account.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.salwa.salwa.databinding.ActivityAdminBinding
import com.salwa.salwa.homepage.ui.account.delivery.DeliveryActivity

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

        // KLIK DELIVERY
        clickDelivery()

    }

    private fun clickDelivery() {
        binding?.deliveryBtn?.setOnClickListener {
            val intent = Intent(this, DeliveryActivity::class.java)
            intent.putExtra(DeliveryActivity.EXTRA_ROLE, "admin")
            startActivity(intent)
        }
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