package com.salwa.salwa.homepage.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}