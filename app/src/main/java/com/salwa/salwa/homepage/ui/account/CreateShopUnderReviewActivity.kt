package com.salwa.salwa.homepage.ui.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.salwa.salwa.databinding.ActivityCreateShopUnderReviewBinding
import com.salwa.salwa.homepage.HomeActivity

class CreateShopUnderReviewActivity : AppCompatActivity() {

    private var binding: ActivityCreateShopUnderReviewBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateShopUnderReviewBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // KEMBALI KE AKUNKU
        binding?.backButton?.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}