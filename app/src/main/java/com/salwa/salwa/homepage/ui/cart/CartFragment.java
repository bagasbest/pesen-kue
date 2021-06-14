package com.salwa.salwa.homepage.ui.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.salwa.salwa.databinding.FragmentCartBinding;


public class CartFragment extends Fragment {

    private CartViewModel cartViewModel;
    private FragmentCartBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        binding = FragmentCartBinding.inflate(inflater, container, false);

        // inisiasi viewModel untuk menampilkan barang yang ada di keranjang anda
        initViewModel();

        binding.srlData.setOnRefreshListener(() -> {
            binding.srlData.setRefreshing(true);
            // inisiasi viewModel untuk menampilkan barang yang ada di keranjang anda
            initViewModel();
            binding.srlData.setRefreshing(false);
        });

        return binding.getRoot();
    }



    // inisiasi viewModel untuk menampilkan barang yang ada di keranjang anda
    private void initViewModel() {
        // tampilkan daftar belanjaan di keranjang kustomer

        String customerUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        binding.rvCart.setLayoutManager(new LinearLayoutManager(getActivity()));
        CartAdapter cartAdapter = new CartAdapter();
        cartAdapter.notifyDataSetChanged();
        binding.rvCart.setAdapter(cartAdapter);

        binding.progressBar.setVisibility(View.VISIBLE);
        cartViewModel.setCartList(customerUid);
        cartViewModel.getCartList().observe(getViewLifecycleOwner(), cartList -> {
            if (cartList.size() > 0) {
                binding.noData.setVisibility(View.GONE);
                binding.rvCart.setVisibility(View.VISIBLE);
                cartAdapter.setData(cartList);
            } else {
                binding.noData.setVisibility(View.VISIBLE);
                binding.rvCart.setVisibility(View.GONE);
            }
            binding.progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}