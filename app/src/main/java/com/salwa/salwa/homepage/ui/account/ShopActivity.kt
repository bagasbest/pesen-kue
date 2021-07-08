package com.salwa.salwa.homepage.ui.account

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.salwa.salwa.databinding.ActivityShopBinding
import com.salwa.salwa.homepage.ui.account.delivery.DeliveryActivity
import com.salwa.salwa.homepage.ui.account.select_product.SelectProductActivity
import com.salwa.salwa.homepage.ui.account.view_order.ViewOrderActivity
import com.salwa.salwa.homepage.ui.home.AddProductActivity
import java.text.SimpleDateFormat
import java.util.*

class ShopActivity : AppCompatActivity() {

    private var binding: ActivityShopBinding? = null
    private var uid: String? = null
    private var shopName: String? = null
    private var shopDp: String? = null
    private var totalProfit: Int? = 0

    private lateinit var viewModel: ShopViewModel

    override fun onResume() {
        super.onResume()
        // TAMPILKAN TOTAL PROFIT BULAN INI
        getProfitThisMonth()

        // TAMPILKAN PRODUK TERJUAL BULAN INI
        getSoldProductThisMonth()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        uid = FirebaseAuth.getInstance().currentUser?.uid

        supportActionBar?.title = "Shop"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // TAMPILKAN NAMA TOKO
        displayShopName()

        // KLIK TAMBAH PRODUK BARU
        binding?.addProduct?.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            intent.putExtra(AddProductActivity.EXTRA_SHOP_NAME, shopName)
            intent.putExtra(AddProductActivity.EXTRA_SHOP_DP, shopDp)
            startActivity(intent)
        }

        // KLIK SELECT PRODUCT
        binding?.selectProduct?.setOnClickListener {
            startActivity(Intent(this, SelectProductActivity::class.java))
        }

        // KLIK VIEW ORDER
        binding?.viewOrder?.setOnClickListener {
            startActivity(Intent(this, ViewOrderActivity::class.java))
        }

        // KLIK DELIVERY
        binding?.deliveryBtn?.setOnClickListener {
            val intent = Intent(this, DeliveryActivity::class.java)
            intent.putExtra(DeliveryActivity.EXTRA_ROLE, "seller")
            intent.putExtra(DeliveryActivity.EXTRA_UID, uid)
            startActivity(intent)
        }

    }

    private fun getSoldProductThisMonth() {
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[ShopViewModel::class.java]
        binding?.progressBar?.visibility = View.VISIBLE

        // AMBIL BULAN SEKARANG
        @SuppressLint("SimpleDateFormat")
        val getDate = SimpleDateFormat("MM")
        val month = getDate.format(Date())

        binding?.recyclerView2?.layoutManager = LinearLayoutManager(this)
        val adapter = ShopAdapter()
        adapter.notifyDataSetChanged()
        binding?.recyclerView2?.adapter = adapter

        uid?.let { viewModel.setProductSold(it, month) }
        viewModel.getProductSold().observe(this, { productSold ->
            if(productSold.size > 0) {
                binding?.progressBar?.visibility = View.GONE
                adapter.setData(productSold)
            } else {
                binding?.noData?.visibility = View.VISIBLE
            }
            binding?.progressBar?.visibility = View.GONE
        })
    }

    @SuppressLint("SetTextI18n")
    private fun getProfitThisMonth() {
        @SuppressLint("SimpleDateFormat")
        val getDate = SimpleDateFormat("MM")
        val month = getDate.format(Date())

        uid?.let {
            FirebaseFirestore
                .getInstance()
                .collection("shop")
                .document(it)
                .collection("transaction")
                .whereEqualTo("month", month)
                .get()
                .addOnSuccessListener { getMonth ->
                    for (transaction in getMonth) {
                        totalProfit = totalProfit?.plus(transaction.data["price"].toString().toInt())
                    }
                    binding?.profit?.text = "Rp. $totalProfit"
                }
        }

    }

    private fun displayShopName() {
        uid?.let {
            FirebaseFirestore
                .getInstance()
                .collection("shop")
                .document(it)
                .get()
                .addOnSuccessListener { doc ->
                    shopName = "" + doc.get("name")
                    shopDp = "" + doc.get("dp")
                    binding?.textView6?.text = shopName
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
}