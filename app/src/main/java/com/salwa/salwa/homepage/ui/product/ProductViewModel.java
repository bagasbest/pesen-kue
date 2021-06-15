package com.salwa.salwa.homepage.ui.product;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;



import java.util.ArrayList;

public class ProductViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<ProductModel>> listProduct = new MutableLiveData<>();
    final ArrayList<ProductModel> productArrayList = new ArrayList<>();


    private static final String TAG = ProductViewModel.class.getSimpleName();
    private double totalRating = 0.0;
    int total = 0;
    int totalData = 0;


    public void setProductList() {
        productArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("product")
                    .get()
                    .addOnCompleteListener(task -> {

                        totalData = task.getResult().size();

                        if (task.isSuccessful() && totalData > 0) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String productId = "" + document.get("productId");
                                String title = "" + document.get("title");
                                String description = "" + document.get("description");
                                String image = "" + document.get("productDp");
                                int price = Integer.parseInt("" + document.get("price"));
                                int likes = Integer.parseInt("" + document.get("likes"));

                                getRating(productId, title, description, image, price, likes);

                            }
                        } else if(totalData == 0) {
                            Log.d(TAG, "Total data 0", task.getException());
                            listProduct.postValue(productArrayList);
                        }
                        else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getRating(String productId, String title, String description, String image, int price, int likes) {

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("product")
                    .document(productId)
                    .collection("rating")
                    .get()
                    .addOnCompleteListener(task -> {
                        total = task.getResult().size();

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                // ambil rating jika ada produk
                                if (document.exists()) {

                                    double rating = Double.parseDouble("" + document.get("rating"));
                                    totalRating = totalRating + rating;
                                }
                            }


                            ProductModel productModel = new ProductModel();
                            productModel.setTitle(title);
                            productModel.setDescription(description);
                            productModel.setProductId(productId);
                            productModel.setImage(image);
                            productModel.setPrice(price);
                            if (total == 0) {
                                productModel.setLikes(0.0);
                            } else {
                                productModel.setLikes((double) totalRating / total);
                            }
                            productModel.setPersonRated(total);




                            productArrayList.add(productModel);


                            totalRating = 0;
                            total = 0;

                            if (totalData == productArrayList.size()) {
                                listProduct.postValue(productArrayList);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
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