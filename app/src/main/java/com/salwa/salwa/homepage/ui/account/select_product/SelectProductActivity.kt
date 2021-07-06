package com.salwa.salwa.homepage.ui.account.select_product

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.salwa.salwa.databinding.ActivitySelectProductBinding
import com.salwa.salwa.homepage.ui.home.ProductAdapter
import com.salwa.salwa.homepage.ui.home.ProductViewModel

class SelectProductActivity : AppCompatActivity() {

    private var binding: ActivitySelectProductBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectProductBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.title = "Select Product"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // INIT VIEW MODEL TO POPULATE PRODUCT
        initViewModel()
    }

    private fun initViewModel() {
        val productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        // tampilkan daftar cookies
        binding?.productRv?.layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )

        val productAdapter = ProductAdapter()
        binding?.productRv?.adapter = productAdapter

        binding?.progressBar?.visibility = View.VISIBLE
        productViewModel.setProductListById(uid)
        productViewModel.productList.observe(this, { productList ->
                if (productList.size > 0) {
                    binding?.noData?.visibility = View.GONE
                    productAdapter.setData(productList)
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