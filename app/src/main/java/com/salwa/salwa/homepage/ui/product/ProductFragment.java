package com.salwa.salwa.homepage.ui.product;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.salwa.salwa.databinding.FragmentProductBinding;

import org.jetbrains.annotations.NotNull;

public class ProductFragment extends Fragment {

    private FragmentProductBinding binding;
    private String uid;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProductViewModel productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        binding = FragmentProductBinding.inflate(inflater, container, false);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // cek apakah user yang sedang login ini admin atau user biasa
        checkIsAdminOrNot();

        // tampilkan daftar cookies
        binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        ProductAdapter productAdapter = new ProductAdapter();
        productAdapter.notifyDataSetChanged();
        binding.recyclerView.setAdapter(productAdapter);

        binding.progressBar.setVisibility(View.VISIBLE);
        productViewModel.setProductList();
        productViewModel.getProductList().observe(getViewLifecycleOwner(), productList -> {
            if (productList.size() > 0) {
                binding.noData.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
                productAdapter.setData(productList);
            } else {
                binding.noData.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);
            }
            binding.progressBar.setVisibility(View.GONE);
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // klik tombol tambah produk
        binding.addProduct.setOnClickListener(view1 ->
                startActivity(new Intent(getActivity(), AddProductActivity.class)));
    }

    @SuppressLint("SetTextI18n")
    private void checkIsAdminOrNot() {
        // CEK APAKAH USER YANG SEDANG LOGIN ADMIN ATAU BUKAN, JIKA YA, MAKA TAMPILKAN tombol add product
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        binding.nameTv.setText("Halo, " + document.get("name"));
                        if (document.get("role") == "admin") {
                            binding.addProduct.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}