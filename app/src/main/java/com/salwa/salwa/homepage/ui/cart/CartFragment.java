package com.salwa.salwa.homepage.ui.cart;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.salwa.salwa.databinding.FragmentCartBinding;


public class CartFragment extends Fragment {

    private CartViewModel cartViewModel;
    private FragmentCartBinding binding;


    @Override
    public void onResume() {
        super.onResume();
        // inisiasi viewModel untuk menampilkan barang yang ada di keranjang anda
        initViewModel();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Keranjang");
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }


    // inisiasi viewModel untuk menampilkan barang yang ada di keranjang anda
    private void initViewModel() {
        // tampilkan daftar belanjaan di keranjang kustomer
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        String customerUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        binding.rvCart.setLayoutManager(new LinearLayoutManager(getActivity()));
        CartAdapter cartAdapter = new CartAdapter();
        binding.rvCart.setAdapter(cartAdapter);

        binding.progressBar.setVisibility(View.VISIBLE);
        cartViewModel.setCartList(customerUid);
        cartViewModel.getCartList().observe(getViewLifecycleOwner(), cartList -> {
            Log.e("TAG", String.valueOf(cartList.size()));
            if (cartList.size() > 0) {
                binding.noData.setVisibility(View.GONE);
                cartAdapter.setData(cartList);
            } else {
                binding.noData.setVisibility(View.VISIBLE);
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