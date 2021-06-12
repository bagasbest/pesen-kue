package com.salwa.salwa.homepage.ui.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.salwa.salwa.databinding.FragmentCartBinding;
import com.salwa.salwa.homepage.ui.product.ProductAdapter;


public class CartFragment extends Fragment {

    private CartViewModel cartViewModel;
    private FragmentCartBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        binding = FragmentCartBinding.inflate(inflater, container, false);

        String customerUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // tampilkan daftar belanjaan di keranjang kustomer
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


        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}