package com.salwa.salwa.homepage.ui.product;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.salwa.salwa.LoginActivity;
import com.salwa.salwa.R;
import com.salwa.salwa.databinding.FragmentProductBinding;

import org.jetbrains.annotations.NotNull;

public class ProductFragment extends Fragment {

    private FragmentProductBinding binding;
    private String uid;
    private boolean isVisible = true;

    @Override
    public void onResume() {
        super.onResume();
        initViewModel();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Produk Tersedia");
        setHasOptionsMenu(true);


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProductBinding.inflate(inflater, container, false);


        // cek apakah user yang sedang login ini admin atau user biasa
        checkIsAdminOrNot();


        // show hide selamat datang
        showHideNameAndGreeting();

        return binding.getRoot();
    }

    // inisiasi view model untuk menampilkan list produk
    private void initViewModel() {
        ProductViewModel productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        // tampilkan daftar cookies
        binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        ProductAdapter productAdapter = new ProductAdapter();
        binding.recyclerView.setAdapter(productAdapter);

        binding.progressBar.setVisibility(View.VISIBLE);
        productViewModel.setProductList();
        productViewModel.getProductList().observe(getViewLifecycleOwner(), productList -> {
            if (productList.size() > 0) {
                binding.noData.setVisibility(View.GONE);
                productAdapter.setData(productList);
            } else {
                binding.noData.setVisibility(View.VISIBLE);
            }
            binding.progressBar.setVisibility(View.GONE);
        });
    }

    private void showHideNameAndGreeting() {
        binding.showHideName.setOnClickListener(view -> {
            if(isVisible) {
                binding.nameTv.setVisibility(View.GONE);
                binding.textView2.setVisibility(View.GONE);
                isVisible = false;
            } else {
                binding.nameTv.setVisibility(View.VISIBLE);
                binding.textView2.setVisibility(View.VISIBLE);
                isVisible = true;
            }
        });
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
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        binding.nameTv.setText("Halo, " + document.get("name"));
                        if (("" + document.get("role")).equals("admin")) {
                            binding.addProduct.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater2) {
        inflater2.inflate(R.menu.menu_logout, menu);
        super.onCreateOptionsMenu(menu, inflater2);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == R.id.navigation_logout) {
            showConfirmDialog();
        }
        return true;

    }

    private void showConfirmDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah anada yakin ingin keluar aplikasi ?")
                .setPositiveButton("YA", (dialogInterface, i) -> {
                    // sign out dari firebase autentikasi
                    FirebaseAuth.getInstance().signOut();

                    // go to login activity
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    dialogInterface.dismiss();
                    startActivity(intent);
                    getActivity().finish();

                })
                .setNegativeButton("TIDAK", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}