package com.salwa.salwa.homepage.ui.account.delivery

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.salwa.salwa.databinding.ActivityDeliveryBinding
import com.salwa.salwa.homepage.ui.delivery.DeliveryAdapter
import com.salwa.salwa.homepage.ui.delivery.DeliveryViewModel

class DeliveryActivity : AppCompatActivity() {

    private var binding: ActivityDeliveryBinding? = null
    private lateinit var deliveryAdapter: DeliveryAdapter
    private lateinit var viewModel: DeliveryViewModel

    override fun onResume() {
        super.onResume()
        // TAMPILKAN LIST DELIVERY BERDASARKAN ROLE SELLER / ADMIN
        initRecyclerView()
        initViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeliveryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.title = "Delivery"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initRecyclerView() {
        binding?.rvDelivery?.layoutManager = LinearLayoutManager(this)
        deliveryAdapter = DeliveryAdapter()
        binding?.rvDelivery?.adapter = deliveryAdapter
    }

    private fun initViewModel() {
        // tampilkan daftar delivery
        viewModel = ViewModelProvider(this).get(
            DeliveryViewModel::class.java
        )


        if(intent.getStringExtra(EXTRA_ROLE) == "admin") {
            viewModel.setAllDelivery()
        } else {
            viewModel.setDeliveryBySellerSide(intent.getStringExtra(EXTRA_UID))
        }
        viewModel.deliveryList.observe(this, { deliveryList ->
            if (deliveryList.size > 0) {
                binding!!.noData.visibility = View.GONE
                deliveryAdapter.setData(deliveryList)
            } else {
                binding!!.noData.visibility = View.VISIBLE
            }
            binding!!.progressBar.visibility = View.GONE
        })
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
        const val EXTRA_ROLE = "role"
        const val EXTRA_UID = "uid"
    }

}