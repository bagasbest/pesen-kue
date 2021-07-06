package com.salwa.salwa.homepage;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.salwa.salwa.R;
import com.salwa.salwa.databinding.ActivityHomeBinding;
import com.salwa.salwa.homepage.ui.account.AccountFragment;
import com.salwa.salwa.homepage.ui.cart.CartFragment;
import com.salwa.salwa.homepage.ui.delivery.DeliveryFragment;
import com.salwa.salwa.homepage.ui.order.OrderFragment;
import com.salwa.salwa.homepage.ui.home.ProductFragment;

public class HomeActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // untuk mengganti halaman contoh: halaman produk -> halaman keranjang -> halaman pemesanan/pembayaran -> halaman akun
        navView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = new ProductFragment();
            switch (item.getItemId()) {
                case R.id.navigation_home: {
                    navView.getMenu().findItem(R.id.navigation_home).setEnabled(false);
                    navView.getMenu().findItem(R.id.navigation_cart).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_payment).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_delivery).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_account).setEnabled(true);
                    selectedFragment = new ProductFragment();
                    break;
                }
                case R.id.navigation_cart: {
                    navView.getMenu().findItem(R.id.navigation_home).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_cart).setEnabled(false);
                    navView.getMenu().findItem(R.id.navigation_payment).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_delivery).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_account).setEnabled(true);
                    selectedFragment = new CartFragment();
                    break;
                }
                case R.id.navigation_payment: {
                    navView.getMenu().findItem(R.id.navigation_home).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_cart).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_payment).setEnabled(false);
                    navView.getMenu().findItem(R.id.navigation_delivery).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_account).setEnabled(true);
                    selectedFragment = new OrderFragment();
                    break;
                }
                case R.id.navigation_delivery: {
                    navView.getMenu().findItem(R.id.navigation_home).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_cart).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_payment).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_delivery).setEnabled(false);
                    navView.getMenu().findItem(R.id.navigation_account).setEnabled(true);
                    selectedFragment = new DeliveryFragment();
                    break;
                }
                case R.id.navigation_account: {
                    navView.getMenu().findItem(R.id.navigation_home).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_cart).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_payment).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_delivery).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_account).setEnabled(false);
                    selectedFragment = new AccountFragment();
                    break;
                }
                default: {
                    navView.getMenu().findItem(R.id.navigation_home).setEnabled(false);
                }
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, selectedFragment);
            transaction.commit();

            return true;
        });
    }

}