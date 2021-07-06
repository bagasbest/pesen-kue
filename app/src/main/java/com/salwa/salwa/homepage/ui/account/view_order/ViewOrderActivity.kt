package com.salwa.salwa.homepage.ui.account.view_order

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.salwa.salwa.databinding.ActivityViewOrderBinding
import com.salwa.salwa.homepage.ui.order.OrderAdapter
import com.salwa.salwa.homepage.ui.order.OrderViewModel

class ViewOrderActivity : AppCompatActivity() {

    private var binding: ActivityViewOrderBinding? = null
    private lateinit var orderAdapter: OrderAdapter

    override fun onResume() {
        super.onResume()
        // INISIASI VIEW UNTUK MENAMPILKAN DATA ORDER
        initRecyclerView()
        initViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewOrderBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.title = "Manage Order"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    private fun initRecyclerView() {
        binding?.rvOrder?.layoutManager = LinearLayoutManager(this)
        orderAdapter = OrderAdapter()
        orderAdapter.notifyDataSetChanged()
        binding?.rvOrder?.adapter = orderAdapter
    }

    private fun initViewModel() {
        // tampilkan daftar belanjaan di Halaman Order/Payment
        val orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        binding?.progressBar?.visibility = View.VISIBLE
        orderViewModel.setOrderListByShopId(uid)
        orderViewModel.orderList.observe(this, { orderList ->
                if (orderList.size > 0) {
                    binding?.noData?.visibility = View.GONE
                    orderAdapter.setData(orderList)
                } else {
                    binding?.noData?.visibility = View.VISIBLE
                }
            binding?.progressBar?.visibility = View.GONE
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
}