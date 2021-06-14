package com.salwa.salwa.homepage.ui.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.salwa.salwa.databinding.FragmentOrderBinding;

public class OrderFragment extends Fragment {

    private OrderViewModel orderViewModel;
    private FragmentOrderBinding binding;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Pemesanan dan Pembayaran");
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        binding = FragmentOrderBinding.inflate(inflater, container, false);

        // inisiasi viewModel untuk menampilkan barang yang ada di Halaman Order/Payment
        initViewModel();

        binding.srlData.setOnRefreshListener(() -> {
            binding.srlData.setRefreshing(true);
            // inisiasi viewModel untuk menampilkan barang yang ada di Halaman Order/Payment
            initViewModel();
            binding.srlData.setRefreshing(false);
        });

        return binding.getRoot();
    }

    private void initViewModel() {
        // tampilkan daftar belanjaan di Halaman Order/Payment

        String customerUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        binding.rvOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        OrderAdapter orderAdapter = new OrderAdapter();
        orderAdapter.notifyDataSetChanged();
        binding.rvOrder.setAdapter(orderAdapter);

        binding.progressBar.setVisibility(View.VISIBLE);
        orderViewModel.setOrderList(customerUid);
        orderViewModel.getOrderList().observe(getViewLifecycleOwner(), orderList -> {
            if (orderList.size() > 0) {
                binding.noData.setVisibility(View.GONE);
                binding.rvOrder.setVisibility(View.VISIBLE);
                orderAdapter.setData(orderList);
            } else {
                binding.noData.setVisibility(View.VISIBLE);
                binding.rvOrder.setVisibility(View.GONE);
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