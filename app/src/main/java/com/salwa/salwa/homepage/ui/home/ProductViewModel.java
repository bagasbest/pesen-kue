package com.salwa.salwa.homepage.ui.home;

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
                                String shopId1 = "" + document.get("shopId");
                                String shopDp = "" + document.get("shopDp");
                                String description = "" + document.get("description");
                                String quantity = "" + document.get("quantity");
                                String title = "" + document.get("title");
                                String shopName = "" + document.get("shopName");
                                String image = "" + document.get("productDp");
                                int price = Integer.parseInt("" + document.get("price"));

                                getRating(productId, title, shopName, image, price,  shopId1, shopDp, description, quantity);

                            }
                        } else if (totalData == 0) {
                            Log.d(TAG, "Total data 0", task.getException());
                            listProduct.postValue(productArrayList);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getRating(String productId, String title, String shopName, String image, int price, String shopId, String shopDp, String description, String quantity) {

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
                            productModel.setShopName(shopName);
                            productModel.setProductId(productId);
                            productModel.setImage(image);
                            productModel.setPrice(price);
                            productModel.setShopId(shopId);
                            productModel.setShopDp(shopDp);
                            productModel.setDescription(description);
                            productModel.setQuantity(quantity);
                            if (total == 0) {
                                productModel.setLikes(0.0);
                            } else {
                                productModel.setLikes(totalRating / total);
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


    public void setProductListById(String shopId) {
        productArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("product")
                    .whereEqualTo("shopId", shopId)
                    .get()
                    .addOnCompleteListener(task -> {

                        totalData = task.getResult().size();

                        if (task.isSuccessful() && totalData > 0) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String productId = "" + document.get("productId");
                                String shopId1 = "" + document.get("shopId");
                                String shopDp = "" + document.get("shopDp");
                                String description = "" + document.get("description");
                                String quantity = "" + document.get("quantity");
                                String title = "" + document.get("title");
                                String shopName = "" + document.get("shopName");
                                String image = "" + document.get("productDp");
                                int price = Integer.parseInt("" + document.get("price"));

                                getRatingProductListById(productId, title, shopName, image, price, shopId1, shopDp, description, quantity);

                            }
                        } else if (totalData == 0) {
                            Log.d(TAG, "Total data 0", task.getException());
                            listProduct.postValue(productArrayList);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getRatingProductListById(String productId, String title, String shopName, String image, int price, String shopId, String shopDp, String description, String quantity) {

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
                            productModel.setShopName(shopName);
                            productModel.setProductId(productId);
                            productModel.setImage(image);
                            productModel.setPrice(price);
                            productModel.setShopId(shopId);
                            productModel.setShopDp(shopDp);
                            productModel.setDescription(description);
                            productModel.setQuantity(quantity);
                            if (total == 0) {
                                productModel.setLikes(0.0);
                            } else {
                                productModel.setLikes(totalRating / total);
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