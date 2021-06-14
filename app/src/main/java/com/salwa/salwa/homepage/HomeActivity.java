package com.salwa.salwa.homepage;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.salwa.salwa.R;
import com.salwa.salwa.databinding.ActivityHomeBinding;
import com.salwa.salwa.homepage.ui.cart.CartFragment;
import com.salwa.salwa.homepage.ui.order.OrderFragment;
import com.salwa.salwa.homepage.ui.product.ProductFragment;

public class HomeActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_product, R.id.navigation_cart, R.id.navigation_payment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


        // untuk mengganti halaman contoh: halaman produk -> halaman keranjang -> halaman pemesanan/pembayaran
        navView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = new ProductFragment();
            switch (item.getItemId()) {
                case R.id.navigation_product: {
                    navView.getMenu().findItem(R.id.navigation_product).setEnabled(false);
                    navView.getMenu().findItem(R.id.navigation_cart).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_payment).setEnabled(true);
                    selectedFragment = new ProductFragment();
                    break;
                }
                case R.id.navigation_cart: {
                    navView.getMenu().findItem(R.id.navigation_product).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_cart).setEnabled(false);
                    navView.getMenu().findItem(R.id.navigation_payment).setEnabled(true);
                    selectedFragment = new CartFragment();
                    break;
                }
                case R.id.navigation_payment: {
                    navView.getMenu().findItem(R.id.navigation_product).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_cart).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_payment).setEnabled(false);
                    selectedFragment = new OrderFragment();
                    break;
                }
                default: {
                    navView.getMenu().findItem(R.id.navigation_product).setEnabled(false);
                }
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, selectedFragment);
            transaction.commit();

            return true;
        });
    }

}