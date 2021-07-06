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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.salwa.salwa.databinding.FragmentOrderBinding;

public class OrderFragment extends Fragment {

    private FragmentOrderBinding binding;
    private OrderAdapter orderAdapter;
    private String uid = "";

    @Override
    public void onResume() {
        super.onResume();
        checkIsAdminOrNot();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Order");
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrderBinding.inflate(inflater, container, false);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        return binding.getRoot();
    }

    private void checkIsAdminOrNot() {
        // CEK APAKAH USER YANG SEDANG LOGIN ADMIN ATAU BUKAN, JIKA YA, MAKA TAMPILKAN tombol add product
        binding.progressBar.setVisibility(View.VISIBLE);

        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(("" + document.get("role")).equals("admin")) {
                            // user yang login adalah admin
                            initRecyclerView();
                            initViewModel("admin");
                        } else {
                            // user yang login adalah pengguna biasa/kustomer
                            initRecyclerView();
                            initViewModel("user");
                        }
                    }
                });
    }

    private void initRecyclerView() {
        binding.rvOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderAdapter = new OrderAdapter();
        orderAdapter.notifyDataSetChanged();
        binding.rvOrder.setAdapter(orderAdapter);
    }

    private void initViewModel(String role) {
        // tampilkan daftar belanjaan di Halaman Order/Payment
        OrderViewModel orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        if (role.equals("admin")) {
            orderViewModel.setOrderListByAdminSide();
        } else {
            orderViewModel.setOrderList(uid);
        }
        orderViewModel.getOrderList().observe(getViewLifecycleOwner(), orderList -> {
            if (orderList.size() > 0) {
                binding.noData.setVisibility(View.GONE);
                orderAdapter.setData(orderList);
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