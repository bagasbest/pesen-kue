package com.salwa.salwa.homepage.ui.product;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ProductViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<ProductModel>> listProduct = new MutableLiveData<>();
    private static final String TAG = ProductViewModel.class.getSimpleName();

    public void setProductList() {
        final ArrayList<ProductModel> productArrayList = new ArrayList<>();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("product")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    ProductModel productModel = new ProductModel();
                                    productModel.setTitle("" + document.get("title"));
                                    productModel.setDescription("" + document.get("description"));
                                    productModel.setProductId("" + document.get("productId"));
                                    productModel.setPrice(Integer.parseInt("" + document.get("price")));
                                    productModel.setImage("" + document.get("productDp"));
                                    productModel.setLikes(Integer.parseInt("" + document.get("likes")));

                                    productArrayList.add(productModel);
                                }
                                listProduct.postValue(productArrayList);
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public LiveData<ArrayList<ProductModel>> getProductList() {
        return listProduct;
    }

}