package com.salwa.salwa.homepage.ui.account.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.salwa.salwa.databinding.ActivityAdminListBinding
import com.salwa.salwa.homepage.ui.account.ShopAdapter
import com.salwa.salwa.homepage.ui.account.ShopViewModel

class AdminListActivity : AppCompatActivity() {

    private var binding: ActivityAdminListBinding? = null
    private lateinit var viewModel: AdminListViewModel
    private lateinit var adapter: AdminListAdapter

    override fun onResume() {
        super.onResume()
        initRecyclerView()
        initViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminListBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.title = "Manage Seller Account"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initRecyclerView() {
        binding?.rvAdminList?.layoutManager = LinearLayoutManager(this)
        adapter = AdminListAdapter()
        adapter.notifyDataSetChanged()
        binding?.rvAdminList?.adapter = adapter
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[AdminListViewModel::class.java]
        binding?.progressBar?.visibility = View.VISIBLE

        viewModel.setSeller()
        viewModel.getSeller().observe(this, { seller ->
            if(seller.size > 0) {
                binding?.noData?.visibility = View.GONE
                adapter.setData(seller)
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