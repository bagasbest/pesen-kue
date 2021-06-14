package com.salwa.salwa.homepage.ui.cart;

import android.util.Log;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.util.ArrayList;

public class CartViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<CartModel>> cartList = new MutableLiveData<>();
    private static final String TAG = CartViewModel.class.getSimpleName();

    public void setCartList(String customerUid) {
        final ArrayList<CartModel> cartArrayList = new ArrayList<>();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("cart")
                    .whereEqualTo("userUid", customerUid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CartModel cartModel = new CartModel();
                                cartModel.setAddedAt("" + document.get("addedAt"));
                                cartModel.setBookedBy("" + document.get("bookedBy"));
                                cartModel.setDescription("" + document.get("description"));
                                cartModel.setPrice(Integer.parseInt("" + document.get("price")));
                                cartModel.setProductDp("" + document.get("productDp"));
                                cartModel.setProductId("" + document.get("productId"));
                                cartModel.setTitle("" + document.get("title"));
                                cartModel.setTotalProduct(Integer.parseInt("" + document.get("totalProduct")));
                                cartModel.setUserUid("" + document.get("userUid"));
                                cartModel.setCartId("" + document.get("cartId"));

                                cartArrayList.add(cartModel);
                            }
                            cartList.postValue(cartArrayList);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public LiveData<ArrayList<CartModel>> getCartList() {
        return cartList;
    }
}