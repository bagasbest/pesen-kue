package com.salwa.salwa.homepage.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.salwa.salwa.databinding.FragmentProductBinding;


public class ProductFragment extends Fragment {

    private FragmentProductBinding binding;
    private boolean isVisible = true;

    @Override
    public void onResume() {
        super.onResume();
        initViewModel();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Home");
        setHasOptionsMenu(true);


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProductBinding.inflate(inflater, container, false);

        // get user name
        getUserName();

        // show hide selamat datang
        showHideNameAndGreeting();

        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    private void getUserName() {
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    String name = "" + documentSnapshot.get("name");
                    binding.nameTv.setText("Halo, " + name);
                });
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}