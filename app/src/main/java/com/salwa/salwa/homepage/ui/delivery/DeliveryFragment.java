package com.salwa.salwa.homepage.ui.delivery;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.salwa.salwa.databinding.FragmentDeliveryBinding;

import org.jetbrains.annotations.NotNull;

public class DeliveryFragment extends Fragment {

    private FragmentDeliveryBinding binding;
    private DeliveryAdapter deliveryAdapter;
    private String uid = "";

    @Override
    public void onResume() {
        super.onResume();
        initViewModel();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Delivery");
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDeliveryBinding.inflate(inflater, container, false);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return binding.getRoot();
    }

    private void initViewModel() {
        // tampilkan daftar delivery
        DeliveryViewModel deliveryViewModel = new ViewModelProvider(this).get(DeliveryViewModel.class);

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.rvDelivery.setLayoutManager(new LinearLayoutManager(getActivity()));
        deliveryAdapter = new DeliveryAdapter();
        binding.rvDelivery.setAdapter(deliveryAdapter);

        deliveryViewModel.setDeliveryList(uid);
        deliveryViewModel.getDeliveryList().observe(getViewLifecycleOwner(), deliveryList -> {
            if (deliveryList.size() > 0) {
                binding.noData.setVisibility(View.GONE);
                deliveryAdapter.setData(deliveryList);
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